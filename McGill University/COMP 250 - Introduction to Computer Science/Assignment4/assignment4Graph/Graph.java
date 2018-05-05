package assignment4Graph;

public class Graph {
	
	boolean[][] adjacency;
	int nbNodes;
	
	public Graph (int nb){
		this.nbNodes = nb;
		this.adjacency = new boolean [nb][nb];
		for (int i = 0; i < nb; i++){
			for (int j = 0; j < nb; j++){
				this.adjacency[i][j] = false;
			}
		}
	}
	
	public void addEdge (int i, int j){
		if (i >= this.nbNodes || i < 0 || j >= this.nbNodes || j < 0)
		{
			System.out.println("Please ensure that the inputs are between 0 and " + (this.nbNodes - 1));
			return;
		}
		this.adjacency[i][j] = true;
		this.adjacency[j][i] = true;
	}
	
	public void removeEdge (int i, int j){
		if (i >= this.nbNodes || i < 0 || j >= this.nbNodes || j < 0)
		{
			System.out.println("Please ensure that the inputs are between 0 and " + (this.nbNodes - 1));
			return;
		}
		this.adjacency[i][j] = false;
		this.adjacency[j][i] = false;
	}
	
	public int nbEdges(){
		int count = 0;
		for (int i = 0; i < this.nbNodes; i++)
		{
			for (int j = i; j < this.nbNodes; j++)
			{
				if (this.adjacency[i][j])
				{
					count++;
				}
			}
		}
		return count;
	}
	
	public boolean dFScycle(int start, boolean[] visited, int count, int curr)
	{
		//Implement Depth first search
		//If we ever attempt to re-visit start then return true
		//Else CONTINUE SEARCHING UNTIL ALL NODES VISITED, then return false
		visited[curr] = true;
		count++;
		for (int i = 0; i < this.nbNodes; i++)
		{
			if (this.adjacency[curr][i] && start == i && visited[start] && count >= 3)
			{
				return true;
			}
			else if (this.adjacency[curr][i] && !visited[i])
			{
				boolean temp = dFScycle(start, visited, count, i);
				visited[i] = false;
				if (temp)
				{
					return temp;
				}
			}
		}
		return false;
	}
	
	public boolean cycle(int start){
		//Heap memory to keep track 
		boolean[] visited = new boolean[this.nbNodes];
		int count = 0;
		for (int i = 0; i < this.nbNodes; i++)
		{
			visited[i] = false;
		}
		return dFScycle(start, visited, count, start);
	}
	
	public int dFSshortestPath(int curr, int end, boolean[] visited, int count, int lowest, int start)
	{
		visited[curr] = true;
		count++;
		for (int i = 0; i < this.nbNodes; i++)
		{
			if (this.adjacency[curr][i] && i == end && count >= 3 && i == start)
			{
				if (count < lowest)
				{
					return count;
				}
				return lowest;
			}
			else if (this.adjacency[curr][i] && i == end && !visited[i])
			{
				if (count < lowest)
				{
					return count;
				}
				return lowest;
			}
			else if (this.adjacency[curr][i] && !visited[i])
			{
				lowest = dFSshortestPath(i, end, visited, count, lowest, start);
				visited[i] = false;
			}
		}
		return lowest;
	}
	
	public int shortestPath(int start, int end){
		//Need to traverse the graph all possible ways, and each time we're about to reach the "end" we return the number of nodes
		//visited
		//If start == end then need to check first if there is an edge to itself (if so return 1)
		//If all traversals finish with never having reached the end, then return this.nbNodes+1
		if (start == end && this.adjacency[start][end])
		{
			return 1;
		}
		boolean[] visited = new boolean[this.nbNodes];
		int count = 0;
		int lowest = this.nbNodes + 1;
		for (int i = 0; i < this.nbNodes; i++)
		{
			visited[i] = false;
		}
		return dFSshortestPath(start, end, visited, count, lowest, start);
	}
	
}
