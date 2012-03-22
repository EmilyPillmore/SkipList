package skipList;

/**
 * @author Wyatt Pillmore
 * 
 * skipNode Class constructor consisting of skipNode array and public data.
 * Constructor sets height of array and value of data.
 * Implements Comparable as requested.
 * 
 */
public class SkipNode<T extends Comparable<? super T>> {

	public SkipNode<T>[] next;
	public T data;
	public int height;
	
	@SuppressWarnings("unchecked")
	public SkipNode (T data, int level) {
		next = new SkipNode[level];
		height = level;
		this.data = data;
		
		for(int i = 0; i < next.length; i++)
			next[i] = null;
	}
}
