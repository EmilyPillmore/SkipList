/**
 * @author Arabian
 * 
 * Naive base indexed implementation of the SkipList data structure. Ready for Serialization,
 * Iteration, and concurrent mapping.
 * Amortized logarithmic complexity for search, insertion, deletion, and contains queries.
 * 
 * Original by William Pugh.
 * 
 * @date 4/4/12;
 */
package skipList;

import java.util.Collection;
import java.util.Random;

@SuppressWarnings("unchecked")
public class SkipList<T extends Comparable<? super T>> {
	
	/**
	 *Static SkipNode constructor. Maintains our data,
	 *prev pointer, next node pointers, and distance.
	 *
	 * @param <T>
	 */
	private static class SkipNode<T extends Comparable<? super T>> {
		private final T data;
		private SkipNode<T> prev;
		private final SkipNode<T>[] next;
		private int[] span;
		
		public SkipNode (T data, int level) {
			this.data = data;
			next = new SkipNode[level];
			span = new int[level];
		}
	}
	
	/* Global vars */
	private SkipNode<T> head;
	private SkipNode<T>[] update;
	private int[] index;
	
	private int size = 0, level = 1;
	private static final double P = 0.5; //Pugh's skiplist cookbook.
	private final int MAX_LEVEL;
	public final Random random = new Random();
	public int t;
	
	/**
	 * SkipList constructor takes MAX_LEVEL and builds head and necessary 
	 * variables using private build() method;
	 * 
	 * @param level
	 */
	public SkipList(int level) {
		this.MAX_LEVEL = level;
		t = 0;
		build(MAX_LEVEL);
	}
	private void build(int level) {
		head = new SkipNode<T>(null, level);
		update = new SkipNode[level];
		index = new int[level];
		
		for(int i = 0; i < level; i++) {
			head.next[i] = head; //set head pointers and distance
			head.span[i] = 1;
		}
		head.prev = head;
	}
	
	/**
	 * Insert method using the standard algorithm. All pointers maintained. and updated,
	 * along with size.
	 * 
	 * @param value
	 * @return true or false if successful or not.
	 */
	public boolean insert(T value) {
		if(value == null)
			return false;
		
		final int level_t = randomLevel();
		SkipNode<T> x = head;
		SkipNode<T> y = head;
		int i;
		int ind = 0;
		
		for(i = level - 1; i >= 0; i--) {
			while(x.next[i] != y && x.next[i].data.compareTo(value) < 0) {
				ind += x.span[i];
				t++;
				x = x.next[i];
			}
			y = x.next[i];
			update[i] = x;
			index[i] = ind;
		}
		
		if (level_t > level) {
			for(i = level; i < level_t; i++) {
				head.span[i] = size + 1;
				update[i] = head;
			}
			level = level_t;
		}
		
		x = new SkipNode<T>(value, level_t);
		for(i = 0; i < level; i++) {
			if (i > level_t - 1)
				update[i].span[i]++;
			else {
				x.next[i] = update[i].next[i];
				update[i].next[i] = x;
				x.span[i] = index[i] + update[i].span[i] - ind;
				update[i].span[i] = ind + 1 - index[i];
			}
		}
		x.prev = update[0];
		x.next[0].prev = x;
		size++;
		return true;
	}
	
	/**
	 * insertAll takes java.util.Collection of any object type and inserts.
	 * using our insert method.
	 * @param values
	 */
	public void insertAll(Collection<? extends T> values) {
		for(T x : values) {
			insert(x);
		}
	}
	
	/**
	 * Remove method takes element and redistributes pointers. The actual deletion
	 * is relegated to our delete() method.
	 * @param element
	 * @return true or false
	 */
	public boolean remove(T element) {
		if(element == null)
			return false;
		
		SkipNode<T> x = head;
		for(int i = level - 1; i >= 0; i--) {
			while(x.next[i] != head && x.next[i].data.compareTo(element) < 0)
				x = x.next[i];
			update[i] = x;
		}
		x = x.next[0];
		if(x == head || x.data.compareTo(element) != 0)
			return false;
		delete(x, update);
		return true;
	}

	
	/**
	 * same as above, but takes index instead of value parameters.
	 * 
	 * @param index
	 * @return value at index 
	 */
	public T removeIndex(int index) {
		SkipNode<T> x = head;
		int ind = 0;
		for(int i = level - 1; i >= 0; i--) {
			while(ind + x.span[i] <= index) {
				ind += x.span[i];
				x = x.next[i];
			}
			update[i] = x;
		}
		x = x.next[0];
		delete(x, update);
		return x.data;
	}
	public void removeAllByValue(Collection<? extends T> values) {
		for(T x : values)
			remove(x);
	}
	public void removeAllByIndex(Collection<Integer> values) {
		for(Integer x : values) 
			removeIndex((int) x);
	}
	
	/**
	 * our main deletion method. Takes our updated
	 * references and redistributes to lazily delete node.
	 * 
	 * @param node - our current node.
	 * @param update - updated reference array.
	 */
	private void delete(SkipNode<T> node, SkipNode<T>[] update) {
		for (int i = 0; i < level; i++)
			if (update[i].next[i] == node) {
				update[i].next[i] = node.next[i];
				update[i].span[i] += node.span[i] - 1;
			} 
			else update[i].span[i]--;
		
		node.next[0].prev = node.prev;
		while (head.next[level] == head && level > 1)
			level--;
		size--;
	}
	
	/**
	 * Concurrent random number generator.
	 * 
	 * @return randomly generated int
	 */
	protected int randomLevel() {
		
		int x = random.nextInt() | 0x100; //256
       		x ^= x << 13;
        	x ^= x >>> 17;
        	x ^= x << 5;
        	if ((x & 0x8001) != 0) // test highest and lowest bits
            		return 1;
       		int level = 1;
       		
        	while (((x >>>= 1) & 1) != 0) ++level;
        		return level+1; 
	}
	
	/**
	 * Contains method simply returns boolean using searchBy* methods.
	 * @param x
	 * @return true or false
	 */
	public boolean containsElement(T x) {
		return searchByElement(x) != null;
	}
	
	/**
	 * Uses the basic skiplist algorithm to find relevant node.
	 * 
	 * @param element
	 * @return SkipNode<T> with relevant value
	 */
	public SkipNode<T> searchByElement(T element) {
		SkipNode<T> curr = head;
		for (int i = level - 1; i >= 0; i--)
			while (curr.next[i] != head && curr.next[i].data.compareTo(element) < 0)
				curr = curr.next[i];
		curr = curr.next[0];
		
		if (curr != head && curr.data.equals(element))
			return curr;
		return null;
	}
	
	/**
	 * Same as above, but greps using given index parameter.
	 *  
	 * @param index
	 * @return SkipNode<T> with relevant index.
	 */
	private SkipNode<T> searchByIndex(int index) {
		if(index > size)
			return null;
		
		SkipNode<T> curr = head;
		int ind = -1;
		for (int i = level - 1; i >= 0; i--)
			while (ind + curr.span[i] <= index) {
				ind += curr.span[i];
				curr = curr.next[i];
			}
		return curr;
	}
	
	/**
	 * Simple index value get method.
	 * @param index
	 * @return value at index.
	 */
	public T get(int index) {
		return !(index > size) ? searchByIndex(index).data : null;
	}
	
	/* -------------- Utility Methods ------------- */
	
	/**
	 * Clearing utility method. 
	 */
	public void makeEmpty() {
		head = null;
		build(MAX_LEVEL);
		size = 0;
	}
	
	/**
	 * Checks if empty
	 * @return true or false;
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Size utility
	 * @return current size;
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Traversal count
	 * @return traversal count;
	 */
	public int traverse() {
		return t;
	}
}