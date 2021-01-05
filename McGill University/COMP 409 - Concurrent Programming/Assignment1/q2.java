package main;

import java.util.concurrent.ThreadLocalRandom;

public class q2 {
	//Root for depth traversal
	public static Node root;
	
	//Head and tail marked volatile so that updating the references happens atomically
	public static volatile Node head;
	public static volatile Node tail;
	
	//For naming purposes
	public static int childrenPairCount = 0;
	
	//For termination purposes
	public static long startTime;
	
	//For printing after 5s has passed
	public static String thread0Output;
	public static int thread2Output;
	
	public static void main(String[] args) {
		
		//Setting up the initial Tree
		root = new Node("root");
		tail = new Node("CHILDREN_PAIR_" + childrenPairCount);
		head = new Node("children_pair_" + childrenPairCount);
		childrenPairCount++;
		//Threading the leaves and creating the tree structure
		root.leftChild = head;
		root.rightChild = tail;
		head.nextLeaf = tail;
		
		//Runnables
		Thread0 r0 = new Thread0();
		Thread1 r1 = new Thread1();
		Thread2 r2 = new Thread2();

		//Threads
		Thread[] threads = new Thread[3];
		threads[0] = new Thread(r0);
		threads[1] = new Thread(r1);
		threads[2] = new Thread(r2);
		
		startTime = System.currentTimeMillis();
		
		//Launch all threads
		for(int i = 0; i < 3; i++) {
			threads[i].start();
		}
		
		//Join all threads
		try {
			for(int i = 0; i < 3; i++) {
				threads[i].join();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//Print out the last two traversals in the desired order
		System.out.println("\n" + thread2Output);
		System.out.println(thread0Output);
	}
}

//Node for trees
class Node {
	public String name;
	public volatile Node leftChild, rightChild;
	public volatile Node nextLeaf;
	
	public Node(String name) {
		this.name = name;
		this.leftChild = null;
		this.rightChild = null;
		this.nextLeaf = null;
	}
}

class Thread0 implements Runnable {
	public void run() {
		//Loop for the desired 5 seconds and only terminate when 5s has passed
		while (true) {
			//Build up a buffer for the prints so that they don't overlap with Thread2's print calls
			String buffer = "";
			Node pointer = q2.head;
			buffer += "*";
			while (pointer != null) {
				buffer += " " + pointer.name;
				pointer = pointer.nextLeaf;
				try {
					Thread.sleep(50);
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//propogate up the string buffer for formatting the final print and then exit thread
			if (System.currentTimeMillis() - q2.startTime > 5000) {
				q2.thread0Output = buffer;
				return;
			}
			else {
				//Sleep for 200ms between each "run"
				System.out.println(buffer);
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class Thread1 implements Runnable {
	public void run() {
		//Same 5 second looping strategy
		while (true) {
			Node pointer = q2.head;
			while (pointer != null) {
				if (oneTenthChance()) expand(pointer);
				try {
					Thread.sleep(20);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				pointer = pointer.nextLeaf;
			}
			if (System.currentTimeMillis() - q2.startTime > 5000) return;
			else {
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//Function returning true 1/10th of the time
	private boolean oneTenthChance() {
		int chance = ThreadLocalRandom.current().nextInt(0, 10);
		if (chance == 0) return true;
		return false;
	}
	
	//This is where the use of volatile allows for this concurrency to function
	private void expand(Node n) {
		//First create the two children locally
		Node leftChild = new Node("children_pair_" + q2.childrenPairCount);
		Node rightChild = new Node("CHILDREN_PAIR_" + q2.childrenPairCount);
		q2.childrenPairCount++;
		
		//Attach the left child to the right, and the right to the expanding node's nextLeaf (ie still just local)
		leftChild.nextLeaf = rightChild;
		rightChild.nextLeaf = n.nextLeaf;
		
		//These conditional blocks are the calls that update the shared variables
		if (q2.head.name.equals(n.name)) {
			q2.head = leftChild;
			n.leftChild = leftChild;
			n.rightChild = rightChild;
		}
		else {
			Node pointer = q2.head;
			while (pointer.nextLeaf != null) {
				if (pointer.nextLeaf.name.equals(n.name)) {
					pointer.nextLeaf = leftChild;
					n.leftChild = leftChild;
					n.rightChild = rightChild;
					
					if (pointer.nextLeaf.name.equals(q2.tail.name)) {
						q2.tail = rightChild;
					}
				}
				pointer = pointer.nextLeaf;
			}
		}
	}
}

class Thread2 implements Runnable {
	public static int count;
	
	public void run() {
		while (true) {
			//Just some simple depth first searching with nothing special added except the sleeping
			count = 0;
			depthFirstSearch(q2.root);
			if (System.currentTimeMillis() - q2.startTime > 5000) {
				q2.thread2Output = count;
				return;
			}
			else {
				System.out.println(count);
				try {
					Thread.sleep(200);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void depthFirstSearch(Node n) {
		count++;
		try {
			Thread.sleep(10);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (n.leftChild != null) {
			depthFirstSearch(n.leftChild);
		}
		if (n.rightChild != null) {
			depthFirstSearch(n.rightChild);
		}
	}
}


















