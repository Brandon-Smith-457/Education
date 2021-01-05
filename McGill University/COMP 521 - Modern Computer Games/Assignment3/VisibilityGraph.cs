using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class VisibilityGraph : MonoBehaviour
{
    public GameObject edgePrefab;
    public static VisibilityGraph instance = null;

    public List<Node> nodes;
    private bool generated = false;
    private List<LineRenderer> lineRenderers;
    int lineCount = 0;

    // Start is called before the first frame update
    void Start()
    {
        if (instance == null)
            instance = this;
        else
            GameObject.Destroy(gameObject);
    }

    // This code lies in the update function because it's dependent on the LShapes having been instantiated which apparently doesn't resolve until AFTER all the static start functions are called
    void Update()
    {
        if (!generated)
        {
            // Get the positions of every node to be added to the visibility graph
            nodes = new List<Node>();
            for (int i = 0; i < transform.childCount; i++)
            {
                nodes.Add(new Node(transform.GetChild(i).position));
            }

            // Build up the visibility edges
            lineRenderers = new List<LineRenderer>();
            for (int i = 0; i < nodes.Count; i++)
            {
                for (int j = i + 1; j < nodes.Count; j++)
                {
                    if (visible(nodes[i], nodes[j]))
                    {
                        //lineRenderers.Add(GameObject.Instantiate(edgePrefab, transform).GetComponent<LineRenderer>());
                        //lineRenderers[lineCount].positionCount = 2;
                        //lineRenderers[lineCount].SetPosition(0, nodes[i].pos);
                        //lineRenderers[lineCount].SetPosition(1, nodes[j].pos);
                        //lineCount++;

                        nodes[i].neighbors.Add(nodes[j]);
                        nodes[j].neighbors.Add(nodes[i]);
                    }
                }
            }

            // TODO: Reduction of the visibility graph
            generated = true;
        }
    }

    public void drawPath(List<Node> path)
    {
        for (int i = 0; i < path.Count - 1; i++)
        {
            lineRenderers.Add(GameObject.Instantiate(edgePrefab, transform).GetComponent<LineRenderer>());
            lineRenderers[lineCount].positionCount = 2;
            lineRenderers[lineCount].SetPosition(0, path[i].pos);
            lineRenderers[lineCount].SetPosition(1, path[i+1].pos);
            lineCount++;
        }
    }

    public bool visible(Node node1, Node node2)
    {
        float thickness = 0.125f;
        int layerMask = 1 << 8;
        RaycastHit hit;
        return !Physics.SphereCast(node1.pos, thickness, (node2.pos - node1.pos), out hit, (node2.pos - node1.pos).magnitude, layerMask);
    }

    public List<Node> Astar(Node s, Node d)
    {
        List<Node> fringe = new List<Node>();
        List<Node> deadSet = new List<Node>();
        fringe.Add(s);

        while (fringe.Count > 0)
        {
            Node current = fringe[0];
            for (int i = 1; i < fringe.Count; i++)
            {
                if (fringe[i].fCost() < current.fCost() || fringe[i].fCost() == current.fCost() && fringe[i].hCost < current.hCost)
                {
                    current = fringe[i];
                }
            }

            fringe.Remove(current);
            deadSet.Add(current);

            if (current == d)
            {
                return retracePath(s, d);
            }

            for (int i = 0; i < current.neighbors.Count; i++)
            {
                if (deadSet.Contains(current.neighbors[i]))
                    continue;

                float moveCost = current.gCost + current.getDistance(current.neighbors[i]);
                if (moveCost < current.neighbors[i].gCost || !fringe.Contains(current.neighbors[i]))
                {
                    current.neighbors[i].gCost = moveCost;
                    current.neighbors[i].hCost = current.neighbors[i].getDistance(d);
                    current.neighbors[i].parent = current;

                    if (!fringe.Contains(current.neighbors[i]))
                    {
                        fringe.Add(current.neighbors[i]);
                    }
                }
            }
        }

        return null;
    }

    private List<Node> retracePath(Node s, Node d)
    {
        List<Node> path = new List<Node>();
        Node current = d;
        while (current != s)
        {
            path.Add(current);
            current = current.parent;
        }
        path.Reverse();
        return path;
    }
}
