using Random = System.Random;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Enums;
using Photon.Pun;

public class Victim : MonoBehaviourPun, POI {
    POIstatus status;

    public void SetLocation(Cell cell)
    {
        gameObject.transform.SetParent(cell.GetGameObjectsContainer().transform, false);
        int[] coords = { (int)cell.coords.x, (int)cell.coords.y };
        photonView.RPC("RPCSetVictimLocation", RpcTarget.Others, coords);
    }

    public POI Reveal(Cell cell)
    {
        Random rand = new Random();
        int victimNumber = rand.Next(12) + 1;
        gameObject.GetComponent<Image>().sprite = Resources.Load<Sprite>("Sprites/Points_of_Interest/Victims/Victim_" + victimNumber);
        photonView.RPC("RPCReveal", RpcTarget.Others, victimNumber);
        SetStatus(POIstatus.REVEALED);
        return null;
    }

    public POI Remove(Cell cell)
    {
        Game.Instance.DeactivatePOI(this);
        SetStatus(POIstatus.REMOVED);
        return null;
    }

    public void SetStatus(POIstatus status)
    {
        this.status = status;
        if (this.status == POIstatus.REMOVED)
        {
            gameObject.SetActive(false);
        }
    }

    public POIstatus GetStatus()
    {
        return this.status;
    }

    public void Rescue()
    {
        Debug.Log("Victim was rescued!");
        Game.Instance.AddVictimRescued();
        Remove(null);
        photonView.RPC("RPCRescue", RpcTarget.Others);
    }

    public void Kill()
    {
        Game.Instance.AddVictimLost();
        Remove(null);
        photonView.RPC("RPCKill", RpcTarget.Others);
    }

    [PunRPC]
    public void RPCSetVictimLocation(int[] coords)
    {
        Cell newCell = null;
        Game.Instance.GetCell(coords[0], coords[1], ref newCell);
        gameObject.transform.SetParent(newCell.GetGameObjectsContainer().transform, false);
    }

    [PunRPC]
    public void RPCReveal(int victimNumber)
    {
        gameObject.GetComponent<Image>().sprite = Resources.Load<Sprite>("Sprites/Points_of_Interest/Victims/Victim_" + victimNumber);
        SetStatus(POIstatus.REVEALED);
    }

    [PunRPC]
    public void RPCRescue()
    {
        Game.Instance.AddVictimRescued();
        Remove(null);
    }

    [PunRPC]
    public void RPCKill()
    {
        Game.Instance.AddVictimLost();
        Remove(null);
    }
}
