using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Agent : MonoBehaviour
{
    private Node startingNode;
    private Node destinationNode = null;
    private bool added = false;
    // Start is called before the first frame update
    void Start()
    {
        startingNode = new Node(transform.position);
    }

    // Update is called once per frame
    void Update()
    {
        if (!added && destinationNode != null)
        {
            if (!AgentManager.spawningPhase)
            {
                for (int i = 0; i < VisibilityGraph.instance.nodes.Count; i++)
                {
                    if (VisibilityGraph.instance.visible(startingNode, VisibilityGraph.instance.nodes[i]))
                    {
                        startingNode.neighbors.Add(VisibilityGraph.instance.nodes[i]);
                        VisibilityGraph.instance.nodes[i].neighbors.Add(startingNode);
                    }
                    if (VisibilityGraph.instance.visible(destinationNode, VisibilityGraph.instance.nodes[i]))
                    {
                        destinationNode.neighbors.Add(VisibilityGraph.instance.nodes[i]);
                        VisibilityGraph.instance.nodes[i].neighbors.Add(destinationNode);
                    }
                }
            
                List<Node> path = VisibilityGraph.instance.Astar(startingNode, destinationNode);
                path.Insert(0, startingNode); // THESE TWO LINES RIGHT HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                VisibilityGraph.instance.drawPath(path); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                for (int i = 0; i < VisibilityGraph.instance.nodes.Count; i++)
                {
                    VisibilityGraph.instance.nodes[i].neighbors.Remove(startingNode);
                }
                startingNode.neighbors.Clear();

                added = true;
            }
        }

        if (Input.GetMouseButtonDown(0) && destinationNode == null)
        {
            Vector3 mousePos = Input.mousePosition;
            mousePos = Camera.main.ScreenToWorldPoint(mousePos);
            mousePos.z = -0.3f;
            destinationNode = new Node(mousePos);
        }
    }

    void OnCollisionStay(Collision collision)
    {
        if (!AgentManager.spawningPhase)
        {
            GameObject.Destroy(gameObject);
            AgentManager.spawningPhase = true;
        }
    }
}
