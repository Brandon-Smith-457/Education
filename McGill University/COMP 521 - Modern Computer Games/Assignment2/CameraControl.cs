using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraControl : MonoBehaviour
{
    void Start()
    {
        // Just want to work in the coordinates of 0,0 in the bottom left so shifting the camera as such
        Camera cam = gameObject.GetComponent<Camera>();
        transform.position = new Vector3(cam.aspect * cam.orthographicSize, cam.orthographicSize, transform.position.z);
    }
}
