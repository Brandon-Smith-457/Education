using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using Enums;
using Photon.Pun;

public class FalseAlarm : MonoBehaviourPun, POI {
    POIstatus status;

    public void SetLocation(Cell cell)
    {
        gameObject.transform.SetParent(cell.GetGameObjectsContainer().transform, false);
        int[] coords = { (int)cell.coords.x, (int)cell.coords.y };
        photonView.RPC("RPCSetFaLocation", RpcTarget.Others, coords);
    }

    public POI Reveal(Cell cell)
    {
        gameObject.GetComponent<Image>().sprite = Resources.Load<Sprite>("Sprites/Points_of_Interest/POI_False_Alarm");
        SetStatus(POIstatus.REVEALED);
        photonView.RPC("RPCReveal", RpcTarget.Others);
        return Remove(cell);
    }

    public POI Remove(Cell cell)
    {
        Game.Instance.DeactivatePOI(this);
        SetStatus(POIstatus.REMOVED);
        return this;
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
        return status;
    }

    [PunRPC]
    public void RPCSetFaLocation(int[] coords)
    {
        Cell newCell = null;
        Game.Instance.GetCell(coords[0], coords[1], ref newCell);
        gameObject.transform.SetParent(newCell.GetGameObjectsContainer().transform, false);
    }

    [PunRPC]
    public void RPCReveal()
    {
        gameObject.GetComponent<Image>().sprite = Resources.Load<Sprite>("Sprites/Points_of_Interest/POI_False_Alarm");
        SetStatus(POIstatus.REVEALED);
        Remove(null);
    }
}
