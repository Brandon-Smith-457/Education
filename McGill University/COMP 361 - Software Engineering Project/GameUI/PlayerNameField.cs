using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Photon.Pun;

[RequireComponent(typeof(InputField))]
public class PlayerNameField : MonoBehaviour
{
    [SerializeField]
    private GameObject connectButton;
    //key for the player's previous name in PlayerPrefs
    const string playerNamePrefKey = "PlayerName";
    // Start is called before the first frame update
    void Start()
    {
        string defaultName = string.Empty;
        InputField _inputField = this.GetComponent<InputField>();

        if (_inputField != null)
        {
            if (PlayerPrefs.HasKey(playerNamePrefKey))
            {
                defaultName = PlayerPrefs.GetString(playerNamePrefKey);
                _inputField.text = defaultName;
            }
        }
        
        PhotonNetwork.NickName = defaultName;
        connectButton.GetComponent<Button>().enabled = true;
    }

    public void SetPlayerName(string value) 
    {
        if (string.IsNullOrEmpty(value))
        {
            Debug.LogError("Cannot have an empty player name");
            connectButton.GetComponent<Button>().enabled = false;
            return;
        }

        PhotonNetwork.NickName = value;
        connectButton.GetComponent<Button>().enabled = true;
        Debug.Log("Your name is now " + PhotonNetwork.NickName);
        PlayerPrefs.SetString(playerNamePrefKey, value);
    }
}
