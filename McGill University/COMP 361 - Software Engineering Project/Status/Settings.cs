using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Settings : MonoBehaviour {
    int SCREEN_WIDTH, SCREEN_HEIGHT;
    double VOLUME;

	// Use this for initialization
	void Start () {
        DontDestroyOnLoad(this);
        SCREEN_WIDTH = Screen.width;
        SCREEN_HEIGHT = Screen.height;
        VOLUME = 0.5;
        SceneManager.LoadScene(1);
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void UpdateSystemSettings(int width, int height, double volume)
    {
        this.SCREEN_WIDTH = width;
        this.SCREEN_HEIGHT = height;
        this.VOLUME = volume;
    }

    public int GetCurrentWidth()
    {
        return SCREEN_WIDTH;
    }

    public int GetCurrentHeight()
    {
        return SCREEN_HEIGHT;
    }

    public double GetSystemVolume()
    {
        return VOLUME;
    }
}
