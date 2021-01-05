using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BulletBehaviour : MonoBehaviour
{
    public float speed = 60.0f;

    Vector3 velocity;
    Vector3 startingPosition;
    Vector3 previousPosition;
    // Start is called before the first frame update
    void Start()
    {
        velocity = gameObject.transform.forward * speed;
        startingPosition = transform.position;
    }

    // Update is called once per frame
    void Update()
    {
        previousPosition = transform.position;
        gameObject.transform.position += velocity * Time.deltaTime;

        RaycastHit[] raycastHits = Physics.RaycastAll(new Ray(previousPosition, (transform.position - previousPosition).normalized), (transform.position - previousPosition).magnitude);

        for (int i = 0; i < raycastHits.Length; /*no need to increment because the bullet gets destroyed on first hit*/)
        {
            Collider other = raycastHits[i].collider;
            Debug.Log("BulletHitSomething");
            if (other.gameObject.tag == "BulletTarget")
            {
                Debug.Log("BulletHitABulletTarget");
                Destroy(other.gameObject);
                Destroy(gameObject);
            }
            else
            {
                Destroy(gameObject);
            }
            return;
        }

        if (Mathf.Abs(startingPosition.magnitude - transform.position.magnitude) > 120.0f)
            Destroy(gameObject);
    }
}
