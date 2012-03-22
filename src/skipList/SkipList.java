/**
 * @author Wyatt Pillmore
 * 
 * A skip list is a data structure for storing a sorted list of items 
 * using a hierarchy of linked lists that connect increasingly sparse 
 * subsequences of the items.
 * 
 * @complexity: 
 */
package skipList;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
   
	public SkipNode<T> head; 
	protected SkipNode<T> cPointer; //current reference pointer node.
	public SkipNode<T>[] update; //updateable pointer array
	public final int maxLevel;
	int size = 0, level;
	
	/**
	 * Constructor that takes a maximum level parameter.
	 * @param maxLevel
	 */
	public SkipList(int maxLevel) {
		this.maxLevel = maxLevel;
		head = new SkipNode<T>(null, maxLevel);
		for(int i = 0; i < maxLevel; i++)
			head.next[i] = null;
	}
	
	/**
	 * Generate random number at decreasing percentage rate the higher it goes.
	 * @return - random number between 0 and max
	 */
	public int generateRandomLevel() {
		Random random = new Random();
		int rand = (int) (Math.log(1.0 - random.nextDouble())/Math.log(1-.5)); //Increasingly lower chance of maxLevel occurrences
		return Math.min(rand, maxLevel - 1);
	}
	
	/**
	 * Method updates using update(value) and inserts node appropriately
	 * @param value - value to be inserted
	 */
	public void insert(T value) {
		if(value == null)
			return;
		
		else update(value);
		SkipNode<T> node = cPointer;
		SkipNode<T>[] temp = update;
		
		if(node == null || ! node.data.equals(value)) {
			int lvl = generateRandomLevel();
			
			if(lvl > level) {
				for(int i = level + 1; i <= lvl; i++) {
					temp[i] = head;
				}
				level = lvl;
			}
			
			node = new SkipNode<T>(value, lvl);
			for(int i = 0; i <= lvl - 1; i++) {
				node.next[i] = temp[i].next[i];
				temp[i].next[i] = node;
			}
		}
		size++;
 	}
	
	/**
	 * Updates and removes references to node with designated value and
	 * decrements level as needed.
	 * @param value - Value to be deleted.
	 */
	public void delete(T value) {
		if(value == null || isEmpty() == true)
			return;
		else update(value);
	
		SkipNode<T> node = cPointer;
		SkipNode<T>[] tmp = update;
		if(node.data.equals(value)) {
			for(int i = 0; i <= level; i++) {
				if(tmp[i].next[i] != node)
					break;
				tmp[i].next[i] = node.next[i];
			}
			
			while(level > 0 && head.next[level] == null)
				level--;
		}
	}
	
	/**
	 * Method to return the skipNode of the value we need.
	 * @param value - value whose node container is returned
	 * @return - SkipNode carrying the designated value.
	 */
	public SkipNode<T> find(T value) {
		if(value == null || isEmpty() == true)
			return null;
		else update(value);
		if(cPointer != null && cPointer.data.equals(value))
			return cPointer;
		else return null;
	}
	
	/**
	 * Contains method greps SkipList and returns true or false depended on
	 * whether T value is present
	 * @param value - value to check against in SkipList
	 * @return - true or false
	 */
	public boolean contains(T value) {
		if(value == null || isEmpty() == true)
			return false;
		
		SkipNode<T> x = head;
		for(int i = level; i >= 0; i--) {
			while(x.next[i] != null && x.next[i].data.compareTo(value) < 0) {
				x = x.next[i];
			}
		}
		x = x.next[0];
		return x != null && x.data.equals(value);
	
	}
	
	/**
	 * Updater method to update our current Pointer (cPointer)
	 * and references.
	 * @param value - value to set cPointer reference to.
	 */
	@SuppressWarnings("unchecked")
	protected void update(T value) {
		SkipNode<T> x = head;
		SkipNode<T> [] temp = new SkipNode[maxLevel];
		
		for(int i = level; i >= 0; i--) {
			while(x.next[i] != null && x.next[i].data.compareTo(value) < 0) {
				x = x.next[i];
			}
			temp[i] = x;
		}
		cPointer = x.next[0];
		update = temp;
	}

	/**
	 * Method to return the size of the skipList.
	 * Size is incremented and decremented by our methods.
	 * @return size of SkipList
	 */
	public int sizeOf() {
		return isEmpty() == true ? null : size; 
	}
	
	/**
	 * Method to safeguard against null skipList.
	 * @return true if head == null, or false if not;
	 */
	public boolean isEmpty() {
		return head == null ? true : false;
	}
	
	/**
	 * return an empty head, creating a null SkipList.
	 */
	public void makeEmpty() {
		head = null;
	}
}