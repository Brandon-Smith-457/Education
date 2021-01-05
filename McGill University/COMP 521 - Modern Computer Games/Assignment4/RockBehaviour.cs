using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RockBehaviour : MonoBehaviour
{
    private Rigidbody rb;

    private bool held = false;
    private bool airbourne = false;
    private float stationaryCutoff = 0.5f;

    // Start is called before the first frame update
    void Start()
    {
        rb = gameObject.GetComponent<Rigidbody>();
    }

    // Update is called once per frame
    void Update()
    {
        if (rb.velocity.magnitude <= stationaryCutoff)
        {
            airbourne = false;
        }
    }

    public void pickedup()
    {
        held = true;
    }

    public void thrown()
    {
        held = false;
        airbourne = true;
    }

    void OnCollisionEnter(Collision other)
    {
        if (other.gameObject.layer == LayerMask.NameToLayer("Player") && airbourne)
        {
            other.gameObject.GetComponent<PlayerState>().damagePlayer();
        }
    }
}
