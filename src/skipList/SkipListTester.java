package skipList;

public class SkipListTester {
	public static void main(String[] args) {
		int max = 13;
		SkipList<Integer> x = new SkipList<Integer>(max);
	
		System.out.println(x.isEmpty());
		x.makeEmpty();
		System.out.println(x.isEmpty());
		
		for(int i = 0; i < 100; i++)
			System.out.println("Contains " + i + ":\t" + x.contains(i));
		
		for (int i = 0; i < 100; i++)
			x.insert(i);
		for(int i = 0; i < 100; i++)  {
			if(i % 2 == 0)
				x.delete(i);
		System.out.println("Contains " + i + ":\t" + x.contains(i) + "\tFound: " + x.find(i));
		}
		
		System.out.println(x.sizeOf());
		System.out.println(x.getLevel());
		System.out.println(x.isEmpty());		
		x.makeEmpty();
		
		long start = System.nanoTime();
		for(int i = 0; i < 100; i++){
			x.insert(i);
			System.out.println("MaxLevel: " + max + "\tLevel: " + x.generateRandomLevel() + "\tInserting: " + i + "\tSize: " + x.sizeOf() + "\t Time(ms): " + (System.nanoTime() - start)/1000000);
		}
		System.out.println("Total Time: " + (System.nanoTime() - start)/1000000 + "\tSize: " + x.sizeOf());
		
		
		
	}
}