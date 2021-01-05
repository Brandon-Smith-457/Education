using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AssetManager : MonoBehaviour
{
    public static AssetManager instance = null;

    public GameObject canonPrefab;
    public GameObject canonBallPrefab;

    public GameObject canonLeft;
    public GameObject canonRight;

    public List<GameObject> canonBalls;

    private float screenWidth;
    private float leftCanonPosition;
    private float rightCanonPosition;

    void Awake()
    {
        if (instance == null) instance = this;
        else Destroy(gameObject);
    }

    // Start is called before the first frame update
    void Start()
    {
        screenWidth = Camera.main.aspect * Camera.main.orthographicSize * 2;
        
        // Instantiate the two canons at 7% and 93% of the screen's x positions
        leftCanonPosition = screenWidth * 0.07f;
        rightCanonPosition = screenWidth * 0.93f;
        canonLeft = GameObject.Instantiate(canonPrefab);
        canonRight = GameObject.Instantiate(canonPrefab);
        // The canon on the right is flipped 180% over the x axis
        canonRight.GetComponent<SpriteRenderer>().flipX = true;
        // The heights of the canons are not exact to the perlin noise however by using the coarse amplitude we can get decently close
        canonLeft.transform.position = new Vector3(leftCanonPosition, TerrainGenerator.instance.groundHeight + TerrainGenerator.instance.perlinNoiseCoarseAmplitude * 2, 0);
        canonRight.transform.position = new Vector3(rightCanonPosition, TerrainGenerator.instance.groundHeight + TerrainGenerator.instance.perlinNoiseCoarseAmplitude * 2, 0);
        canonLeft.transform.localScale = new Vector3(0.7f, 0.7f, 1.0f);
        canonRight.transform.localScale = new Vector3(0.7f, 0.7f, 1.0f);
    }

    // A function that can be used by an external class (CanonBehaviour) to spawn a canonBall at a given position with a given velocity.
    public void spawnCanonBall(Vector3 position, Vector3 velocity)
    {
        canonBalls.Add(GameObject.Instantiate(canonBallPrefab));
        canonBalls[canonBalls.Count - 1].AddComponent<MyRigidBody>();
        canonBalls[canonBalls.Count - 1].AddComponent<CircleCollider>();

        canonBalls[canonBalls.Count - 1].transform.position = position;
        canonBalls[canonBalls.Count - 1].transform.localScale = new Vector3(0.45f, 0.45f, 0.45f);

        canonBalls[canonBalls.Count - 1].GetComponent<MyRigidBody>().velocity = velocity;
        canonBalls[canonBalls.Count - 1].GetComponent<CircleCollider>().radius = canonBalls[canonBalls.Count - 1].GetComponent<SpriteRenderer>().bounds.size.x / 2;

        PhysicsManager.instance.addEntity(canonBalls[canonBalls.Count - 1].GetComponent<MyRigidBody>(), canonBalls[canonBalls.Count - 1].GetComponent<CircleCollider>());
    }
}
