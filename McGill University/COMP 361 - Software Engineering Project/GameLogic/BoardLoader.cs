using System.IO;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Enums;

public sealed class BoardLoader {
	private static BoardLoader instance = null;
    //honestly don't know what this is for, just following MSDN
    private static readonly object padlock = new object();

    const int cellsHigh = 8;
	const int cellsWide = 10;

	GameObject cellPrefab;
	GameObject emptyPrefab;
    GameObject wallPrefab;
	GameObject doorPrefab;
    GameObject gameObjectsContainer;

    GameObject cellLayer;
    GameObject edgeLayer;
    GameObject gameObjectsLayer;

    readonly int[,] horizEdges =
    {
        {0, 0, 0, 0, 0, 0, 0},
        {1, 0, 0, 0, 1, 0, 1},
        {1, 0, 0, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 2},
        {1, 0, 1, 0, 2, 0, 1},
        {1, 0, 1, 0, 1, 0, 1},
        {2, 0, 1, 0, 1, 0, 1},
        {1, 0, 1, 0, 1, 0, 1},
        {1, 0, 2, 0, 1, 0, 1},
        {0, 0, 0, 0, 0, 0, 0}
    };
    readonly int[,] vertEdges =
    {
        {0, 1, 1, 1, 1, 1, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 2, 1, 0, 0, 0},
        {0, 2, 1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 1, 1, 0, 0, 1, 2, 0},
        {0, 0, 0, 1, 2, 0, 0, 0},
        {0, 0, 0, 0, 0, 1, 2, 0},
        {0, 1, 1, 1, 2, 1, 1, 0}
    };


    private BoardLoader() {
		cellPrefab = Resources.Load("Prefabs/" + "Cell") as GameObject;
		emptyPrefab = Resources.Load("Prefabs/" + "Empty") as GameObject;
        wallPrefab = Resources.Load("Prefabs/" + "Wall") as GameObject;
        doorPrefab = Resources.Load("Prefabs/" + "Door") as GameObject;
        gameObjectsContainer = Resources.Load("Prefabs/" + "GameObjectsContainer") as GameObject;

        cellLayer = GameObject.FindGameObjectWithTag("CellLayer");
        edgeLayer = GameObject.FindGameObjectWithTag("EdgeLayer");
        gameObjectsLayer = GameObject.FindGameObjectWithTag("GameObjectsLayer");
    }

	public static BoardLoader Instance {
		get {
			if (instance == null) {
				lock (padlock) {
					if (instance == null) {
						instance = new BoardLoader();
					}
				}
			}
			return instance;
		}
	}

	public Dictionary<Vector2, Cell> InitBoard1 () {
        int layerWidth = (int)edgeLayer.GetComponent<RectTransform>().rect.width;
        int layerHeight = (int)edgeLayer.GetComponent<RectTransform>().rect.height;
        int edgeWidth = (int)wallPrefab.GetComponent<RectTransform>().rect.width;

        Vector2 cellDim = new Vector2(layerWidth / 10, layerHeight / 8);

        //offset for placing edges
        //these are hardcoded as it is easier to change the anchor of the edges 
        //and move them around in the scene view to see what the offset should 
        //be, rather than try to calculate it.
        Vector2 firstHorizEdge = new Vector2(0 - (edgeWidth/2), 840 + (edgeWidth/2));
        Vector2 firstVertEdge = new Vector2(160 - (edgeWidth/2), 840 - (edgeWidth/2));

        Dictionary<Vector2, Cell> cells = new Dictionary<Vector2, Cell>();

        InitCells(cells);
		
        //the rest of this is for initializing edges
        for (int i = 0; i < cellsWide; i++) {
            for (int j = 0; j < cellsHigh; j++) { 
                Vector2 index = new Vector2(i, j);
                Cell cell1 = cells[index];

                if (j != cellsHigh - 1) {
                    Cell cell2 = cells[index + Vector2.up];
                    GameObject horizEdge = MakeEdge(horizEdges[i, j], false, cellDim);

                    Edge edgeComponent = GetEdgeComponent(horizEdge, horizEdges[i, j]);

                    cell1.SetAdjacent(Direction.DOWN, cell2, edgeComponent);
                    cell2.SetAdjacent(Direction.UP, cell1, edgeComponent);

                    horizEdge.transform.SetParent(edgeLayer.transform, false);
                    Vector2 position = new Vector2(cellDim.x * i, cellDim.y * j * -1)
                                     + firstHorizEdge;
                    horizEdge.GetComponent<RectTransform>().anchoredPosition = position;
                    horizEdge.name = horizEdge.name + i.ToString() + ", " + j.ToString(); 
                }

                if (i != cellsWide - 1)
                {
                    Cell cell3 = cells[index + Vector2.right];
                    GameObject vertEdge = MakeEdge(vertEdges[i, j], true, cellDim);

                    Edge edgeComponent = GetEdgeComponent(vertEdge, vertEdges[i, j]);

                    cell1.SetAdjacent(Direction.RIGHT, cell3, edgeComponent);
                    cell3.SetAdjacent(Direction.LEFT, cell1, edgeComponent);

                    vertEdge.transform.SetParent(edgeLayer.transform, false);
                    Vector2 position = new Vector2(cellDim.x * i, cellDim.y * j * -1)
                                     + firstVertEdge;
                    vertEdge.GetComponent<RectTransform>().anchoredPosition = position;
                }
            }
        }
		return cells;
	}

    void InitCells(Dictionary<Vector2, Cell> cells)
    {
        ArrayList fireEngineSpots = new ArrayList();
        fireEngineSpots.Add(new Vector2(0, 1));
        fireEngineSpots.Add(new Vector2(0, 2));
        fireEngineSpots.Add(new Vector2(7, 0));
        fireEngineSpots.Add(new Vector2(8, 0));
        fireEngineSpots.Add(new Vector2(1, 7));
        fireEngineSpots.Add(new Vector2(2, 7));
        fireEngineSpots.Add(new Vector2(9, 5));
        fireEngineSpots.Add(new Vector2(9, 6));
        ArrayList ambulanceSpots = new ArrayList();
        ambulanceSpots.Add(new Vector2(0, 3));
        ambulanceSpots.Add(new Vector2(0, 4));
        ambulanceSpots.Add(new Vector2(5, 0));
        ambulanceSpots.Add(new Vector2(6, 0));
        ambulanceSpots.Add(new Vector2(3, 7));
        ambulanceSpots.Add(new Vector2(4, 7));
        ambulanceSpots.Add(new Vector2(9, 3));
        ambulanceSpots.Add(new Vector2(9, 4));

        for (int i = 0; i < cellsWide; i++)
        {
            for (int j = 0; j < cellsHigh; j++)
            {
                Vector2 coords = new Vector2(i, j);
                GameObject newCell = Object.Instantiate(cellPrefab);
                GameObject newGameObjectContainer = Object.Instantiate(gameObjectsContainer);

                CellKind kind = IsOutdoor(coords) ? CellKind.OUTDOOR : CellKind.INDOOR;
                ParkingKind parkingKind = ParkingKind.NONE;
                foreach (Vector2 fireEngineSpot in fireEngineSpots)
                {
                    if (fireEngineSpot.Equals(coords))
                    {
                        fireEngineSpots.Remove(fireEngineSpot);
                        parkingKind = ParkingKind.FIRE_ENGINE;
                        break;
                    }
                }
                foreach (Vector2 ambulanceSpot in ambulanceSpots)
                {
                    if (ambulanceSpot.Equals(coords))
                    {
                        ambulanceSpots.Remove(ambulanceSpot);
                        parkingKind = ParkingKind.AMBULANCE;
                        break;
                    }
                }
                newCell.GetComponent<Cell>().Init(coords, kind, parkingKind, newGameObjectContainer);

                cells.Add(coords, newCell.GetComponent<Cell>());
                newCell.transform.SetParent(cellLayer.transform, false);
                newGameObjectContainer.transform.SetParent(gameObjectsLayer.transform, false);
            }
        }
    }

    bool IsOutdoor(Vector2 coords) {
		return (coords.x == 0 
			 || coords.x == 9 
			 || coords.y == 0 
			 || coords.y == 7);
	}

    //instantiates whatever edge we need
    //kind is 0 if empty, 1 if wall, 2 if door
    //vertical is true if it's a vertical wall, otherwise it's horizontal
    //cellDim is the dimensions of a cell on the board
    GameObject MakeEdge(int kind, bool vertical, Vector2 cellDim)
    {
        Vector3 ninetyClockwise = new Vector3(0, 0, -90);
        GameObject edge = null;
        if (kind == 0)
        {
            edge = Object.Instantiate(emptyPrefab) as GameObject;
        }
        else if (kind == 1)
        {
            edge = Object.Instantiate(wallPrefab) as GameObject;
        }
        else
        {
            edge = Object.Instantiate(doorPrefab) as GameObject;
        }

        RectTransform rt = edge.GetComponent<RectTransform>();
        Vector2 sizeDelta = rt.sizeDelta;
        if (!vertical)
        {
            rt.sizeDelta = new Vector2(sizeDelta.x, sizeDelta.y + (cellDim.x - cellDim.y));
            rt.Rotate(ninetyClockwise);
        }

        return edge;
    }

    //grabs the correct edge component from the edge
    //this just makes it so that there isn't a big if statement 
    //in the key methods
    Edge GetEdgeComponent(GameObject edge, int kind)
    {
        if (kind == 0)
        {
            return edge.GetComponent<Empty>();
        } 
        else if (kind == 1)
        {
            return edge.GetComponent<Wall>();
        } 
        else
        {
            return edge.GetComponent<Door>();
        }
    }

}
