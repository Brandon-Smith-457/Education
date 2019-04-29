using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using Enums;
using UnityEngine.UI;
public class Login : MonoBehaviour {

    // Game object InputField
    public GameObject user;
    public GameObject pass;
    public GameObject responseText;

    //variables we want to keep during the enitre session, like PlayerID and Username, etc.
    public string username;
    private string password;

    // Use this for initialization
    void Start ()
    {

    }
	
	// Update is called once per frame
	void Update ()
    {
        if (Input.GetKeyDown(KeyCode.Return))
        {
            LoginButton(4);
        }
    }

    public void LoginButton(int index)
    {
        //Player localPlayer = GameObject.Find("LocalPlayer").GetComponent<Player>();
        //username = user.GetComponent<InputField>().text;
        //password = pass.GetComponent<InputField>().text;
        //if (!username.Equals("") && !password.Equals(""))
        //{
        //    localPlayer.Initialize(username, PlayerStatus.AVAILABLE);
        //    SceneManager.LoadScene(index);
        //}
        //else responseText.GetComponent<Text>().text = "Please fill all fields";
    }

    public void BackButton(int index)
    {
        SceneManager.LoadScene(index);
    }
}
