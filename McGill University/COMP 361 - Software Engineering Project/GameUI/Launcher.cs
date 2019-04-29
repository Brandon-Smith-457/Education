using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Photon.Pun;
using Photon.Realtime;
using Enums;

public class Launcher : MonoBehaviourPunCallbacks
{
    [SerializeField]
    private GameObject connectPanel;
    [SerializeField]
    private GameObject joinHostPanel;
    [SerializeField]
    private GameObject gameList;

    public GameObject gameEntryPrefab;
    private List<GameEntry> games;
    private RoomInfo selectedGame;

    bool isConnecting;

    void Awake()
    {
        PhotonNetwork.AutomaticallySyncScene = true;
        games = new List<GameEntry>();

        if (!PhotonNetwork.IsConnected)
        {
            connectPanel.SetActive(true);
            joinHostPanel.SetActive(false);
        }
        else
        {
            connectPanel.SetActive(false);
            joinHostPanel.SetActive(true);
        }
    }

    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    #region Pun callbacks

    public override void OnDisconnected(DisconnectCause cause)
    {
        connectPanel.SetActive(true);
        joinHostPanel.SetActive(false);
        Debug.LogWarningFormat("Disconnected from Photon with reason {0}", cause);
    }

    public override void OnConnectedToMaster()
    {
        //if we're not connecting, we've just left a game room
        if (!isConnecting)
        {
            PhotonNetwork.LeaveLobby();
        }
        Debug.Log("Connection successful, joining lobby");
        PhotonNetwork.JoinLobby();
    }

    public override void OnRoomListUpdate(List<RoomInfo> roomList)
    {
        foreach (Transform child in gameList.transform)
        {
            Destroy(child.gameObject);
        }

        games.Clear();

        foreach (RoomInfo room in roomList)
        {
            Debug.Log("Adding a game called " + room.Name + " to the gameList");
            AddGameEntry(room);
        }
    }

    public override void OnJoinedLobby()
    {
        Debug.Log(PhotonNetwork.CurrentLobby.IsDefault);
        joinHostPanel.SetActive(true);
        games.Clear();
    }

    #endregion

    #region Button methods

    public void Connect()
    {
        isConnecting = true;
        connectPanel.SetActive(false);

        if (!PhotonNetwork.IsConnected)
        {
            PhotonNetwork.PhotonServerSettings.AppSettings.FixedRegion = "cae";
            PhotonNetwork.ConnectUsingSettings();
        }
    }

    public void SetSelectedGame(RoomInfo game)
    {
        selectedGame = game;
    }

    public void JoinSelectedGame()
    {
        if (selectedGame == null)
        {
            Debug.Log("Trying to join a game without selecting one first");
            return;
        }

        PhotonNetwork.JoinRoom(selectedGame.Name);
        PhotonNetwork.LoadLevel("NewGameLobby");
    }

    public void HostGame()
    {
        RoomOptions options = new RoomOptions
        {
            IsOpen = true,
            IsVisible = true,
            MaxPlayers = 6,
            EmptyRoomTtl = 0
        };

        options.CustomRoomPropertiesForLobby = new string[] { "ruleset", "hasPassword" };
        options.CustomRoomProperties = new ExitGames.Client.Photon.Hashtable()
        {
            {"ruleset", Ruleset.Family},
            {"hasPassword", false}
        };

        PhotonNetwork.CreateRoom(PhotonNetwork.NickName + "\'s game", options);
        PhotonNetwork.LoadLevel("NewGameLobby");
    }

    #endregion

    #region private methods

    private void AddGameEntry(RoomInfo newRoom)
    {
        if (newRoom == null || !newRoom.IsVisible)
        {
            Debug.Log("Stop fucking around");
            return;
        }
        GameObject gameEntryUi = Instantiate(gameEntryPrefab, gameList.transform);
        GameEntry gameEntry = gameEntryUi.GetComponent<GameEntry>();
        gameEntry.launcher = this;
        gameEntry.LoadGameEntry(newRoom);
        games.Add(gameEntry);
    }

    #endregion
}
