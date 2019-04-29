using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using Enums;

public class GameLobby : MonoBehaviour {
    Ruleset ruleSet;
    BoardType boardType;
    int numPlayers;

	// Use this for initialization
	void Start () {
        numPlayers = 0;
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void StartGameButton()
    {
        DontDestroyOnLoad(this);
        ruleSet = Ruleset.Family;
        foreach (GameObject player in GameObject.FindGameObjectsWithTag("Player"))
        {
            numPlayers++;
        }
        boardType = BoardType.PRE_SET_1;
        if (boardType == BoardType.PRE_SET_1)
        {
            SceneManager.LoadScene(9);
        }
        else if (boardType == BoardType.PRE_SET_2)
        {
            SceneManager.LoadScene(10);
        }
        else if (boardType == BoardType.RANDOM)
        {
            SceneManager.LoadScene(11);
        }
    }

    public void SystemSettingsButton(int index)
    {
        SceneManager.LoadScene(index);
    }

    public void LeaveGameButton(int index)
    {
        SceneManager.LoadScene(index);
    }

    public void Destroy()
    {
        Destroy(gameObject);
    }

    public Ruleset GetRuleSet()
    {
        return this.ruleSet;
    }

    public int GetNumPlayers()
    {
        return this.numPlayers;
    }
}
