package skipList;

/**
 * @author Wyatt Pillmore
 * skipNode Class constructor consisting of skipNode array and public data.
 * Constructor sets height of array and value of data.
 * Implements Comparable as requested.
 */
public class SkipNode<T extends Comparable<? super T>> {

	public final SkipNode<T>[] nodeArray;
	public final T data;
	
	@SuppressWarnings("unchecked")
	public SkipNode(T data, int height) {
		nodeArray = new SkipNode[height];
		this.data = data;
	}
	public int height() {
		return nodeArray.length - 1;
	}
}
