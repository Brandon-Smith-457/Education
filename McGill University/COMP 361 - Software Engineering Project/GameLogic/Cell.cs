using System.Collections;
using System.Collections.Generic;
using Enums;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;
using Photon.Realtime;
using Photon.Pun;
using System;

public class Cell : MonoBehaviour, IPointerClickHandler
{
    public CellKind kind;
    public CellStatus status;
    public ParkingKind parkingKind;

    private Game game; //Game variable to call gameLogic functions
    public RectTransform transformCell; //This is the x and y for PIXELS (pre-initiated by the gameObject it's attached to
    public Vector2 coords;
    private ArrayList pois = new ArrayList();
    
    //Fire/Smoke
    private GameObject smokeFire;
    public Sprite smokeSprite;
    public Sprite fireSprite;

    private Dictionary<Direction, Cell> adjacents = new Dictionary<Direction, Cell>();
    private Dictionary<Direction, Edge> edges = new Dictionary<Direction, Edge>();
    private Cell closestAmbulance;

    private GameObject gameObjectsContainer;

    // Use this for initialization
    void Start()
    {
        // initialize the Cell's transform and get the existing Game Object.
        this.transformCell = gameObject.GetComponent<RectTransform>();
        game = Game.Instance;

        if (this.parkingKind == ParkingKind.AMBULANCE) closestAmbulance = this;
        else
        {
            Cell min = null;
            double minDist = 12.0;
            foreach (Cell ambulance in game.GetAmbulanceSpots())
            {
                double x2 = System.Math.Pow(ambulance.GetCoordinates().x - this.coords.x, 2.0);
                double y2 = System.Math.Pow(ambulance.GetCoordinates().y - this.coords.y, 2.0);
                double h = System.Math.Sqrt(x2 + y2);
                if (h < minDist)
                {
                    minDist = h;
                    min = ambulance;
                }
            }
            closestAmbulance = min;
        }
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void Init(Vector2 coords, CellKind kind, ParkingKind parkingKind, GameObject gameObjectsContainer) {
        this.coords = coords;
        this.kind = kind;
        this.parkingKind = parkingKind;
        this.gameObjectsContainer = gameObjectsContainer;
    }

    public void OnPointerClick(PointerEventData eventData)
    {
        //IPointerClickHandler for left and right clicks to only call MoveFireFighter if we click a cell.
        if (eventData.button == PointerEventData.InputButton.Left || eventData.button == PointerEventData.InputButton.Right)
        {
            if (!game.escapeMenu.activeSelf)
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
        Vector2 position = GetTransform().anchoredPosition;
        position.x += 160 + 150;
        position.y += 840 + 10;

        //This is going off the assumption that we currently only have one player at a time
        FireMan fireman = game.GetLocalFireman();
        if (fireman == null)
        {
            Debug.Log("fireman is null!");
        }

        if (game.status == GameStatus.MAIN_GAME)
        {
            foreach (POI poi in pois)
            {
                if (poi.GetStatus() == POIstatus.REVEALED && typeof(Victim).IsInstanceOfType(poi) && fireman.GetVictim() == null && fireman.GetLocation() == this)
                {
                    popup.AddButton("Pickup Victim: 0 AP", delegate { fireman.Pickup((Victim)pois[0]); });
                }
            }

            if (fireman.GetVictim() != null && fireman.GetLocation() == this)
            {
                popup.AddButton("Drop Victim: 0 AP", delegate { fireman.Drop(); });
            }

            if (this.status == CellStatus.FIRE)
            {
                int cost1 = fireman.determineCost(ActionType.EXTINGUISH);
                string msg1 = string.Format("Putout Fire: {0} AP", cost1);
                popup.AddButton(msg1, delegate { fireman.ExtinguishFire(this); });
            }

            else
            {
                int cost = game.PathFinder(this);
                string msg = string.Format("Move: {0} AP", cost);
                popup.AddButton(msg, delegate { fireman.Move(this); });
            }

            if (this.status == CellStatus.SMOKE)
            {
                int cost1 = fireman.determineCost(ActionType.EXTINGUISH);
                string msg1 = string.Format("Putout Smoke: {0} AP", cost1);
                popup.AddButton(msg1, delegate { fireman.ExtinguishFire(this); });
            }

            popup.SetPositionAndShow(position);
        }
        else if (game.status == GameStatus.SETUP)
        {
            if (this.kind == CellKind.OUTDOOR)
            {
                popup.AddButton("Select Starting Space", delegate { game.SetFiremanStartingSpace(fireman, this); });
                popup.SetPositionAndShow(position);
            }
            else
            {
                Debug.Log("Please pick an outdoor cell");
            }
        }
    }

    //IPointerEnterHandler for when you mouse over cells to call PathFinder which draws an arrow telling the player if his FireMan has enough AP to move
//    public void OnPointerEnter(PointerEventData eventData)
//   {
//        if (!game.escapeMenu.activeSelf && game.IsLocalPlayersTurn() && game.status == GameStatus.MAIN_GAME)
//        {
            //print(game.GetFireMan(PhotonNetwork.LocalPlayer).GetAP());
            //game.PathFinder(this, localPlayer.GetUsername());
//        }
//    }

    public void SetAdjacent(Direction direction, Cell cell, Edge edge)
    {
        if (adjacents.ContainsKey(direction) || edges.ContainsKey(direction))
        {
            Debug.Log("Overwriting a cell adjacency!");
        }
        
        adjacents.Add(direction, cell);
        edges.Add(direction, edge);
    }

    public Edge GetEdge(Direction direction)
    {
        Edge result = null;
        edges.TryGetValue(direction, out result);
        return result;
    }

    public bool GetEdgeDirection(Edge edge, ref Direction outputDir)
    {
        foreach (Direction dir in Enum.GetValues(typeof(Direction)))
        {
            if (GetEdge(dir) == edge)
            {
                outputDir = dir;
                return true;
            }
        }
        return false;
    }

    public Cell GetAdjacent(Direction direction)
    {
        Cell result = null;
        adjacents.TryGetValue(direction, out result);
        return result;
    }

    public CellKind GetKind()
    {
        return this.kind;
    }

    public void SetCoordinates(Vector2 coords)
    {
        this.coords = coords;
    }

    public Vector2 GetCoordinates()
    {
        return this.coords;
    }

    public RectTransform GetTransform()
    {
        return this.transformCell;
    }

    public GameObject GetGameObjectsContainer()
    {
        return this.gameObjectsContainer;
    }

    public void SetStatus(CellStatus status)
    {
        this.status = status;
        if (this.smokeFire == null)
        {
            this.smokeFire = GameObject.Instantiate<GameObject>(Resources.Load<GameObject>("Prefabs/SmokeFire"), this.gameObjectsContainer.transform);//GameObject.Find("GameObjectsLayer").transform);
        }

        if (this.status == CellStatus.FIRE)
        {
            this.smokeFire.GetComponent<Image>().sprite = fireSprite;
        }
        else if (this.status == CellStatus.SMOKE)
        {
            this.smokeFire.GetComponent<Image>().sprite = smokeSprite;
        }
        else if (this.status == CellStatus.NO_FIRE)
        {
            Destroy(smokeFire);
            this.smokeFire = null;
        }
    }

    public CellStatus GetStatus()
    {
        return this.status;
    }

    public void RevealPOI()
    {
        ArrayList removeThese = new ArrayList();
        if (this.pois.Count > 0)
        {
            foreach (POI poi in pois)
            {
                if (poi.GetStatus() == POIstatus.HIDDEN)
                {
                    POI result = poi.Reveal(this);
                    if (result != null) 
                    {
                        Debug.Log("False alarm!");
                        removeThese.Add(result);
                    }
                }
            }
            foreach (POI poi in removeThese)
            {
                this.RemovePOI(poi);
            }
        }
    }

    public void SetClosestAmbulanceCell(Cell cell)
    {
        this.closestAmbulance = cell;
    }

    public void Shockwave(Direction direction, Cell curr)
    {
        Edge e=curr.GetEdge(direction);
        if(e.IsTraversable())
        {
            Cell adjacent=curr.GetAdjacent(direction);
            if (typeof(Door).IsInstanceOfType(e))
            {
                EventManager.i.SetDoorStatusEvent(curr, direction, DoorStatus.DESTROYED);
            }
            else if(adjacent.GetStatus() == CellStatus.NO_FIRE || adjacent.GetStatus() == CellStatus.SMOKE)
            {
                EventManager.i.SetCellStatusEvent(adjacent, CellStatus.FIRE);
            }
            else if (adjacent.GetKind() == CellKind.INDOOR)
            {
                Shockwave(direction, adjacent);
            }
        }
        else
        {
            //closed doors and all undestroyed walls are not traversable
            if(typeof(Door).IsInstanceOfType(e))
            {
                EventManager.i.SetDoorStatusEvent(curr, direction, DoorStatus.DESTROYED);
            }
            if(typeof(Wall).IsInstanceOfType(e))
            {
                EventManager.i.DamageWallEvent(curr, direction);
            }
        }
    }

    public void Explosion()
    {
        foreach(Direction direction in System.Enum.GetValues(typeof(Direction)))
        {
            Shockwave(direction, this);
        }
    }

    public void AddPOI(POI poi)
    {
        pois.Add(poi);
    }

    public void RemovePOI(POI poi)
    {
        pois.Remove(poi);
    }

    public ArrayList GetPOI()
    {
        return this.pois;
    }

    public ParkingKind GetParkingKind()
    {
        return this.parkingKind;
    }

    public Cell GetClosestAmbulance()
    {
        return this.closestAmbulance;
    }
}
