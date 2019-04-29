using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class MainMenu : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void PlayGameButton(int index)
    {
        SceneManager.LoadScene(index);
    }

    public void TutorialButton(int index)
    {
        SceneManager.LoadScene(index);
    }

    public void SystemSettingsButton(int index)
    {
        SceneManager.LoadScene(index);
    }

    public void LogoutButton(int index)
    {
        //Player localPlayer = GameObject.Find("LocalPlayer").GetComponent<Player>();
        //localPlayer.Destroy();
        //SceneManager.LoadScene(index);
    }

    public void QuitButton()
    {
        Application.Quit();
    }
}
