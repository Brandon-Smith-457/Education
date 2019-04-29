using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Photon.Realtime;
using Photon.Pun;
using ExitGames.Client.Photon;
using UnityEngine.UI;

public class GameLobbyManager : MonoBehaviourPunCallbacks
{
    public GameObject playerEntryPrefab;
    public GameObject playerList;
    private Dictionary<int, GameObject> playerIdToPlayer;


    // Start is called before the first frame update
    void Start()
    {
        playerIdToPlayer = new Dictionary<int, GameObject>();
    }

    public override void OnRoomPropertiesUpdate(ExitGames.Client.Photon.Hashtable propertiesThatChanged)
    {

    }

    public override void OnPlayerEnteredRoom(Player newPlayer)
    {
        AddPlayerEntry(newPlayer);
    }

    public override void OnPlayerLeftRoom(Player otherPlayer)
    {
        //delete the right thing from the dictionary
        Destroy(playerIdToPlayer[otherPlayer.ActorNumber]);
        playerIdToPlayer.Remove(otherPlayer.ActorNumber);
    }

    //populate the list with players already in the room
    public override void OnJoinedRoom()
    {
        Player[] players = PhotonNetwork.PlayerList;
        Debug.Log("There are " + players.Length + " players in the room");
        foreach (Player player in players)
        {
            AddPlayerEntry(player);
        }
    }

    public override void OnLeftRoom()
    {
        PhotonNetwork.LoadLevel("NewLauncher");
    }

    public void LeaveGame()
    {
        if(PhotonNetwork.IsMasterClient)
        {
            PhotonNetwork.CurrentRoom.IsVisible = false;
        }

        PhotonNetwork.LeaveRoom();
    }

    public void StartGame()
    {
        if (!PhotonNetwork.LocalPlayer.IsMasterClient)
        {
            return;
        }

        PhotonNetwork.LoadLevel("PreSetGameBoard1");
    }

    private void AddPlayerEntry(Player newPlayer)
    {
        if (newPlayer == null)
        {
            Debug.Log("tried to add a null player");
            return;
        }
        Debug.Log("adding to player list");
        //instantiate something for them
        GameObject playerEntry = Instantiate(playerEntryPrefab, playerList.transform);
        playerEntry.transform.GetChild(0).GetComponent<Text>().text = newPlayer.NickName;
        //put that thing in the dictionary
        playerIdToPlayer.Add(newPlayer.ActorNumber, playerEntry);
    }
}
