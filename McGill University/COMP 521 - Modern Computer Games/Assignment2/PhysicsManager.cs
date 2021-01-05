using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PhysicsManager : MonoBehaviour
{
    public static PhysicsManager instance = null;

    public int binarySearchIterationMin = 10;

    List<MyRigidBody> myRigidBodies;
    List<ICollider> colliders;

    void Awake()
    {
        if (instance == null) instance = this;
        else Destroy(gameObject);
        myRigidBodies = new List<MyRigidBody>();
        colliders = new List<ICollider>();
    }

    void FixedUpdate()
    {
        // Looping over all the necessary collisions to check (because checking [i, i] is pointless, and checking [i,j] is the same as checking [j, i])
        for (int i = 0; i < colliders.Count; i++)
        {
            for (int j = i + 1; j < colliders.Count; j++)
            {
                if (colliders[i].isColliding(colliders[j]))
                {
                    collisionResponse(i, j);
                }
            }
        }
    }

    public void addEntity(MyRigidBody myRigidBody, ICollider collider)
    {
        myRigidBody.id = myRigidBodies.Count;
        myRigidBodies.Add(myRigidBody);

        collider.setId(colliders.Count);
        colliders.Add(collider);
    }

    public void removeId(int id)
    {
        myRigidBodies.RemoveAt(id);
        colliders.RemoveAt(id);
        for (int i = id; i < myRigidBodies.Count; i++)
        {
            myRigidBodies[i].id = i;
            colliders[i].setId(i);
        }
    }

    private void collisionResponse(int id1, int id2)
    {
        if (myRigidBodies[id1].isWater)
        {
            Destroy(myRigidBodies[id2].gameObject);
            removeId(id2);
            return;
        }
        else if (myRigidBodies[id2].isWater)
        {
            Destroy(myRigidBodies[id1].gameObject);
            removeId(id1);
            return;
        }

        Vector3 normal;
        Vector3 newPosOfCircle;
        Vector3 newVelocityOfCircle;
        if (typeof(CircleCollider).IsInstanceOfType(colliders[id1]) && typeof(LineCollider).IsInstanceOfType(colliders[id2]))
        {
            // Find a collision normal/tangent
            normal = circleCollisionWithLineNormal(id2);

            // Deal with Interpenetration
            newPosOfCircle = binarySearchRewind(id1, id2);
            
            // Find new velocities
            newVelocityOfCircle = resultingVelocity(id1, id2, normal);
        }
        else if (typeof(CircleCollider).IsInstanceOfType(colliders[id2]) && typeof(LineCollider).IsInstanceOfType(colliders[id1]))
        {
            // Find a collision normal/tangent
            normal = circleCollisionWithLineNormal(id1);
            
            // Deal with Interpenetration
            newPosOfCircle = binarySearchRewind(id2, id1);
            
            // Find new velocities
            newVelocityOfCircle = resultingVelocity(id2, id1, normal);
        }
        else // This is where I would handle the Verlet Collision Response
            return;
    }

    // Returns the normal of the given line
    private Vector3 circleCollisionWithLineNormal(int lineId)
    {
        Vector3 startPos = ((LineCollider)colliders[lineId]).startPos;
        Vector3 endPos = ((LineCollider)colliders[lineId]).endPos;

        Vector3 dv = endPos - startPos;

        return new Vector3(-dv.y, dv.x);
    }

    // Returns the position of the circle such that it is no longer intersecting the line.
    private Vector3 binarySearchRewind(int circleId, int lineId)
    {
        int i = 0;
        MyRigidBody mrb = myRigidBodies[circleId];

        while (colliders[circleId].isColliding(colliders[lineId]) && i < binarySearchIterationMin)
        {
            if (colliders[circleId].isColliding(colliders[lineId]))
                mrb.updatePosition(-Time.deltaTime / Mathf.Pow(2, i));
            else
                mrb.updatePosition(Time.deltaTime / Mathf.Pow(2, i));
            i++;
        }

        return mrb.transform.position;
    }

    // Calculates the resultant velocity of the collision
    private Vector3 resultingVelocity(int circleId, int lineId, Vector3 normal)
    {
        Vector3 startPos = ((LineCollider)colliders[lineId]).startPos;
        Vector3 endPos = ((LineCollider)colliders[lineId]).endPos;
        Vector3 tangent = (endPos - startPos).normalized;

        MyRigidBody mrb = myRigidBodies[circleId];
        Vector3 velocity = mrb.velocity;

        float vt = Vector3.Dot(velocity, tangent);

        float vnminus = Vector3.Dot(velocity, normal);
        float vnplus = vnminus - (1 + MyRigidBody.coefficientOfRestitution) * vnminus;

        Vector3 vtplusVec = tangent * vt;
        Vector3 vnplusVec = normal * vnplus;
        Vector3 vPlus = vtplusVec + vnplusVec;

        mrb.velocity = vPlus;

        return vPlus;
    }
}
