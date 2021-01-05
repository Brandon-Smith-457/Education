package main;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.lang.Math;

public class q1a implements Runnable {
	
	//Input Arguments
	public static int n;
	public static int t;
	public static int k;
	
	//Flag for when to terminate threads
	public static boolean end = false;
	
	//Fields to change the coordinate region
	public static float minX = 0.0f;
	public static float minY = 0.0f;
	public static float maxX = 1000.0f;
	public static float maxY = 1000.0f;
	
	//Minimum distance required between two points
	public static float minDistBetweenPoints = 0.0001f;
	
	//ArrayList to store the nodes
	public static ArrayList<Node> webNodes;
	public static int edgeCount = 0;
		
	public void run() {
		//Local variable for failure count on the current thread
		int failCount = 0;
		while (true) {
			//If the end flag has been set then terminate
			synchronized(this) {
				if (end) return;
			}
			
			//Acquire two unique random indices
			int index0 = ThreadLocalRandom.current().nextInt(0, n);
			int index1;
			do {
				index1 = ThreadLocalRandom.current().nextInt(0, n);
			} while (index1 == index0);
			
			//Ensuring that the outer synchronized block will always be a lower index than the inner so it's not possible to have any cyclic deadlock occur
			if (index1 < index0) {
				int temp = index0;
				index0 = index1;
				index1 = temp;
			}
			Node n0 = webNodes.get(index0);
			Node n1 = webNodes.get(index1);
			
			//n0 acquires the lock first as addIfNotNeighbors is a synchronized method, and inside the method n1 then acquires the lock
			if (n0.addIfNotNeighbors(n1)) {
				synchronized (this) {
					q1a.edgeCount++;
				}
			} else {
				synchronized (this) {
					failCount++;
					end = failCount >= k;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			if (args.length != 3) {
        		System.out.println("Please provide the arguments as integer values: num_nodes num_threads failure_tolerance");
        		return;
			}
			
			q1a.n =	Integer.parseInt(args[0]);
        	q1a.t = Integer.parseInt(args[1]);
        	q1a.k =	Integer.parseInt(args[2]);
        	
        	webNodes = new ArrayList<Node>();
        	
        	//Include the four corners
        	webNodes.add(new Node(0.0f, 0.0f));
        	webNodes.add(new Node(1000.0f, 0.0f));
        	webNodes.add(new Node(1000.0f, 1000.0f));
        	webNodes.add(new Node(0.0f, 1000.0f));
        	
        	//Random point selection
        	Random r = new Random();
        	outer: while (webNodes.size() < n) {
            	float x = q1a.minX + r.nextFloat() * (q1a.maxX - q1a.minX);
            	float y = q1a.minY + r.nextFloat() * (q1a.maxY - q1a.minY);
            	for(Node node : webNodes) {
            		if(node.tooClose(x, y, q1a.minDistBetweenPoints))
            			continue outer;
            	}
            	webNodes.add(new Node(x, y));
        	}
        	        	        	
            //Setup all n threads
            q1a runnable = new q1a();
            Thread threads[] = new Thread[t];
            for (int i = 0; i < t; i++) {
            	threads[i] = new Thread(runnable);
            }
            
            //Start all n threads
            for (int i = 0; i < t; i++) {
            	threads[i].start();
            }
            
            //Join all n threads
            for (int i = 0; i < t; i++) {
            	threads[i].join();
            }
            
            System.out.println("Number of successfully created edges: " + q1a.edgeCount);
		}
		catch (NumberFormatException e) {
    		System.out.println("Please provide the arguments as integer values: num_nodes num_threads failure_tolerance");
        	System.out.println("Error " + e);
        	e.printStackTrace();
		}
		catch (Exception e) {
        	System.out.println("Error " + e);
			e.printStackTrace();
		}
	}
}

class Node {
	public float x;
	public float y;
	ArrayList<Node> neighbors;
	
	Node(float x, float y) {
		this.x = x;
		this.y = y;
		neighbors = new ArrayList<Node>();
	}
	
	public boolean tooClose(float x, float y, float minDist) {
		return Math.sqrt((this.x - x)*(this.x - x) + (this.y - y)*(this.y - y)) < minDist;
	}
	
	//So long as "this" is a lower index than "n" no deadlock will occur
	synchronized public boolean addIfNotNeighbors(Node n) {
		synchronized (n) {
			if (!this.isNeighbors(n) && !n.isNeighbors(this)) {
				this.addNeighbor(n);
				n.addNeighbor(this);
				return true;
			} else return false;
		}
	}
		
	private void addNeighbor(Node n) {
		neighbors.add(n);
	}
	
	private boolean isNeighbors(Node n) {
		return neighbors.contains(n);
	}
}
