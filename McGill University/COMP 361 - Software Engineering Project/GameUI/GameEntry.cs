using UnityEngine;
using UnityEngine.UI;
using Photon.Realtime;

public class GameEntry : MonoBehaviour
{
    [SerializeField]
    Text gameName;
    [SerializeField]
    Text ruleset;
    [SerializeField]
    Text numPlayers;
    [SerializeField]
    Text hasPassword;

    public RoomInfo photonGame { get; set; }
    public Launcher launcher { get; set; }

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void LoadGameEntry(RoomInfo newPhotonRoom)
    {
        if (newPhotonRoom == null)
        {
            Debug.LogWarning("Creating a GameEntry from a null RoomInfo");
        }
        photonGame = newPhotonRoom;
        gameName.text = photonGame.Name;
        ruleset.text = photonGame.CustomProperties["ruleset"].ToString();
        numPlayers.text = string.Format("{0}/{1}", photonGame.PlayerCount, photonGame.MaxPlayers);
        hasPassword.text = photonGame.CustomProperties["hasPassword"].ToString();
    }

    public void Select()
    {
        launcher.SetSelectedGame(photonGame);
    }


}
