using UnityEngine;
using UnityEngine.UI;
using Enums;
using UnityEngine.EventSystems;
using System;
using UnityEngine.Events;

public class Door : MonoBehaviour, Edge, IPointerClickHandler
{
    public DoorStatus status;
    private Game game;
    GameObject doorHinge;
    Sprite destroyedDoor;

	// Use this for initialization
	void Start () {
        game = GameObject.Find("Game").GetComponent<Game>();
        doorHinge = transform.Find("DoorHinge").gameObject;
        destroyedDoor = Resources.Load<Sprite>("Sprites/Edges/" + "destroyed_door");
        if (destroyedDoor == null)
        {
            Debug.Log("problem with loading door sprite");
        }
        //this is okay here because it's within the start method
        SetStatus(DoorStatus.OPEN);
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    public void OnPointerClick(PointerEventData eventData)
    {
        RectTransform clicked = (RectTransform)transform;

        if (eventData.button == PointerEventData.InputButton.Left || eventData.button == PointerEventData.InputButton.Right)
        {
            //this is gross
            bool playerIsAdjacent = false;
            Cell playerCell = game.GetLocalFireman().GetLocation();
            foreach (Direction dir in Enum.GetValues(typeof(Direction)))
            {
                if (playerCell.GetEdge(dir) == this)
                {
                    playerIsAdjacent = true;
                    break;
                }
            }

            if (!game.escapeMenu.activeSelf && playerIsAdjacent)
            {
                MakePopupPanel();
            }
        }
    }

    public void MakePopupPanel()
    {
        if (!game.IsLocalPlayersTurn())
        {
            return;
        }

        PopupManager popup = PopupManager.Instance;

        //This is going off the assumption that we currently only have one player at a time
        FireMan fireman = game.GetLocalFireman();

        if (GetStatus() == DoorStatus.OPEN)
        {
            int cost = fireman.determineCost(ActionType.TOGGLE_DOOR);
            string msg = string.Format("Close Door: {0} AP", cost);
            popup.AddButton(msg, delegate { fireman.ToggleDoor(this); });
        }
        else if (GetStatus() == DoorStatus.CLOSED)
        {
            int cost = fireman.determineCost(ActionType.TOGGLE_DOOR);
            string msg = string.Format("Open Door: {0} AP", cost);
            popup.AddButton(msg, delegate { fireman.ToggleDoor(this); });
        }
        Vector2 position = gameObject.GetComponent<RectTransform>().anchoredPosition;

        //bad Sarah
        Vector3 rotation = gameObject.GetComponent<RectTransform>().rotation.eulerAngles;
        //if the edge is vertical, offset the popup by less
        if (rotation.Equals(Vector3.zero))
        {
            position.x += 160;
        }
        else
        {
            position.x += 320;
        }

        popup.SetPositionAndShow(position);
    }

    public bool IsTraversable()
    {
        if (status == DoorStatus.DESTROYED || status == DoorStatus.OPEN)
        {
            return true;
        }
        return false;
    }

    public void Toggle()
    {
        if (status != DoorStatus.DESTROYED)
        {
            status = (status == DoorStatus.CLOSED) ? DoorStatus.OPEN : DoorStatus.CLOSED;
        }

        SetDoorHingeAppearance();
    }

    public DoorStatus GetStatus()
    {
        return this.status;
    }

    public void SetStatus(DoorStatus status)
    {
        this.status = status;
        SetDoorHingeAppearance();
    }

    void SetDoorHingeAppearance()
    {
        RectTransform rt = doorHinge.GetComponent<RectTransform>();
        switch (status)
        {
            case DoorStatus.OPEN:
                rt.Rotate(new Vector3(0f, 0f, 90f));
                break;
            case DoorStatus.CLOSED:
                rt.Rotate(new Vector3(0f, 0f, -90f));
                break;
            case DoorStatus.DESTROYED:
                Image img = doorHinge.GetComponent<Image>();
                //possibly not necessary, but whatever
                if (img.sprite != destroyedDoor)
                {
                    img.sprite = destroyedDoor;
                }
                break;
        }
    }
}