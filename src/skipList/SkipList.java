package skipList;

public class SkipList<T extends Comparable<? super T>> {
	
	private int size = 0;
	private static int max;
	protected final SkipNode<T> head;
	
	/**
	 * SkipList constructor that takes a MAX level threshold for parameter.
	 * @empty constructor
	 */
	public SkipList(int max_level) {
		max = max_level;
		head = new SkipNode<T>(null, max_level + 1);
	}
	
	/**
	 * @function randomLevel()
	 * randomizeLevel generates a number inclusive of 0 and our given MAX
	 * returning number with an increasing lower chance of appearance the higher
	 * it is.
	 * 
	 * @return int between low_bound, max.
	 */
	private int randomLevel() {
	    int low_bound = (int)(Math.log(1.0 - Math.random())/Math.log(1.0 - .5));
	    return Math.min(low_bound, max);
	} 
	
	/**
	 * Insert function takes some generic value, instantiating a temporary SkipNode 
	 * and an array of SkipNode pointers and assigns the value accordingly, updating references
	 * and assigning level values as well.
	 * 
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	protected void insert(T value) {
		SkipNode<T> current = head;
		SkipNode<T>[] ref_array = new SkipNode[max + 1];
		
		//Update pointer array for current and assign to ref.
		for (int i = max; i >= 0; i--) {
			while(current.nodeArray[i] != null
					&& current.nodeArray[i].data.compareTo(value) < 0) {
				current = current.nodeArray[i];
			}
			ref_array[i] = current;
		}
		current = current.nodeArray[0];
		
		//Assign random level to current and update. Check for duplicates, otherwise create new Node.
		if(current == null || ! current.data.equals(value)) {
			int current_level = randomLevel();
			
			//Formally update ref_array
			if (current_level > max) {
				for (int i = max + 1; i <= current_level; i++) {
					ref_array[i] = head;
				}
				max = current_level;
			}
			//Insert
			current = new SkipNode<T>(value, max);
			for(int i = 0; i < max; i++) {
				current.nodeArray[i] = ref_array[i].nodeArray[i];
				ref_array[i].nodeArray[i] = current;
			}
			size++;
		}
	}
	
	/**
	 * Takes some value to be deleted, updates SkipNode pointer array,
	 * removes SkipNode, and if necessary, decrements level.
	 * 
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public void delete(T value) {
		
		SkipNode<T> current = head;
		SkipNode<T>[] ref_array = new SkipNode[max + 1];
		
		//update node pointer array
		for(int i = max; i >= 0; i--) {
			while(current.nodeArray[i] != null 
					&& current.nodeArray[i].data.compareTo(value) < 0) {
				current = current.nodeArray[i];
			}
			ref_array[i] = current;
		}
		current = current.nodeArray[0];
		
		//Remove node and decrement level as necessary.
		if(current.data.equals(value)) {
			for(int i = 0; i <= max; i++) {
				if (ref_array[i].nodeArray[i] != current)
					break;
				ref_array[i].nodeArray[i] = current.nodeArray[i];
			}
			//decrement level
			while (max > 0 && head.nodeArray[max] == null) {
				max--;
			}
			size--;
		}
		else return;	
	}
	
	/**
	 * Takes a value and compares to data of each member of current's Node array.
	 * 
	 * @param value
	 * @return true or false
	 */
	public boolean contains(T value) {
		SkipNode<T> current = head;
	
		//grep at node array spot [0..max] 
		for(int i = max; i >= 0; i--) {
			while(current.nodeArray[i] != null &&
					current.nodeArray[i].data.compareTo(value) < 0) {
				current = current.nodeArray[i];
			}
		}
		current = current.nodeArray[0];
		if(current != null && current.data.equals(value))
			return true;
		else return false; 
	}
	
	public int sizeOf() {
		return size;
	}
	
}