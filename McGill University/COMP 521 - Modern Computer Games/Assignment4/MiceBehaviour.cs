using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MiceBehaviour : MonoBehaviour
{
    private Vector3 velocity;
    private float maxSpeed = 10.0f;
    private float fov = 45.0f;

    private float accelerationFactor = 2.0f;
    private float steerDistance = 2.5f;
    private float rightSteeringFactor = 3.0f;
    private float forwardSteeringFactor = 1.0f;
    private float leftSteeringFactor = 3.0f;
    private float turnaroundValue = 0.5f;
    private float kickFactor = 2.0f;

    private static System.Random random = null;
    private static int seed = 123456789;
    private static bool useSeed = false;

    private bool idle = false;
    private double idleTime = 3.0;
    private double idleStartTime = -1.0;
    private double elapsedTime = -1.0;
    private System.TimeSpan time;

    void Start()
    {
        if (random == null)
            random = (useSeed) ? new System.Random(seed) : new System.Random();
        velocity = transform.forward;
        velocity = Quaternion.AngleAxis(random.Next(0, 360), Vector3.up) * velocity;
    }

    void FixedUpdate()
    {
        if (random.Next(0, 10000) < 5 || idle)
        {
            idle = true;
            time = System.DateTime.UtcNow - System.DateTime.MinValue;
            if (idleStartTime == -1)
            {
                idleStartTime = time.TotalSeconds;
                transform.forward = velocity.normalized;
                velocity = new Vector3(0.0f, 0.0f, 0.0f);
            }
            elapsedTime = time.TotalSeconds - idleStartTime;
            if (elapsedTime >= idleTime)
            {
                idleStartTime = -1.0;
                elapsedTime = -1.0;
                idle = false;
                velocity = transform.forward;
            }
            return;
        }

        Vector3 forwardV = velocity.normalized;
        RaycastHit forwardHit;
        if (Physics.Raycast(transform.position, forwardV, out forwardHit, Mathf.Infinity))
        {
            Debug.DrawRay(transform.position, forwardV * forwardHit.distance, Color.green);
        }
        Vector3 leftV = Quaternion.AngleAxis(-fov, Vector3.up) * forwardV;
        RaycastHit leftHit;
        if (Physics.Raycast(transform.position, leftV, out leftHit, Mathf.Infinity))
        {
            Debug.DrawRay(transform.position, leftV * leftHit.distance, Color.red);
        }
        Vector3 rightV = Quaternion.AngleAxis(fov, Vector3.up) * forwardV;
        RaycastHit rightHit;
        if (Physics.Raycast(transform.position, rightV, out rightHit, Mathf.Infinity))
        {
            Debug.DrawRay(transform.position, rightV * rightHit.distance, Color.blue);
        }

        // In our closed system with an infinite raycast we're always guaranteed to have hit something
        float minDist = Mathf.Min(forwardHit.distance, leftHit.distance, rightHit.distance);
        if (minDist <= steerDistance)
        {
            Vector3 acceleration = leftV * -(velocity.magnitude / leftHit.distance + turnaroundValue) * rightSteeringFactor;
            acceleration += forwardV * -(velocity.magnitude / forwardHit.distance + turnaroundValue) * forwardSteeringFactor;
            acceleration += rightV * -(velocity.magnitude / rightHit.distance + turnaroundValue) * leftSteeringFactor;
            if (velocity.magnitude <= 1.0f)
            {
                if (random.Next(0, 2) == 1)
                    acceleration += rightV * -(velocity.magnitude / rightHit.distance + turnaroundValue) * kickFactor;
                else
                    acceleration += leftV * -(velocity.magnitude / leftHit.distance + turnaroundValue) * kickFactor;
            }
            velocity += acceleration * Time.fixedDeltaTime;
        }
        else
        {
            if (velocity.magnitude >= maxSpeed)
            {
                velocity = velocity.normalized * maxSpeed;
            }
            else
            {
                float accel = (velocity.magnitude > 1.0f) ? minDist / velocity.magnitude : minDist;
                Vector3 acceleration = velocity.normalized * accel * accelerationFactor;
                velocity += acceleration * Time.fixedDeltaTime;
            }
        }

        transform.position += velocity * Time.fixedDeltaTime;
    }

    void OnCollisionEnter(Collision other)
    {
        if (other.gameObject.layer == LayerMask.NameToLayer("Monster"))
        {
            GameObject.Destroy(gameObject);
        }
    }
}
