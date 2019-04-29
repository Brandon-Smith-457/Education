using UnityEngine;
using UnityEngine.UI;
using Enums;
using UnityEngine.EventSystems;
using System;

public class Wall : MonoBehaviour, Edge, IPointerClickHandler {
    public WallStatus status;
    Game game;
    Sprite damagedWall;
    Sprite destroyedWall;


	// Use this for initialization
	void Start () {
        game = GameObject.Find("Game").GetComponent<Game>();
        damagedWall = Resources.Load<Sprite>("Sprites/Edges/" + "damaged_wall");
        destroyedWall = Resources.Load<Sprite>("Sprites/Edges/" + "destroyed_wall");
        status = WallStatus.INTACT;
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

        if (GetStatus() != WallStatus.DESTROYED)
        {
            int cost = fireman.determineCost(ActionType.CHOP);
            string msg = string.Format("Chop Wall: {0} AP", cost);
            popup.AddButton(msg, delegate { fireman.ChopWall(this); });
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
        if (status == WallStatus.DESTROYED)
        {
            return true;
        }
        return false;
    }

    public void Damage()
    {
        WallStatus newStatus = status == WallStatus.INTACT ? WallStatus.DAMAGED : WallStatus.DESTROYED;
        game.AddStructDamage();
        SetStatus(newStatus);
    }

    public WallStatus GetStatus()
    {
        return this.status;
    }

    public void SetStatus(WallStatus status)
    {
        this.status = status;
        SetWallAppearance();
    }

    void SetWallAppearance()
    {
        if (status == WallStatus.DAMAGED)
        {
            GetComponent<Image>().sprite = damagedWall;
        }
        else
        {
            GetComponent<Image>().sprite = destroyedWall;
        }
    }
}
