using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TerrainGenerator : MonoBehaviour
{
    public static TerrainGenerator instance = null;

    // Prefabs for instantiation
    public GameObject linePrefab;
    public GameObject pointPrefab;

    // Values that dictate the "static" randomized shape
    public float groundHeightMin = 1.0f;
    public float groundHeightMax = 3.0f;
    public float waterFloorHeightMin = 0.5f;
    public float waterFloorHeightMax = 1.0f;
    private float waterHeightRelativeToFloor = 1.0f;
    public float mountainLeftHeightMin = 5.0f;
    public float mountainLeftHeightMax = 7.0f;
    public float mountainRightHeightMin = 5.0f;
    public float mountainRightHeightMax = 7.0f;

    // Some values that are useful to know from other classes such as the AssetManager
    public float groundHeight;
    public float waterFloorHeight;
    public float waterHeight;
    public float mountainLeftHeight;
    public float mountainRightHeight;

    // Seeds for terrain and water randomization
    public int seed = 123456789;
    public int waterSeed = 987654321;
    public bool useSeed = true;

    // Parameters for the perlin noise
    public float perlinNoiseCoarseAmplitude = 0.5f;
    public int numCoarsePerlinPoints = 10;
    public int numPerlinOctaves = 8;
    public int perlinOctaveDivisor = 2;

    // How many points to plot and the screen width for determining the world coordinates
    public int horizontalDiscretization = 50; // Must be divisible by 5 for my "static" terrain generation to work unfortunately.
    private float screenWidth = 0;

    // Parameters for the visual lines
    private int interpolationDensity = 100;
    private Color groundColor = Color.red;
    private Color waterColor = Color.blue;
    public float lineWidth = 0.1f;

    // List of points
    private List<GameObject> points;
    private List<GameObject> waterPoints;

    // List of sprite renderers to change the color of the points
    private List<SpriteRenderer> spriteRenderers;
    private List<SpriteRenderer> waterSpriteRenderers;

    // Produce a visual version of the terrain using cosine interpolation, but return a linear interpolation for collision detection (Likely going to use boxes instead of lines).
    void Awake()
    {
        if (instance == null) instance = this;
        else Destroy(gameObject);

        points = new List<GameObject>();
        spriteRenderers = new List<SpriteRenderer>();
        screenWidth = Camera.main.aspect* Camera.main.orthographicSize * 2;

        //Initialize all the points and set their colors
        for (int i = 0; i < horizontalDiscretization; i++)
        {
            points.Add(GameObject.Instantiate(pointPrefab, transform));
            spriteRenderers.Add(points[points.Count - 1].GetComponent<SpriteRenderer>());
            spriteRenderers[i].color = groundColor;
        }

        System.Random rand = useSeed ? new System.Random(seed) : new System.Random();

        // I repositioned the camera such that 0,0 is in the bottom left (see CameraControl script) so these height valuess are the exact y values
        groundHeight = groundHeightMin + ((float)rand.NextDouble()) * (groundHeightMax - groundHeightMin);
        waterFloorHeight = waterFloorHeightMin + ((float)rand.NextDouble()) * (waterFloorHeightMax - waterFloorHeightMin);
        waterHeight = waterFloorHeight + waterHeightRelativeToFloor;
        mountainLeftHeight = mountainLeftHeightMin + ((float)rand.NextDouble()) * (mountainLeftHeightMax - mountainLeftHeightMin);
        mountainRightHeight = mountainRightHeightMin + ((float)rand.NextDouble()) * (mountainRightHeightMax - mountainRightHeightMin);

        // Programmatically creating a randomized version of the terrain (pre-noise)
        float xSpacing = screenWidth / (horizontalDiscretization - 1);
        int regionWidth = (int)(horizontalDiscretization * 0.2f);
        int diffX = regionWidth / 2;
        for (int i = 0; i < horizontalDiscretization; i++)
        {
            switch (i)
            {
                // Region: Left Ground or Right Ground
                case int n when ((n < (int)(horizontalDiscretization * 0.2f)) || (n >= (int)(horizontalDiscretization * 0.8f) && n <= horizontalDiscretization)):
                    points[i].transform.position = new Vector3(i * xSpacing, groundHeight, 0);
                    break;
                // Region: Left Mountain
                case int n when ((n >= (int)(horizontalDiscretization * 0.2f)) && (n < (int)(horizontalDiscretization * 0.4f))):
                    if (i % regionWidth < diffX)
                    {
                        float y = (mountainLeftHeight - groundHeight) / (diffX) * (i % diffX) + groundHeight;
                        points[i].transform.position = new Vector3(i * xSpacing, y, 0);
                    }
                    else
                    {
                        float y = (waterFloorHeight - mountainLeftHeight) / (diffX) * (i % diffX) + mountainLeftHeight;
                        points[i].transform.position = new Vector3(i * xSpacing, y, 0);
                    }
                    break;
                // Region: Water Floor
                case int n when (n >= (int)(horizontalDiscretization * 0.4f) && n < (int)(horizontalDiscretization * 0.6f)):
                    points[i].transform.position = new Vector3(i * xSpacing, waterFloorHeight, 0);
                    break;
                // Region: Right Mountain
                case int n when (n >= (int)(horizontalDiscretization * 0.6f) && n < (int)(horizontalDiscretization * 0.8f)):
                    if (i % regionWidth < diffX)
                    {
                        float y = (mountainRightHeight - waterFloorHeight) / (diffX) * (i % diffX) + waterFloorHeight;
                        points[i].transform.position = new Vector3(i * xSpacing, y, 0);
                    }
                    else
                    {
                        float y = (groundHeight - mountainRightHeight) / (diffX) * (i % diffX) + mountainRightHeight;
                        points[i].transform.position = new Vector3(i * xSpacing, y, 0);
                    }
                    break;
            }
        }

        //Time to add some Perlin Noise!!!!!
        seed = useSeed ? seed : rand.Next();
        PerlinNoise perlinNoise = new PerlinNoise(seed, perlinNoiseCoarseAmplitude, horizontalDiscretization / numCoarsePerlinPoints, 0, horizontalDiscretization, numPerlinOctaves, perlinOctaveDivisor);
        for (int i = 0; i < points.Count; i++)
        {
            Vector3 pos = points[i].transform.position;
            points[i].transform.position = new Vector3(pos.x, pos.y + perlinNoise.getNoise(i), pos.z);
        }

        interpolate(points, interpolationDensity, groundColor, lineWidth, false);

        // Add in the water line and then noise it up.
        waterPoints = new List<GameObject>();
        waterSpriteRenderers = new List<SpriteRenderer>();
        int regionMax = (int)(horizontalDiscretization * 0.6f) + 2;
        int regionMin = (int)(horizontalDiscretization * 0.4f) - 1;
        for (int i = regionMin; i < regionMax; i++)
        {
            int index = i - regionMin;
            waterPoints.Add(GameObject.Instantiate(pointPrefab, transform));
            waterSpriteRenderers.Add(waterPoints[waterPoints.Count - 1].GetComponent<SpriteRenderer>());
            waterSpriteRenderers[index].color = waterColor;
            waterPoints[index].transform.position = new Vector3(i * xSpacing, waterHeight, 0);
        }

        waterSeed = useSeed ? waterSeed : rand.Next();
        PerlinNoise waterPerlinNoise = new PerlinNoise(waterSeed, perlinNoiseCoarseAmplitude/2, (regionMax - regionMin) / numCoarsePerlinPoints, regionMin, regionMax, numPerlinOctaves, perlinOctaveDivisor);
        for (int i = regionMin; i < regionMax; i++)
        {
            int index = i - regionMin;
            Vector3 pos = waterPoints[index].transform.position;
            waterPoints[index].transform.position = new Vector3(pos.x, pos.y + waterPerlinNoise.getNoise(i), pos.z);
        }

        interpolate(waterPoints, interpolationDensity, waterColor, lineWidth, true);

        // TODO: Pass the mountain and water lines over to the physics system!-----------------------

        //-------------------------------------------------------------------------------------------
    }

    // x[0, 1] gives you the y value of the interpolation at the given proportional x position between the two points.
    public float cosineInterpolation(float y1, float y2, float x)
    {
        float fRad = x * Mathf.PI;
        float f = (1 - Mathf.Cos(fRad)) * 0.5f;
        return y1 * (1 - f) + y2 * f;
    }

    void interpolate(List<GameObject> points, int lineDensity, Color lineColor, float lineWidth, bool isWater)
    {
        List<LineRenderer> lineRenderers = new List<LineRenderer>();

        for (int i = 0; i < points.Count - 1; i++)
        {
            lineRenderers.Add(GameObject.Instantiate(linePrefab, transform).GetComponent<LineRenderer>());
            lineRenderers[i].startColor = lineColor;
            lineRenderers[i].endColor = lineColor;
            lineRenderers[i].startWidth = lineWidth;
            lineRenderers[i].endWidth = lineWidth;
            lineRenderers[i].positionCount = lineDensity + 1;
        }

        for (int j = 0; j < lineRenderers.Count; j++)
        {
            // Make one interpolation.
            for (int i = 0; i <= lineDensity; i++)
            {
                Vector3 pos = new Vector3((float)i / (float)lineDensity, cosineInterpolation(points[j].transform.position.y, points[j + 1].transform.position.y, (float)i / (float)lineDensity), 0);
                pos.x *= (points[j + 1].transform.position.x - points[j].transform.position.x);
                pos.x += points[j].transform.position.x;
                pos.z += points[j].transform.position.z; //kinda un-necessary but felt like I should be complete
                lineRenderers[j].SetPosition(i, pos);
            }

            // Creating a line for physics calculations
            GameObject lineObject = new GameObject("line" + j);
            lineObject.transform.SetParent(PhysicsManager.instance.transform);
            lineObject.AddComponent<MyRigidBody>();
            lineObject.AddComponent<LineCollider>();
            lineObject.GetComponent<MyRigidBody>().isStatic = true;
            lineObject.GetComponent<MyRigidBody>().isWater = isWater;
            lineObject.transform.position = points[j].transform.position;
            lineObject.GetComponent<LineCollider>().startPos = points[j].transform.position;
            lineObject.GetComponent<LineCollider>().endPos = points[j + 1].transform.position;
            PhysicsManager.instance.addEntity(lineObject.GetComponent<MyRigidBody>(), lineObject.GetComponent<LineCollider>());
        }
    }

}
