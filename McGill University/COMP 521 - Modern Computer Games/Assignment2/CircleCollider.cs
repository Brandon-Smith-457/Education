using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CircleCollider : MonoBehaviour, ICollider
{
    public int id;

    public float radius;

    // Start is called before the first frame update
    void Start()
    {

    }

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

    private bool collisionWithCircle(CircleCollider other)
    {
        float distBetweenColliders = (other.transform.position - transform.position).magnitude;
        return distBetweenColliders <= radius + other.radius;
    }

    private bool collisionWithLine(LineCollider line)
    {
        bool inside1 = collisionWithPoint(line.startPos);
        bool inside2 = collisionWithPoint(line.endPos);
        if (inside1 || inside2) return true;

        float lineLength = (line.endPos - line.startPos).magnitude;

        // Dot product of line and circle
        float dot = (((transform.position.x - line.startPos.x) * (line.endPos.x - line.startPos.x)) + ((transform.position.y - line.startPos.y) * (line.endPos.y - line.startPos.y))) / Mathf.Pow(lineLength, 2);

        float closestX = line.startPos.x + (dot * (line.endPos.x - line.startPos.x));
        float closestY = line.startPos.y + (dot * (line.endPos.y - line.startPos.y));
        Vector3 closest = new Vector3(closestX, closestY);

        if (!lineCollisionWithPoint(line, closest)) return false;

        float dist = (closest - transform.position).magnitude;

        return dist <= radius;
    }

    // Some helper functions for the collisionWithLine function
    private bool collisionWithPoint(Vector3 point)
    {
        float dist = (point - transform.position).magnitude;
        return dist <= radius;
    }

    private bool lineCollisionWithPoint(LineCollider line, Vector3 point)
    {
        float distToStart = (point - line.startPos).magnitude;
        float distToEnd = (point - line.endPos).magnitude;
        float lineLength = (line.endPos - line.startPos).magnitude;

        float lineWidth = TerrainGenerator.instance.lineWidth;

        return distToStart + distToEnd >= lineLength - lineWidth && distToStart + distToEnd <= lineLength + lineWidth;
    }
}
