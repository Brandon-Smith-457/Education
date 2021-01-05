using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerShootController : MonoBehaviour
{
    public int ammo = 0;

    public GameObject bullet;

    public Camera firstPersonCamera;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetButtonDown("Fire1") && ammo > 0)
        {
            GameObject bulletGO = Instantiate(bullet, firstPersonCamera.transform.position, firstPersonCamera.transform.rotation);
            bulletGO.transform.position += bulletGO.transform.forward * 0.5f;
            ammo--;
        }
    }

    public void addAmmo(int amount)
    {
        ammo += amount;
    }
}
