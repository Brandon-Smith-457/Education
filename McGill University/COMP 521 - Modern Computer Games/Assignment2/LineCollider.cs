using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LineCollider : MonoBehaviour, ICollider
{
    public int id;

    public Vector3 startPos;
    public Vector3 endPos;

    public bool isColliding(ICollider other)
    {
        if (typeof(CircleCollider).IsInstanceOfType(other))
            return collisionWithCircle((CircleCollider)other);
        else if (typeof(LineCollider).IsInstanceOfType(other))
            return collisionWithLine((LineCollider)other);
        Debug.Log("Unsupported collision:");
        Debug.Log(other);
        return false;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    private bool collisionWithCircle(CircleCollider circle)
    {
        bool inside1 = circleCollisionWithPoint(circle, startPos);
        bool inside2 = circleCollisionWithPoint(circle, endPos);
        if (inside1 || inside2) return true;

        float lineLength = (endPos - startPos).magnitude;

        // Dot product of line and circle
        float dot = (((circle.transform.position.x - startPos.x) * (endPos.x - startPos.x)) + ((circle.transform.position.y - startPos.y) * (endPos.y - startPos.y))) / Mathf.Pow(lineLength, 2);

        float closestX = startPos.x + (dot * (endPos.x - startPos.x));
        float closestY = startPos.y + (dot * (endPos.y - startPos.y));
        Vector3 closest = new Vector3(closestX, closestY);

        if (!collisionWithPoint(closest)) return false;

        float dist = (closest - circle.transform.position).magnitude;

        return dist <= circle.radius;
    }

    // Don't think this one is necessary for this assignment however it would be pretty straight forward to implement.  I will not cause running outta time
    private bool collisionWithLine(LineCollider other)
    {

        return false;
    }

    // Some helper functions for the collisionWithLine function
    private bool circleCollisionWithPoint(CircleCollider circle, Vector3 point)
    {
        float dist = (point - circle.transform.position).magnitude;
        return dist <= circle.radius;
    }

    private bool collisionWithPoint(Vector3 point)
    {
        float distToStart = (point - startPos).magnitude;
        float distToEnd = (point - endPos).magnitude;
        float lineLength = (endPos - startPos).magnitude;

        float lineWidth = TerrainGenerator.instance.lineWidth;

        return distToStart + distToEnd >= lineLength - lineWidth && distToStart + distToEnd <= lineLength + lineWidth;
    }

}
