package skipList;

public class SkipListTester {
	public static void main(String[] args) {
		int max = 18;
		SkipList<Integer> x = new SkipList<Integer>(max);
		System.out.println("Beginning Testing.\nPrinting 1million items...");
		
		/* ------------------ Timing Code ------------------- */
		long start_t = System.nanoTime();
		for(int i = 0; i < 100; i++) {
			for(int k = 0; k < 10000; k++) {
				x.insert(i);
			}
			System.out.println((System.nanoTime() - start_t)/1000000 + "\t" + i);
		}
		System.out.println("Total time for 1000000 items: \t" + (System.nanoTime() - start_t)/1000000);
		/*
		for(int i = 0; i < 1000000; i++) {
			x.insert(i);
			System.out.println(i);
		}
		double failCount = 0;
		for(int i = 0; i < 1000000; i++) {
			System.out.println("Contains " + i + ":\t" + x.contains(i));
			if (x.contains(i) == false)
				failCount++;
		}
		System.out.println("\nInsert failure count:\t" + (int) failCount);
		*/
		/* ----------------- End Timing code --------------- */
		
		
		System.out.println("Is Empty: \t" + x.isEmpty() + "\nMaking empty...");
		x.makeEmpty();
		System.out.println("Is Empty: \t" + x.isEmpty());
		
		for(int i = 0; i < 100; i++)
			System.out.println("Contains " + i + ":\t" + x.contains(i));
		
		for (int i = 0; i < 100; i++)
			x.insert(i);
		for(int i = 0; i < 100; i++)  {
			if(i % 2 == 0)
				x.delete(i);
			try { 
				System.out.println("Contains " + i + ":\t" + x.contains(i) + "\tFound: " + x.find(i) + ": \t" + x.find(i).data);
			} catch (NullPointerException e) {
				System.out.println("Null value printed at: " + i + "\tSkipList node address: " + x.find(i));
			}
		}
		
		System.out.println("Size: " + x.sizeOf());
		System.out.println("Level: " + x.getLevel());
		System.out.println("Is Empty: " + x.isEmpty());		
		x.makeEmpty();
		
		long start = System.nanoTime();
		for(int i = 0; i < 100; i++){
			x.insert(i);
			System.out.println("MaxLevel: " + max + "\tLevel: " + x.generateRandomLevel() + "\tInserting: " + i + "\tSize: " + x.sizeOf() + "\t Time(ms): " + (System.nanoTime() - start)/1000000);
		}
		System.out.println("Total Time: " + (System.nanoTime() - start)/1000000 + " milliseconds. \tSize: " + x.sizeOf());
		
	}
}