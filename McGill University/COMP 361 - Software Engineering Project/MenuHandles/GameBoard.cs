using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using Photon.Pun;

public class GameBoard : MonoBehaviour {

    // Use this for initialization
    void Start () {

    }

    // Update is called once per frame
    void Update () {
		
	}

    public void ResumeGameButton()
    {
        //Most likely will never be used
    }

    public void SystemSettingsButton(int index)
    {
        //This will have to be changed significantly
        SceneManager.LoadScene(index);
    }

    public void LeaveGameButton(int index)
    {
        PhotonNetwork.LeaveRoom();
    }
}
