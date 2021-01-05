using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CanonBehaviour : MonoBehaviour
{
    public static float muzzleSpeed = 1.0f;
    public float muzzleSpeedRateOfChange = 10.0f;

    public float rotationSpeed = 60.0f;
    // An integer to help with dealing with the maths of the flipped canon
    public int sign;

    public float spriteWidth;
    public float spriteHeight;

    private Text muzzleSpeedText;
    
    // A bool that works to determine which canon is currently "active"
    private bool selected;

    // Start is called before the first frame update
    void Start()
    {
        SpriteRenderer sr = GetComponent<SpriteRenderer>();
        // Sign is negative if we're flipped
        sign = sr.flipX ? -1 : 1;
        // Select the non-flipped canon (left canon)
        selected = !sr.flipX;
        spriteWidth = sr.bounds.size.x;
        spriteHeight = sr.bounds.size.y;

        muzzleSpeedText = GameObject.Find("MuzzleSpeed").GetComponent<Text>();
        muzzleSpeedText.text = "Muzzle Velocity: " + muzzleSpeed + "units/s";
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Tab)) selected = !selected;
        if (!selected) return;
        // The check for the angle being greater than 90degrees for the flipped canon is because Unity doesn't store negative angles
        if (Input.GetKey(KeyCode.UpArrow))
        {
            float rotation = 0;
            if (transform.rotation.eulerAngles.z > 90 && sign < 0)
                rotation = Mathf.Clamp((transform.rotation.eulerAngles.z - 360) + rotationSpeed * sign * Time.deltaTime, -90.0f, 0.0f);
            else if (sign < 0)
                rotation = Mathf.Clamp(transform.rotation.eulerAngles.z + rotationSpeed * sign * Time.deltaTime, -90.0f, 0.0f);
            else
                rotation = Mathf.Clamp(transform.rotation.eulerAngles.z + rotationSpeed * sign * Time.deltaTime, 0.0f, 90.0f);
            transform.rotation = Quaternion.Euler(0.0f, 0.0f, rotation);
        }
        else if (Input.GetKey(KeyCode.DownArrow))
        {
            float rotation = 0;
            if (transform.rotation.eulerAngles.z > 90 && sign < 0)
                rotation = Mathf.Clamp((transform.rotation.eulerAngles.z - 360) - rotationSpeed * sign * Time.deltaTime, -90.0f, 0.0f);
            else if (sign < 0)
                rotation = Mathf.Clamp(transform.rotation.eulerAngles.z - rotationSpeed * sign * Time.deltaTime, -90.0f, 0.0f);
            else
                rotation = Mathf.Clamp(transform.rotation.eulerAngles.z - rotationSpeed * sign * Time.deltaTime, 0.0f, 90.0f);
            transform.rotation = Quaternion.Euler(0.0f, 0.0f, rotation);
        }

        if (Input.GetKey(KeyCode.RightArrow))
        {
            muzzleSpeed = Mathf.Clamp(muzzleSpeed + muzzleSpeedRateOfChange * Time.deltaTime, 0.0f, int.MaxValue);
            muzzleSpeedText.text = "Muzzle Velocity: " + muzzleSpeed + "units/s";
        }
        else if (Input.GetKey(KeyCode.LeftArrow))
        {
            muzzleSpeed = Mathf.Clamp(muzzleSpeed - muzzleSpeedRateOfChange * Time.deltaTime, 0.0f, int.MaxValue);
            muzzleSpeedText.text = "Muzzle Velocity: " + muzzleSpeed + "units/s";
        }

        if (Input.GetKeyDown(KeyCode.Space))
        {
            float theta = transform.rotation.eulerAngles.z;
            theta = theta * Mathf.PI / 180;
            Vector3 rotatedCanonBallSpawnPos;
            Vector3 rotatedMuzzleVelocity;
            if (sign < 0)
            {
                theta = (theta - 2 * Mathf.PI) * -1.0f;
                rotatedCanonBallSpawnPos = new Vector3(transform.position.x - spriteWidth * Mathf.Cos(theta), transform.position.y + spriteWidth * Mathf.Sin(theta), 0);
                rotatedMuzzleVelocity = new Vector3(muzzleSpeed * Mathf.Cos(theta) * -1.0f, muzzleSpeed * Mathf.Sin(theta), 0);
            }
            else
            {
                rotatedCanonBallSpawnPos = new Vector3(transform.position.x + spriteWidth * Mathf.Cos(theta), transform.position.y + spriteWidth * Mathf.Sin(theta), 0);
                rotatedMuzzleVelocity = new Vector3(muzzleSpeed * Mathf.Cos(theta), muzzleSpeed * Mathf.Sin(theta), 0);
            }
            AssetManager.instance.spawnCanonBall(rotatedCanonBallSpawnPos, rotatedMuzzleVelocity);
        }
    }
}
