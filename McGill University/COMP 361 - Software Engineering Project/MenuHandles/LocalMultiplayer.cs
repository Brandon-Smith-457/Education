using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;
using Enums;

public class LocalMultiplayer : MonoBehaviour
{
    // Start is called before the first frame update
    private int playerNum;
    private int gameBoardIndex = 8;

    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    public void startGameWith()
    {
        GameObject player = null;

        playerNum = (int)GameObject.Find("Player Number").GetComponent<Slider>().value;
        for (int i = 0; i < playerNum; i++)
        {
            //player = Resources.Load<GameObject>("Prefabs/Player");
            //GameObject instantiatedPlayer = GameObject.Instantiate<GameObject>(player);
            //instantiatedPlayer.GetComponent<Player>().Initialize("Player" + (i + 1), PlayerStatus.AVAILABLE);
        }
        SceneManager.LoadScene(gameBoardIndex);
    }
}
