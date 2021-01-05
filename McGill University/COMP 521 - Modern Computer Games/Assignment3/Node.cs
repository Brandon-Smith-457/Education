using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Node
{
    public Vector3 pos;
    public List<Node> neighbors;

    public float gCost;
    public float hCost;
    public Node parent;

    public Node(Vector3 pos)
    {
        this.pos = pos;
        neighbors = new List<Node>();
    }

    public float fCost()
    {
        return gCost + hCost;
    }

    public float getDistance(Node other)
    {
        return (other.pos - pos).magnitude;
    }
}
