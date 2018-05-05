package assignment2;

public class Tester {

	public static void main(String[] args) {

		int[] lengths = new int[] {4, 3, 1,5, 6};
		Warehouse warehouse = new Warehouse(5,lengths, new int[] {15,15,15,15,15});
		
		//TESTING SORT
		warehouse.sort();
		String testSort = "0-th shelf ( 1 - 15 ) : \n1-th shelf ( 3 - 15 ) : \n2-th shelf ( 4 - 15 ) : \n3-th shelf ( 5 - 15 ) : \n4-th shelf ( 6 - 15 ) : \n";
		if (warehouse.print().equals(testSort)){
			System.out.println("Merge Sort seems to work");
		}
		else{
			System.out.println("Merge Sort does not work");
		}
		
		Box b1 = new Box(2, 4, "Box 1");
		Box b2 = new Box(4, 10, "Box 2");
		Box b3 = new Box(2, 10, "Box 3");
		Box b4 = new UrgentBox(2, 10, "Box 4");
		
		// TESTING ADDBOX
		String testAddShelf = "0-th shelf ( 1 - 15 ) : \n1-th shelf ( 3 - 15 ) : \n2-th shelf ( 4 - 15 ) : \n3-th shelf ( 5 - 15 ) : \n4-th shelf ( 6 - 5 ) : Box 2, \n";
		warehouse.storage[4].addBox(b2);
		if (warehouse.print().equals(testAddShelf)){
			System.out.println("Adding a box on a shelf seems to work");
		}
		else{
			System.out.println("Adding a box on a shelf does not work");
		}
		
		String testAddW = "0-th shelf ( 1 - 15 ) : \n1-th shelf ( 3 - 1 ) : Box 1, Box 3, \n2-th shelf ( 4 - 15 ) : \n3-th shelf ( 5 - 15 ) : \n4-th shelf ( 6 - 5 ) : Box 2, \n";
		warehouse.addBox(b1);
		warehouse.addBox(b3);
		if (warehouse.print().equals(testAddW) & b3.previous == b1){
			System.out.println("Adding a box in the warehouse seems to work");
		}
		else{
			System.out.println("Adding a box in the Warehouse does not work");
		}
		
		// TESTING REMOVEBOX
		String testremoveBox = "0-th shelf ( 1 - 15 ) : \n1-th shelf ( 3 - 5 ) : Box 3, \n2-th shelf ( 4 - 15 ) : \n3-th shelf ( 5 - 15 ) : \n4-th shelf ( 6 - 5 ) : Box 2, \n";
		warehouse.storage[1].removeBox("Box 1");
		warehouse.storage[2].removeBox("Box 2");
		if (warehouse.print().equals(testremoveBox) && b3.previous == null && b3.next == null && b1.previous == null && b1.next == null && b2.previous == null && b2.next== null){
			System.out.println("Removing a box from a shelf seems to work");
		}
		else{
			System.out.println("Removing a box from a shelf does not work");
		}
		
		warehouse.clear();
		
		// TESTING SHIPBOX
		warehouse.addBox(b1);
		warehouse.addBox(b2);
		warehouse.addBox(b3);
		warehouse.addBox(b4);
		boolean testShip2 = warehouse.shipBox("Box 2") == Warehouse.noProblem;
		boolean testShip4 = warehouse.shipBox("Box 4") == Warehouse.noProblem;
		boolean testShip3 = warehouse.shipBox("Box 3") == Warehouse.noProblem;
		boolean testShip5 = warehouse.shipBox("Box 5") == Warehouse.problem;
		String testShip = "0-th shelf ( 1 - 15 ) : \n1-th shelf ( 3 - 11 ) : Box 1, \n2-th shelf ( 4 - 15 ) : \n3-th shelf ( 5 - 15 ) : \n4-th shelf ( 6 - 15 ) : \n";
		String testShipping = "not urgent : Box 3, Box 2, \nshould be already gone : Box 4, \n";
		if (warehouse.print().equals(testShip) && warehouse.printShipping().equals(testShipping)){
			if (testShip2 & testShip4 & testShip3 & testShip5){
				System.out.println("Shipping a box seems correct");
			}
			else{
				System.out.println("Use the strings of the class warehouse for shipping a box");
			}
		}
		else{
			System.out.println("Shipping a box does not work");
		}
		
		warehouse.clear();
		
		//TESTING MOVE ONE BOX
		warehouse.storage[4].addBox(b1);
		warehouse.addBox(b3);
		warehouse.moveOneBox(b1, 4);
		String testMove = "0-th shelf ( 1 - 15 ) : \n1-th shelf ( 3 - 1 ) : Box 3, Box 1, \n2-th shelf ( 4 - 15 ) : \n3-th shelf ( 5 - 15 ) : \n4-th shelf ( 6 - 15 ) : \n";
		if (warehouse.print().equals(testMove)){
			System.out.println("Moving a box from a shelf seems to work");
		}
		else{
			System.out.println("Moving a box from a shelf does not work");
		}
		
		warehouse.clear();
		
		//TESTING REORGANIZE
		
		/*
		 * Box b1 = new Box(2, 4, "Box 1");
		Box b2 = new Box(4, 10, "Box 2");
		Box b3 = new Box(2, 10, "Box 3");
		Box b4 = new UrgentBox(2, 10, "Box 4");
		 */
		warehouse.storage[4].addBox(b1);
		warehouse.storage[2].addBox(b2);
		warehouse.storage[3].addBox(b3);
		warehouse.storage[1].addBox(b4);
		Box b5 = new Box (1, 1, "Box 5");
		Box b6 = new Box (4, 2, "Box 6");
		Box b7 = new Box (5, 4, "Box 7");
		warehouse.storage[4].addBox(b7);
		warehouse.storage[4].addBox(b6);
		warehouse.storage[3].addBox(b5);
		//System.out.println(warehouse.print());
		/*System.out.println(b1.previous);
		System.out.println(b1.next.id);
		System.out.println(b7.previous.id);
		System.out.println(b7.next.id);
		System.out.println(b6.previous.id);
		System.out.println(b6.next);*/
		warehouse.reorganize();
		//System.out.println(warehouse.print());
		String testReorganize = "0-th shelf ( 1 - 14 ) : Box 5, \n1-th shelf ( 3 - 1 ) : Box 4, Box 1, \n2-th shelf ( 4 - 3 ) : Box 2, Box 6, \n3-th shelf ( 5 - 1 ) : Box 3, Box 7, \n4-th shelf ( 6 - 15 ) : \n";
		//System.out.println(testReorganize);
		if (warehouse.print().equals(testReorganize)){
			System.out.println("Reorganizing the warehouse seems to work, congrats you are done :)");
		}
		else{
			System.out.println("Reorganizing the warehouse does not work");
		}
		//
		//
		//
		//
		//
		//
		//
		//
		// PERSONALIZED TESTS!
		int[] lengths2 = new int[] {4, 3, 1,5, 6, 539, 1000};
		/*int[] lengths2 = new int[2000];
		int[] actuallyLengths2 = new int[2000];
		int count = 1;
		for (int i = 1999; i >= 0; i--)
		{
			lengths2[i] = count;
			count++;
			actuallyLengths2[i] = count;
		}*/
		Warehouse warehouse2 = new Warehouse(7/*2000*/,lengths2, new int[] {1,15,2341235,15,15,999,236234}/*actuallyLengths2*/);
		
		// TESTING SORT
		System.out.println(warehouse2.print());
		warehouse2.sort();
		System.out.println(warehouse2.print());
		
		// TESTING ADDBOX SHELF
		Box b1_2 = new Box(1, 4, "Box 1");
		Box b2_2 = new Box(2, 10, "Box 2");
		Box b3_2 = new Box(1001, 10, "Box 3");
		Box b4_2 = new UrgentBox(540, 10, "Box 4");
		Box b5_2 = new Box (538, 500, "Box 5");
		Box b6_2 = new Box (539, 499, "Box 6");
		Box b7_2 = new UrgentBox (1000, 4, "Box 7");
		
		warehouse2.storage[4].addBox(b1_2);
		warehouse2.storage[4].addBox(b2_2);
		warehouse2.storage[4].addBox(b3_2);
		warehouse2.storage[4].addBox(b4_2);
		warehouse2.storage[4].addBox(b5_2);
		warehouse2.storage[4].addBox(b6_2);
		warehouse2.storage[4].addBox(b7_2);
		System.out.println(warehouse2.print());
		System.out.println(b1_2.previous);
		System.out.println(b1_2.next.id);
		System.out.println(b2_2.previous.id);
		System.out.println(b2_2.next.id);
		System.out.println(b3_2.previous.id);
		System.out.println(b3_2.next.id);
		System.out.println(b7_2.previous.id);
		System.out.println(b7_2.next);
		System.out.println('\n');
		
		// TESTING REMOVEBOX SHELF
		warehouse2.storage[4].removeBox("Box 1");
		warehouse2.storage[4].removeBox("Box 2");
		warehouse2.storage[4].removeBox("Box 3");
		warehouse2.storage[4].removeBox("Box 4");
		warehouse2.storage[4].removeBox("Box 5");
		warehouse2.storage[4].removeBox("Box 6");
		warehouse2.storage[4].removeBox("Box 7");
		warehouse2.storage[4].removeBox("Box 0");
		System.out.println(warehouse2.print());
		System.out.println(b1_2.previous);
		System.out.println(b1_2.next);
		System.out.println(b2_2.previous);
		System.out.println(b2_2.next);
		System.out.println(b3_2.previous);
		System.out.println(b3_2.next);
		System.out.println(b7_2.previous);
		System.out.println(b7_2.next);
		System.out.println('\n');

		// TESTING ADDBOX WAREHOUSE
		warehouse2.addBox(b1_2);
		warehouse2.addBox(b2_2);
		warehouse2.addBox(b3_2);
		warehouse2.addBox(b4_2);
		warehouse2.addBox(b5_2);
		warehouse2.addBox(b6_2);
		warehouse2.addBox(b7_2);
		System.out.println(warehouse2.print());
						
		// TESTING SHIPBOX
		System.out.println(warehouse2.printShipping());
		String s1 = warehouse2.shipBox(b1_2.id);
		String s2 = warehouse2.shipBox(b2_2.id);
		String s3 = warehouse2.shipBox(b3_2.id);
		String s4 = warehouse2.shipBox(b4_2.id);
		String s5 = warehouse2.shipBox(b5_2.id);
		String s6 = warehouse2.shipBox(b6_2.id);
		String s7 = warehouse2.shipBox(b7_2.id);
		System.out.println(s1+s2+s3+s4+s5+s6+s7);
		System.out.println(warehouse2.printShipping());
		System.out.println(warehouse2.print());
				
		//TESTING MOVE ONE BOX
		warehouse2.storage[4].addBox(b1_2);
		warehouse2.addBox(b2_2);
		warehouse2.addBox(b3_2);
		warehouse2.addBox(b4_2);
		warehouse2.addBox(b5_2);
		warehouse2.addBox(b6_2);
		warehouse2.addBox(b7_2);
		System.out.println(warehouse2.print());
		warehouse2.moveOneBox(b1_2, 4);
		System.out.println(warehouse2.print());
				
		//TESTING REORGANIZE
		warehouse2.clear();
		warehouse2.storage[2].addBox(b1_2);
		warehouse2.storage[4].addBox(b2_2);
		warehouse2.addBox(b3_2);
		warehouse2.addBox(b4_2);
		warehouse2.storage[6].addBox(b5_2);
		warehouse2.addBox(b6_2);
		warehouse2.addBox(b7_2);
		System.out.println(warehouse2.print());
		warehouse2.reorganize();
		System.out.println(warehouse2.print());
	}

}
