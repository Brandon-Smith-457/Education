package main2;

import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Map;
import java.lang.Math;

public class q2a implements Runnable {
	//input arguments
	public static int n;
	public static int t;
	public static int k;
	public static int m;
	
	//End condition for web building
	public static boolean end = false;
	
	//End condition for spider jumping
	public static long startTime = -1;
	
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
		//Local variables for failure and spider jump counting
		int failCount = 0;
		int jumpCount = 0;
		
		//Local end boolean so that the spider jumping portion of run does not require synchronized (this) {} wrapping
		boolean localEnd = false;
		while (true) {
			//If the end flag has been set then trigger spider jumping mode
			synchronized (this) {
				localEnd = end;
			}
			
			//Spider Jumping
			if (localEnd) {
				//Array so we can pass as reference
				Spider localSpider[] = new Spider[1];
				while (true) {
					//localSpider updated with potential landing position, TreeMap of involved nodes returned so we can lock them in order to avoid deadlock.
					TreeMap<Integer, Node> locks = getLandingPosition(localSpider);
					Node nodes[] = new Node[4];
					{
						int i = 0;
						for (Map.Entry<Integer, Node> entry : locks.entrySet()) {
							nodes[i] = entry.getValue();
							i++;
						}
					}
					
					//Locking in the desired order
					synchronized (nodes[0]) {
						synchronized (nodes[1]) {
							synchronized (nodes[2]) {
								synchronized (nodes[3]) {
									//If none of the nodes are occupied we land
									if (	nodes[0].hasBody || nodes[0].hasLeg ||
											nodes[1].hasBody || nodes[1].hasLeg ||
											nodes[2].hasBody || nodes[2].hasLeg ||
											nodes[3].hasBody || nodes[3].hasLeg)
										continue;
									localSpider[0].land();
									break;
								}
							}
						}
					}
				}
				//Now that we have landed at a location we must do the same thing, however this time we also want to acquire the locks of the nodes the spider is jumping from
				while (true) {
					if (q2a.startTime == -1)
						q2a.startTime = System.currentTimeMillis();
					
					//Reset spider because the landingPosition function modifies the localSpider
					Spider resetSpider = localSpider[0];
					
					//Acquire the starting position
					TreeMap<Integer, Node> locks0 = new TreeMap<Integer, Node>(localSpider[0].legs);
					locks0.put(localSpider[0].bodyIndex, localSpider[0].body);
					
					//Acquire the landing position
					TreeMap<Integer, Node> locks1 = getLandingPosition(localSpider);
					Node landingNodes[] = new Node[4];
					{
						int i = 0;
						for (Map.Entry<Integer, Node> entry : locks1.entrySet()) {
							locks0.put(entry.getKey(), entry.getValue());
							landingNodes[i] = entry.getValue();
							i++;
						}
					}
					
					
					//Populate the array for locking purposes
					Node nodes[] = new Node[8];
					{
						int i = 0;
						for (Map.Entry<Integer, Node> entry : locks0.entrySet()) {
							nodes[i] = entry.getValue();
							i++;
						}
					}
					
					try {
						//Acquire the locks in the desired order
						synchronized (nodes[0]) {
							synchronized (nodes[1]) {
								synchronized (nodes[2]) {
									synchronized (nodes[3]) {
										synchronized (nodes[4]) {
											synchronized (nodes[5]) {
												synchronized (nodes[6]) {
													synchronized (nodes[7]) {
														//Check if the landing position is occupied, if not then jump from current position and land at landing position.
														if (	landingNodes[0].hasBody || landingNodes[0].hasLeg ||
																landingNodes[1].hasBody || landingNodes[1].hasLeg ||
																landingNodes[2].hasBody || landingNodes[2].hasLeg ||
																landingNodes[3].hasBody || landingNodes[3].hasLeg) {
															//Otherwise we want to reset the local spider
															localSpider[0] = resetSpider;
														}
														else {
															localSpider[0].jump(resetSpider);
															localSpider[0].land();
															jumpCount++;
														}
													}
												}
											}
										}
									}
								}
							}
						}
					} catch (NullPointerException e) {/*I'm not sure why I'm having Null Pointer Exceptions on line 102 as the length value is indeed 8*/}
					
					//Check if the desired amount of time has passed, and if so then print the spider's jump count
					if (System.currentTimeMillis() - q2a.startTime >= m*1000) {
						System.out.println("Spider" + Thread.currentThread().getId() + ": " + jumpCount);
						return;
					}
					//Otherwise sleep for 50 milliseconds and then try to jump again.
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			//Same as q1a, building the web from this point down in the run() function
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
			
			if (n0.addIfNotNeighbors(n1)) {
				synchronized (this) {
					q2a.edgeCount++;
				}
			} else {
				synchronized (this) {
					failCount++;
					end = failCount >= k;
				}
			}
		}
	}
	
	/* This function works on the idea that we acquire the lock of a random node, copy it's adjacency list into a local variable, and then release the lock.
	 * Then we randomly select 3 nodes from the list and use those as the legs of our spider instantiation.  Finally we then add the body to the TreeMap and return that
	 * so that the return value is in order of indices and thus the deadlock problem is again solved by ensuring an ordering. */
	private TreeMap<Integer, Node> getLandingPosition(Spider localSpider[]) {
		//Acquire the lock of a random node and then copy it's neighbors list
		TreeMap<Integer, Node> localNeighbors;
		Integer bodyIndex = ThreadLocalRandom.current().nextInt(0, n);
		Node body = webNodes.get(bodyIndex);
		synchronized (body) {
			localNeighbors = new TreeMap<Integer, Node>(body.getNeighbors());
		}
		
		//Pick 3 random nodes from the list
		ArrayList<Integer> keys = new ArrayList<Integer>(localNeighbors.keySet());
		Integer randomKeys[] = new Integer[3];
		for (int i = 0; i < 3; i++) {
			int randomIndex = ThreadLocalRandom.current().nextInt(0, keys.size());
			randomKeys[i] = keys.get(randomIndex);
			keys.remove(randomIndex);
		}
		TreeMap<Integer, Node> locks = new TreeMap<Integer, Node>();
		for (int i = 0; i < 3; i++) {
			locks.put(randomKeys[i], localNeighbors.get(randomKeys[i]));
		}
		
		//Construct the spider with the three legs
		localSpider[0] = new Spider(Thread.currentThread().getId(), bodyIndex, body, locks);
		
		//Insert the body into the TreeMap and return
		locks.put(bodyIndex, body);
		return locks;
	}
	
	public static void main(String[] args) {
		try {
			if (args.length != 4) {
        		System.out.println("Please provide the arguments as integer values: num_nodes num_threads failure_tolerance spider_jump_seconds");
        		return;
			}
			
			q2a.n =	Integer.parseInt(args[0]);
        	q2a.t = Integer.parseInt(args[1]);
        	q2a.k =	Integer.parseInt(args[2]);
        	q2a.m = Integer.parseInt(args[3]);
        	
        	webNodes = new ArrayList<Node>();
        	
        	//Include the four corners
        	webNodes.add(new Node(0, 0.0f, 0.0f));
        	webNodes.add(new Node(1, 1000.0f, 0.0f));
        	webNodes.add(new Node(2, 1000.0f, 1000.0f));
        	webNodes.add(new Node(3, 0.0f, 1000.0f));
        	
        	//Random point selection
        	int index = 4;
        	Random r = new Random();
        	outer: while (webNodes.size() < n) {
            	float x = q2a.minX + r.nextFloat() * (q2a.maxX - q2a.minX);
            	float y = q2a.minY + r.nextFloat() * (q2a.maxY - q2a.minY);
            	for(Node node : webNodes) {
            		if(node.tooClose(x, y, q2a.minDistBetweenPoints))
            			continue outer;
            	}
            	webNodes.add(new Node(index, x, y));
            	index++;
        	}
        	        	        	
            //Setup all n threads
            q2a runnable = new q2a();
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
            
            System.out.println("Number of successfully created edges: " + q2a.edgeCount);
		}
		catch (NumberFormatException e) {
    		System.out.println("Please provide the arguments as integer values: num_nodes num_threads failure_tolerance spider_jump_seconds");
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
	//index to keep track of the nodes more clearly (helps with the ordering)
	public Integer index;
	public float x;
	public float y;
	TreeMap<Integer, Node> neighbors;
	
	//Spider information
	public boolean hasBody = false;
	public boolean hasLeg = false;
	public long spiderID = -1;
	
	Node(Integer index, float x, float y) {
		this.index = index;
		this.x = x;
		this.y = y;
		neighbors = new TreeMap<Integer, Node>();
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
		neighbors.put(n.index, n);
	}
	
	private boolean isNeighbors(Node n) {
		return neighbors.containsKey(n.index) && neighbors.containsValue(n);
	}
	
	public TreeMap<Integer, Node> getNeighbors() {
		return neighbors;
	}
}

class Spider {
	public long id;
	public int bodyIndex;
	public Node body;
	public TreeMap<Integer, Node> legs;
	
	public Spider(long id, int bodyIndex, Node body, TreeMap<Integer, Node> legs) {
		this.id = id;
		this.bodyIndex = bodyIndex;
		this.body = body;
		this.legs = new TreeMap<Integer, Node>(legs);
	}
	
	public void land() {
		body.spiderID = id;
		body.hasBody = true;
		for (Map.Entry<Integer, Node> entry : legs.entrySet()) {
			entry.getValue().spiderID = id;
			entry.getValue().hasLeg = true;
		}
	}
	
	public void jump(Spider start) {
		start.body.spiderID = -1;
		start.body.hasBody = false;
		for (Map.Entry<Integer, Node> entry : start.legs.entrySet()) {
			entry.getValue().spiderID = -1;
			entry.getValue().hasLeg = false;
		}
	}
	
	public void printIndices() {
		String ss = "Body: " + bodyIndex + "\n";
		for (Map.Entry<Integer, Node> entry : legs.entrySet()) {
			ss += "Leg: " + entry.getKey() + "\n";
		}
		System.out.println(ss);
	}
}
