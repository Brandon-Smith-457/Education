using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AmmoBehaviour : MonoBehaviour
{
    public float bobbingSpeed = 0.5f;
    public float bobbingHeight = 0.25f;

    public LayerMask playerLayer;
    public PlayerShootController playerShoot;

    Vector3 initialPosition;
    // Start is called before the first frame update
    void Start()
    {
        initialPosition = transform.position;
    }

    // Update is called once per frame
    void Update()
    {
        if (Mathf.Abs((transform.position - initialPosition).magnitude) >= bobbingHeight)
            bobbingSpeed *= -1;
        transform.Translate(0.0f, bobbingSpeed * Time.deltaTime, 0.0f);
    }

    void OnTriggerEnter(Collider other)
    {
        if ((playerLayer.value & 1 << other.gameObject.layer) > 0)
        {
            playerShoot.addAmmo(1);
            Destroy(gameObject);
            return;
        }
    }
}
