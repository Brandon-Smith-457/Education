using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MouseFirstPerson : MonoBehaviour
{
    public float mouseSensitivity = 100.0f;

    public Transform player;

    float xRot = 0.0f;
    // Start is called before the first frame update
    void Start()
    {
        Cursor.lockState = CursorLockMode.Locked;
    }

    // Update is called once per frame
    void Update()
    {
        float mouseX = Input.GetAxis("Mouse X") * mouseSensitivity * Time.deltaTime;
        float mouseY = Input.GetAxis("Mouse Y") * mouseSensitivity * Time.deltaTime;
        xRot -= mouseY;
        xRot = Mathf.Clamp(xRot, -90f, 90f);

        transform.localRotation = Quaternion.Euler(xRot, 0.0f, 0.0f);
        player.Rotate(Vector3.up * mouseX);
    }
}
