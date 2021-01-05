using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MazeGenerator : MonoBehaviour
{
    public Vector3 initialTilePosition;
    public float wallYOffset = 7f;
    public float wallxyZOffset = 10f;
    public float wallyzXOffset = 10f;

    public int mazeXDimension = 5;
    public int mazeZDimension = 5;

    public GameObject mazeTilePrefab;
    public GameObject wallxyPrefab;
    public GameObject wallyzPrefab;

    private GameObject[,] mazeTiles;
    // Start is called before the first frame update
    void Start()
    {
        initialTilePosition = transform.position;
        mazeTiles = new GameObject[mazeXDimension, mazeZDimension];
        for (int x = 0; x < mazeXDimension; x++)
        {
            for (int z = 0; z < mazeZDimension; z++)
            {
                mazeTiles[x, z] = Instantiate(mazeTilePrefab, new Vector3(initialTilePosition.x - wallyzXOffset * 2 * x, initialTilePosition.y, initialTilePosition.z + wallxyZOffset * 2 * z), Quaternion.identity, transform);
            }
        }

        MazeNode[,] mazeNodes = generateMaze(mazeXDimension, mazeZDimension, 0, mazeZDimension / 2, mazeXDimension, mazeZDimension / 2);
        
        for (int x = 0; x < mazeXDimension; x++)
        {
            for (int z = 0; z < mazeZDimension; z++)
            {
                Vector3 tilePosition = mazeTiles[x, z].transform.position;
                if (x == 0)
                    Instantiate(wallxyPrefab, new Vector3(tilePosition.x, tilePosition.y + wallYOffset, tilePosition.z - wallxyZOffset), Quaternion.identity, transform);
                else if (mazeNodes[x, z].left == null && mazeNodes[x - 1, z].right ==null)
                    Instantiate(wallxyPrefab, new Vector3(tilePosition.x, tilePosition.y + wallYOffset, tilePosition.z - wallxyZOffset), Quaternion.identity, transform);
                //Conditional check for if there is an edge between this tile and the bottom neighbour
                if (z == 0)
                {
                    if (!(x == 0 && z == mazeZDimension / 2))
                    {
                        Instantiate(wallyzPrefab, new Vector3(tilePosition.x + wallyzXOffset, tilePosition.y + wallYOffset, tilePosition.z), Quaternion.identity, transform);
                    }
                }
                else if (mazeNodes[x, z].down == null && mazeNodes[x, z - 1].up == null)
                {
                    Instantiate(wallyzPrefab, new Vector3(tilePosition.x + wallyzXOffset, tilePosition.y + wallYOffset, tilePosition.z), Quaternion.identity, transform);
                }
                if (x == mazeXDimension - 1 && !(z == mazeZDimension/2))
                {
                    Instantiate(wallyzPrefab, new Vector3(tilePosition.x - wallyzXOffset, tilePosition.y + wallYOffset, tilePosition.z), Quaternion.identity, transform);
                }
                if (z == mazeZDimension - 1)
                {
                    Instantiate(wallxyPrefab, new Vector3(tilePosition.x, tilePosition.y + wallYOffset, tilePosition.z + wallxyZOffset), Quaternion.identity, transform);
                }
            }
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public MazeNode[,] generateMaze(int xSize, int zSize, int xStart, int zStart, int xEnd, int zEnd)
    {
        //Initialize all the nodes
        MazeNode[,] mazeNodes = new MazeNode[xSize, zSize];
        for (int x = 0; x < xSize; x++)
        {
            for (int z = 0; z < zSize; z++)
            {
                mazeNodes[x, z] = new MazeNode(x, z);
            }
        }

        generateMazeRecursion(mazeNodes, xEnd, zEnd, mazeNodes[xStart, zStart], xSize, zSize);
        return mazeNodes;
    }

    private void generateMazeRecursion(MazeNode[,] mazeNodes, int xEnd, int zEnd, MazeNode current, int xSize, int zSize)
    {
        List<MazeNode> unvisitedNeighbors = new List<MazeNode>();
        int x = current.x;
        int z = current.z;
        if (x != xSize - 1)
            if (!mazeNodes[x + 1, z].visited)
                unvisitedNeighbors.Add(mazeNodes[x + 1, z]);
        if (x != 0)
            if (!mazeNodes[x - 1, z].visited)
                unvisitedNeighbors.Add(mazeNodes[x - 1, z]);
        if (z != zSize - 1)
            if (!mazeNodes[x, z + 1].visited)
                unvisitedNeighbors.Add(mazeNodes[x, z + 1]);
        if (z != 0)
            if (!mazeNodes[x, z - 1].visited)
                unvisitedNeighbors.Add(mazeNodes[x, z - 1]);
        while (unvisitedNeighbors.Count > 0)
        {
            int randomIndex = Random.Range(0, unvisitedNeighbors.Count - 1);
            if (unvisitedNeighbors[randomIndex].x > current.x)
                current.up = unvisitedNeighbors[randomIndex];
            if (unvisitedNeighbors[randomIndex].x < current.x)
                current.down = unvisitedNeighbors[randomIndex];
            if (unvisitedNeighbors[randomIndex].z > current.z)
                current.right = unvisitedNeighbors[randomIndex];
            if (unvisitedNeighbors[randomIndex].z < current.z)
                current.left = unvisitedNeighbors[randomIndex];
            unvisitedNeighbors[randomIndex].visited = true;
            generateMazeRecursion(mazeNodes, xEnd, zEnd, unvisitedNeighbors[randomIndex], xSize, zSize);
            unvisitedNeighbors.RemoveAt(randomIndex);
        }
    }
}

public class MazeNode
{
    public int x;
    public int z;
    public MazeNode up = null; //x++
    public MazeNode down = null; //x--
    public MazeNode right = null; //z++
    public MazeNode left = null; //z--
    public bool visited = false;

    public MazeNode(int x, int z)
    {
        this.x = x;
        this.z = z;
    }
}
