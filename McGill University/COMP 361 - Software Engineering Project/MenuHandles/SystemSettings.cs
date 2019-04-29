using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class SystemSettings : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void SaveButton(int index)
    {
        Settings settings = GameObject.Find("SystemSettings").GetComponent<Settings>();
        settings.UpdateSystemSettings(960, 540, 0.6);
        SceneManager.LoadScene(index);
    }

    public void CancelButton(int index)
    {
        SceneManager.LoadScene(index);
    }
}
