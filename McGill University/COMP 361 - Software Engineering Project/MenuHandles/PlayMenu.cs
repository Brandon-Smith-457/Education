using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class PlayMenu : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void BackButton(int index)
    {
        SceneManager.LoadScene(index);
    }

    public void JoinButton(int index)
    {
        SceneManager.LoadScene(index);
    }
}
