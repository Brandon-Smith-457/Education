using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MiceSpawning : MonoBehaviour
{
    public static MiceSpawning instance = null;

    public GameObject mouse;

    private float xMin = -22.5f;
    private float xMax = 22.5f;
    private float zMin = -22.5f;
    private float zMax = 22.5f;

    private int numMice = 5;

    private System.Random random;
    private int seed = 123456789;
    private bool useSeed = false;

    private List<GameObject> mice;

    private void Awake()
    {
        if (instance == null) instance = this;
        else GameObject.Destroy(gameObject);
    }

    public void spawnMice()
    {
        random = useSeed ? new System.Random(seed) : new System.Random();
        mice = new List<GameObject>();

        int j = 0;
        while (j < numMice)
        {
            float xPos = (float)(random.NextDouble()) * (xMax - xMin) + xMin;
            float zPos = (float)(random.NextDouble()) * (zMax - zMin) + zMin;
            GameObject temp = GameObject.Instantiate(mouse, transform);
            while (!SpawnRocksAndCrates.instance.checkValidSpawn(xPos, zPos, temp) || !checkValidSpawn(xPos, zPos, temp))
            {
                xPos = (float)(random.NextDouble()) * (xMax - xMin) + xMin;
                zPos = (float)(random.NextDouble()) * (zMax - zMin) + zMin;
            }
            temp.transform.localPosition = new Vector3(xPos, temp.transform.localPosition.y, zPos);
            mice.Add(temp);
            j++;
        }
    }

    public bool checkValidSpawn(float xPos, float zPos, GameObject temp)
    {
        foreach (GameObject other in mice)
        {
            if (Mathf.Abs(xPos - other.transform.localPosition.x) < (other.transform.localScale.x + temp.transform.localScale.x)
             && Mathf.Abs(zPos - other.transform.localPosition.z) < (other.transform.localScale.z + temp.transform.localScale.z))
            {
                return false;
            }
        }
        return true;
    }
}
