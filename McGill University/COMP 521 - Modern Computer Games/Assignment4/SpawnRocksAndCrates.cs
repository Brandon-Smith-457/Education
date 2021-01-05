using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SpawnRocksAndCrates : MonoBehaviour
{
    public GameObject blockDirectPath;
    public GameObject randomCratesAndRocks;
    public GameObject crate;
    public GameObject rock;

    public static SpawnRocksAndCrates instance = null;

    private float xMin = -22.5f;
    private float xMax = 22.5f;
    private float zMin = -22.5f;
    private float zMax = 22.5f;

    private float xBlockStart = -22.4f;
    private float zBlockStart = -15.0f;
    private float xSpacing = 0.1f;

    private int numBlockDirect = 5;
    private int numRandom = 15;

    private System.Random random;
    private int seed = 123456789;
    private bool useSeed = false;

    private List<GameObject> rocksAndCrates;

    private void Awake()
    {
        if (instance == null) instance = this;
        else GameObject.Destroy(gameObject);
    }

    public void spawnRocksAndCrates()
    {
        random = useSeed ? new System.Random(seed) : new System.Random();
        rocksAndCrates = new List<GameObject>();

        for (int i = 0; i < numBlockDirect; i++)
        {
            GameObject temp = (random.Next(2) == 1) ? GameObject.Instantiate(crate, blockDirectPath.transform) : GameObject.Instantiate(rock, blockDirectPath.transform);
            temp.transform.localPosition = new Vector3(xBlockStart + (xSpacing + temp.transform.localScale.x) * i, temp.transform.localPosition.y, zBlockStart + (temp.transform.localScale.z / 2.0f) * i);
            rocksAndCrates.Add(temp);
        }

        int j = 0;
        while (j < numRandom)
        {
            float xPos = (float)(random.NextDouble()) * (xMax - xMin) + xMin;
            float zPos = (float)(random.NextDouble()) * (zMax - zMin) + zMin;
            GameObject temp = (random.Next(2) == 1) ? GameObject.Instantiate(crate, randomCratesAndRocks.transform) : GameObject.Instantiate(rock, randomCratesAndRocks.transform);
            while (!checkValidSpawn(xPos, zPos, temp))
            {
                xPos = (float)(random.NextDouble()) * (xMax - xMin) + xMin;
                zPos = (float)(random.NextDouble()) * (zMax - zMin) + zMin;
            }
            temp.transform.localPosition = new Vector3(xPos, temp.transform.localPosition.y, zPos);
            rocksAndCrates.Add(temp);
            j++;
        }
    }

    public bool checkValidSpawn(float xPos, float zPos, GameObject temp)
    {
        foreach (GameObject obstacle in rocksAndCrates)
        {
            if (Mathf.Abs(xPos - obstacle.transform.localPosition.x) < (obstacle.transform.localScale.x + temp.transform.localScale.x)/2.0f
             && Mathf.Abs(zPos - obstacle.transform.localPosition.z) < (obstacle.transform.localScale.z + temp.transform.localScale.z)/2.0f)
            {
                return false;
            }
        }
        return true;
    }
}
