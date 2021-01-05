using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SpawningControlFlow : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        SpawnRocksAndCrates.instance.spawnRocksAndCrates();
        MiceSpawning.instance.spawnMice();
    }
}
