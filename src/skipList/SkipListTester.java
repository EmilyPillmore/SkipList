package skipList;
import java.util.ArrayList;

public class SkipListTester {
	public static void main(String[] args) {
		SkipList<Integer> x = new SkipList<Integer>(5);
		ArrayList<Integer> arbitrary = new ArrayList<Integer>();
		
		for (int i = 0; i < 100; i++) {
			x.insert(i);
		}
		
		System.out.println("Size: " + x.sizeOf());
		for (int i = 0; i < 100; i++) {
			System.out.println("contains " + i + ":\t" + x.contains(i));
		}
		
		for (int i = 0; i < 100; i++) {
			if (i % 3 == 0) { 
				x.delete(i);
				arbitrary.add(i);
			}
		}
		
		arbitrary.trimToSize();
		for (int i = 0; i < 100; i++) {
			System.out.println("contains " + i + ":\t" + x.contains(i));
		}
		System.out.println("Size: " + x.sizeOf());
			
	}
}
