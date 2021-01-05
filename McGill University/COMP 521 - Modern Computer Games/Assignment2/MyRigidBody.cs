using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MyRigidBody : MonoBehaviour
{
    public int id;

    public static float gravity = -9.81f;
    public static float windResistance = 0.0f;
    public static float coefficientOfRestitution = 0.5f;
    
    public Vector3 velocity;

    public bool isStatic = false;
    public bool isVerlet = false;
    public bool isWater = false;

    public float stationaryCutoff = 0.01f;

    private int deathCounter = 0;
    private float stationaryDeathTime = 5.0f; //Seconds
    private float fixedCallsUntilDeath;

    private float screenWidth;

    void Start()
    {
        fixedCallsUntilDeath = stationaryDeathTime / Time.fixedDeltaTime;
        screenWidth = Camera.main.aspect * Camera.main.orthographicSize * 2;
    }

    // This function is called indepently of frame-rate at Time.fixedDeltaTime second intervals
    void FixedUpdate()
    {
        if (isStatic || isVerlet) return;

        velocity.x = velocity.x +  windResistance * Time.deltaTime;
        velocity.y = velocity.y + gravity * Time.deltaTime;

        // Checking if the body has remained stationary for "stationaryDeathTime" amount of time.
        if (velocity.magnitude < stationaryCutoff)
        {
            if (++deathCounter >= fixedCallsUntilDeath)
                PhysicsManager.instance.removeId(id);
        }
        else deathCounter = 0;

        Vector3 pos = transform.position;
        pos.x = pos.x + velocity.x * Time.deltaTime;
        pos.y = pos.y + velocity.y * Time.deltaTime;
        transform.position = pos;

        if (transform.position.x < 0 || transform.position.x > screenWidth)
        {
            PhysicsManager.instance.removeId(id);
            Destroy(gameObject);
            return;
        }
    }

    // A helper function for when we need to binary search backup
    public void updatePosition(float deltaTime)
    {
        Vector3 pos = transform.position;
        pos.x = pos.x + velocity.x * deltaTime;
        pos.y = pos.y + velocity.y * deltaTime;
        transform.position = pos;
    }
}
