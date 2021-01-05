using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine;

public class PlayerState : MonoBehaviour
{
    public Text shieldGUIText;

    private bool started = false;
    private bool hasTreasure = false;

    private int healthPoint = 2;
    private float shieldPoints = 10.0f;
    private bool shieldActive = false;

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Space))
        {
            shieldActive = (shieldPoints > 0.0f) ? !shieldActive : false;
        }
        if (shieldActive)
        {
            shieldPoints -= 1.0f * Time.deltaTime;
            if (shieldPoints <= 0.0f)
            {
                shieldActive = false;
            }
        }
        string shieldMsg;
        if (shieldActive)
        {
            shieldMsg = "Shield is On: " + shieldPoints + "s remaining";
        }
        else
        {
            shieldMsg = (shieldPoints > 0) ? "Shield is Off: " + shieldPoints + "s remaining" : "Shield is out of power!";
        }
        shieldGUIText.text = shieldMsg;
        // Losing state of the game
        if (healthPoint <= 0)
        {
            Debug.Log("Player Lost");
            // TODO: Some kind of victory screen
#if UNITY_EDITOR
            UnityEditor.EditorApplication.isPlaying = false;
#else
            Application.Quit();
#endif
        }
    }

    public bool damagePlayer()
    {
        if (!shieldActive) healthPoint--;
        return healthPoint <= 0;
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.layer == LayerMask.NameToLayer("Treasure"))
        {
            hasTreasure = true;
            GameObject.Destroy(other.gameObject);
        }
        else if (other.gameObject.layer == LayerMask.NameToLayer("PlayerEnter"))
        {
            if (!started)
            {
                // Notify the HTN planner to begin
                started = true;
                Debug.Log("Notify HTN planner to begin");
            }
            if (hasTreasure)
            {
                // Trigger winning state of the game
                Debug.Log("Player Won");
                // TODO: Some kind of victory screen
#if UNITY_EDITOR
                UnityEditor.EditorApplication.isPlaying = false;
#else
                Application.Quit();
#endif
            }
        }
    }
}
