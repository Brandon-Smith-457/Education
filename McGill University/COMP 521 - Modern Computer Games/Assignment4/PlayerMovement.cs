using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMovement : MonoBehaviour
{
    public CharacterController controller;

    private float speed = 5.0f;
    private float gravity = -9.81f;

    private Vector3 velocity;

    // Update is called once per frame
    void Update()
    {
        if (controller.isGrounded && velocity.y < 0)
        {
            velocity.y = -0.2f;
        }

        float x = Input.GetAxis("Horizontal");
        float z = Input.GetAxis("Vertical");

        Vector3 move = (transform.right * x + transform.forward * z) * speed * Time.deltaTime;
        controller.Move(move);

        velocity.y += gravity * Time.deltaTime;
        controller.Move(velocity * Time.deltaTime);
    }
}
