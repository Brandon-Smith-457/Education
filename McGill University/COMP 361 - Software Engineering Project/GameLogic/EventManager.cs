using UnityEngine;
using System.Collections;
using Photon.Realtime;
using ExitGames.Client.Photon;
using Photon.Pun;
using Enums;

public class EventManager : MonoBehaviour, IOnEventCallback
{
    public static EventManager i = null;
    // Use this for initialization
    void Start()
    {
        i = this;
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void OnEvent(EventData photonEvent)
    {
        byte eventCode = photonEvent.Code;
        object[] data = null;
        Cell cell = null;
        int viewId = -1;

        switch (eventCode)
        {
            case GameEvents.IncrementTurn:
                Game.Instance.IncrementTurn();
                if (Game.Instance.IsLocalPlayersTurn())
                {
                    Player player = Game.Instance.GetCurrentPlayer();
                    foreach (POI poi in Game.Instance.activePOIs)
                    {
                        ((MonoBehaviourPun)poi).photonView.TransferOwnership(player.ActorNumber);
                    }
                }
                break;
            case GameEvents.FindFiremanAndAssociate:
                //first arg is player's ActorNumber
                //second arg is fireman GameObject's viewID
                data = (object[])photonEvent.CustomData;
                int actorNumber = (int)data[0];
                viewId = (int)data[1];
                GameObject firemanGo = PhotonView.Find(viewId).gameObject;
                FireMan fireman = firemanGo.GetComponent<FireMan>();
                Game.Instance.AddFiremanAndAssociate(fireman, actorNumber);
                break;
            case GameEvents.SetCellStatus:
                data = (object[])photonEvent.CustomData;

                Game.Instance.GetCell((int)data[0], (int)data[1], ref cell);

                CellStatus status = (CellStatus)data[2];
                cell.SetStatus(status);
                break;
            case GameEvents.DamageWall:
                data = (object[])photonEvent.CustomData;

                Game.Instance.GetCell((int)data[0], (int)data[1], ref cell);

                Wall wall = (Wall)cell.GetEdge((Direction)data[2]);

                wall.Damage();
                break;
            case GameEvents.SetDoorStatus:
                data = (object[])photonEvent.CustomData;

                Game.Instance.GetCell((int)data[0], (int)data[1], ref cell);

                Door door = (Door)cell.GetEdge((Direction)data[2]);

                door.SetStatus((DoorStatus)data[3]);
                break;
            case GameEvents.FindPOI:
                data = (object[])photonEvent.CustomData;
                viewId = (int)data[0];
                GameObject poiGo = PhotonView.Find(viewId).gameObject;
                if (poiGo.GetComponent<Victim>() != null)
                {
                    Game.Instance.inactivePOIs.Add(poiGo.GetComponent<Victim>());
                }
                else
                {
                    Game.Instance.inactivePOIs.Add(poiGo.GetComponent<FalseAlarm>());
                }
                break;
            case GameEvents.ActivatePOIEvent:
                data = (object[])photonEvent.CustomData;
                int index = (int)data[0];

                if (index > Game.Instance.inactivePOIs.Count)
                {
                    Debug.LogError("it seems that the inactivePOIs arraylist is out of sync");
                    index = 0;
                }
                Game.Instance.GetCell((int)data[1], (int)data[2], ref cell);
                Game.Instance.ActivatePOI(index, cell);
                break;
            case GameEvents.TransferOwnership:

                break;
            default:
                Debug.Log("event code not implemented: " + eventCode);
                break;

        }

    }

    public void OnEnable()
    {
        PhotonNetwork.AddCallbackTarget(this);
    }

    public void OnDisable()
    {
        PhotonNetwork.RemoveCallbackTarget(this);
    }

    #region Events

    //tells all clients to increment turn
    public void IncrementTurnEvent()
    {
        PhotonNetwork.RaiseEvent(GameEvents.IncrementTurn,
                                        null,
                                        new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                        new SendOptions { Reliability = true });
    }

    //tells all clients to change the cell's status
    public void SetCellStatusEvent(Cell cell, CellStatus newStatus)
    {
        cell.SetStatus(newStatus);
        int x = (int)cell.coords.x;
        int y = (int)cell.coords.y;
        PhotonNetwork.RaiseEvent(GameEvents.SetCellStatus,
                                    new object[] { x, y, newStatus },
                                    new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                    new SendOptions { Reliability = true });
    }

    public void FindFiremanAndAssociateEvent(GameObject firemanGo, Player player) {
        PhotonNetwork.RaiseEvent(GameEvents.FindFiremanAndAssociate,
                                            new object[] { player.ActorNumber, firemanGo.GetPhotonView().ViewID },
                                            new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                            new SendOptions { Reliability = true });
    }

    public void DamageWallEvent(Cell cell, Direction dir)
    {
        Edge wall = cell.GetEdge(dir);

        if (!(typeof(Wall).IsInstanceOfType(wall)))
        {
            Debug.LogError("We are supposed to be damaging a wall but we got something else");
            Debug.LogError("Cell coords are" + cell.coords.ToString() + " direction is " + dir.ToString());
            Debug.LogError("Is Empty: " + (typeof(Empty).IsInstanceOfType(wall)) + " Is Door: " + (typeof(Door).IsInstanceOfType(wall)) + " Is Wall: " + (typeof(Wall).IsInstanceOfType(wall)));
        }
        
        if (wall == null)
        {
            return;
        }

        ((Wall)wall).Damage();
        
        int x = (int)cell.coords.x;
        int y = (int)cell.coords.y;
        PhotonNetwork.RaiseEvent(GameEvents.DamageWall,
                                    new object[] { x, y, dir },
                                    new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                    new SendOptions { Reliability = true });
    }

    public void SetDoorStatusEvent(Cell cell, Direction dir, DoorStatus status)
    {
        Door door = (Door)cell.GetEdge(dir);

        door.SetStatus(status);

        int x = (int)cell.coords.x;
        int y = (int)cell.coords.y;
        PhotonNetwork.RaiseEvent(GameEvents.SetDoorStatus,
                                    new object[] { x, y, dir, status },
                                    new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                    new SendOptions { Reliability = true });
    }

    public void FindPOI(MonoBehaviourPun poi)
    {
        PhotonNetwork.RaiseEvent(GameEvents.FindPOI,
                                            new object[] { poi.photonView.ViewID },
                                            new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                            new SendOptions { Reliability = true });
    }

    public void ActivatePOIEvent(int index, Cell cell)
    {
        int x = (int)cell.coords.x;
        int y = (int)cell.coords.y;
        PhotonNetwork.RaiseEvent(GameEvents.ActivatePOIEvent,
                                            new object[] { index, x, y },
                                            new RaiseEventOptions { Receivers = ReceiverGroup.Others },
                                            new SendOptions { Reliability = true });
    }


    #endregion

    #region helper methods


    #endregion
}
