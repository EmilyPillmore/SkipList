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
import java.util.Collection;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
   
	private  SkipNode<T> root; 
	protected SkipNode<T> cPointer; //current reference pointer node.
	protected SkipNode<T>[] pArray; //pArrayable pointer array
	public final int maxLevel;
	int size = 0, level;
	
	/**
	 * Constructor that takes a maximum level parameter.
	 * @param maxLevel
	 */
	public SkipList(int maxLevel) {
		this.maxLevel = maxLevel;
		root = new SkipNode<T>(null, maxLevel); 
		for(int i = 0; i < maxLevel; i++)
			root.next[i] = null;
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
	 * Method pArrays using pArray(value) and inserts node appropriately
	 * @param value - value to be inserted
	 */
	public void insert(T value) {
		if(value == null)
			return;
		else updatePointers(value);
		
		SkipNode<T> node_t = cPointer;
		SkipNode<T>[] array_t = pArray;
		
		if(node_t == null || ! node_t.data.equals(value)) {
			int level_t = generateRandomLevel();
			
			if(level_t > level) {
				for(int i = level + 1; i <= level_t; i++) {
					array_t[i] = root;
				}
				level = level_t;
			}
			
			node_t = new SkipNode<T>(value, level_t);
			for(int i = 0; i <= level_t; i++) {
				node_t.next[i] = array_t[i].next[i];
				array_t[i].next[i] = node_t;
			}
		}
		size++;
 	}
	
	/**
	 * Takes a collection of values to add to SkipList and inserts them
	 * if they are not null;
	 * @param values - collection of values to be inserted.
	 */
	protected void insertAll(Collection<T> values) {
		for(T type_t : values) 
			if (type_t == null) 
				return;
			else {
				updatePointers(type_t);
				insert(type_t);
				size++;
			}
	}
	
	/**
	 * pArrays and removes references to node with designated value and
	 * decrements level as needed.
	 * @param value - Value to be deleted.
	 */
	public void delete(T value) {
		if(value == null || isEmpty() == true)
			return;
		else updatePointers(value);
	
		SkipNode<T> node_t = cPointer;
		SkipNode<T>[] array_t = pArray;
		if(node_t.data.equals(value)) {
			for(int i = 0; i <= level; i++) {
				if(array_t[i].next[i] != node_t)
					break;
				array_t[i].next[i] = node_t.next[i];
			}
			
			while(level > 0 && root.next[level] == null)
				level--;
		}
		size--;
	}
	
	/**
	 * Takes a collection of values to be deleted and deltes if they are not null;
	 * @param values - collection to be deleted.
	 */
	protected void deleteAll(Collection<T> values) {
		for(T type_t : values) 
			if (type_t == null) 
				return;
			else {
				updatePointers(type_t);
				delete(type_t);
				size--;
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
		else updatePointers(value);

		SkipNode<T> node_t = cPointer;
		return node_t != null && node_t.data.equals(value) 
				? node_t : null;
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
		else updatePointers(value);
		
		SkipNode<T> node_t = cPointer;
		return node_t != null && node_t.data.equals(value);
	}
	
	/**
	 * pArrayr method to pArray our current Pointer (cPointer)
	 * and references.
	 * @param value - value to set cPointer reference to.
	 */
	@SuppressWarnings("unchecked")
	protected void updatePointers(T value) {
		if(root == null)
			root = new SkipNode<T>(null, maxLevel);
		
		SkipNode<T> node_t = root;
		SkipNode<T> [] array_t = new SkipNode[maxLevel + 1];
		
		for(int i = level; i >= 0; i--) {
			while(node_t.next[i] != null && node_t.next[i].data.compareTo(value) < 0) {
				node_t = node_t.next[i];
			}
			array_t[i] = node_t;
		}
		cPointer = node_t.next[0];
		pArray = array_t;
	}

/* ----------- Utility Methods ------------ */
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
	 * @return true if root == null, or false if not;
	 */
	protected boolean isEmpty() {
		return root == null ? true : false;
	}
	
	/**
	 * @return an empty root, creating a null SkipList.
	 */
	public void makeEmpty() {
		root = null;
		size = 0;
	}
	
	/**
	 * @return level
	 */
	public int getLevel() {
		return level;
	}
}