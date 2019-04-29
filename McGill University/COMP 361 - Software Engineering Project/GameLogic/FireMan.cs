using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Enums;
using Photon.Pun;
using ExitGames.Client.Photon;

public class FireMan : MonoBehaviourPun {
    int AP, moveAP, extinguishAP;
    SpecialistType specialist;
    private Cell currentCell;
    private Victim victim;
    public Game Game { get; set; }

    //Used only in the move functions to traverse the board as a graph
    private Dictionary<Vector2, CellElem> Unvisited;
    private Dictionary<Vector2, CellElem> Visited;
    public class CellElem
    {
        public int cost;
        public Cell parent;

        public CellElem()
        {
            this.Reset();
        }

        public void Reset()
        {
            this.cost = int.MaxValue;
            this.parent = null;
        }
    }

    private void Start()
    {
        SetAP(4);
        Game = Game.Instance;
        Game.Instance.UpdateLocalAPText(this.AP);
    }

    public void Move(Cell cellClicked)
    {
        //Actually setting the FireMan's gameObject's RectTransform to the center of the cellClicked
        int cost;
        List<Cell> path = CanMove(cellClicked, out cost);
        if (path != null)
        {
            this.MoveDeductAP(cost);
            
            Cell previous = currentCell;
            SetLocation(cellClicked);

            if (this.victim != null)
            {
                victim.SetLocation(currentCell);
                previous.RemovePOI(victim);
                currentCell.AddPOI(victim);
                if (currentCell.GetKind() == CellKind.OUTDOOR)
                    Drop();
            }
            
            cellClicked.RevealPOI();
            previous = null;
        }
    }

    #region Pathfinding
    //This is where the all important algorithm will be going!!!!
    //This is where the all important algorithm will be going!!!!
    public List<Cell> CanMove(Cell cell, out int cost)
    {
        cost = -1;
        if (currentCell == null || cell == null)
            return null;
            
        List<Cell> path = GetCheapestPath(this.currentCell, cell, out cost);
        return path;
    }

    public void SetNeighCost(Cell currCell){
        List<Cell> unvisitedNeighCells = GetUnvisitedNeighCells(currCell);
        int currCost = GetCost(currCell);
        if (currCost == int.MaxValue)
        {
            return;
        }
        for (int i = 0; i < unvisitedNeighCells.Count; i++){
            int cost = GetCost(unvisitedNeighCells[i]);
            int moveCost = CalMovementCost(currCell, unvisitedNeighCells[i]);
            if (moveCost == int.MaxValue)
                continue;
            else if (currCost + moveCost < cost)
                SetCost(unvisitedNeighCells[i], currCost + moveCost);
        }
    }

    private int CalMovementCost(Cell currCell, Cell neigh){
        if (victim != null){
            if (neigh.GetStatus() != CellStatus.FIRE)
                return 2;
            else
                return int.MaxValue;
        }
        else{
            if (neigh.GetStatus() != CellStatus.FIRE)
                return 1;
            else
                return 2;
        }
    }

    private Cell selectCurrCell(){
        Cell minCell = null;
        Vector2 minVec = new Vector2(0,0);
        foreach (Vector2 v in Unvisited.Keys)
        {
            minVec = v;
            break;
        }
        int minCost = Unvisited[minVec].cost;
        foreach (Vector2 vec in Unvisited.Keys){
            if (Unvisited[vec].cost < minCost){
                minCost = Unvisited[vec].cost;
                minVec = vec;
            }
        }
        bool bret = Game.GetCell((int)minVec.x, (int)minVec.y, ref minCell);
        return minCell;
    }


    public List<Cell> GetCheapestPath(Cell startCell, Cell endCell, out int cost)
    {
        if (startCell == endCell)
        {
            cost = 0;
            return null;
        }
        InitWalkBoards();
        SetCost(startCell, 0);
        Vector2 vec = startCell.GetCoordinates();
        Unvisited[vec].parent = null;
        cost = 0;
        bool bret = GetCheapestPathRec(startCell, endCell);
        if (bret == false)
            return null;
        List<Cell> path = WalkBackPath(endCell, out cost);
            if (cost > (AP + moveAP))
                return null;
            else
           {
                if(cost == (AP + moveAP))
                {
                    if (endCell.GetStatus() == CellStatus.FIRE)
                        return null;
                }
                return path;
            }
    }

    public List<Cell> WalkBackPath(Cell endCell, out int TotalCost)
    {
        TotalCost = 0;
        Stack<Cell> pathS = new Stack<Cell>();
        List<Cell> pathL = new List<Cell>();
        Vector2 vec = endCell.GetCoordinates();
        Cell parent = Visited[vec].parent;
        Cell curr = endCell;
        while(curr != null)
        {
            pathS.Push(curr);
            Vector2 currVec = curr.GetCoordinates();
            curr = Visited[currVec].parent; 
        }
        int count = pathS.Count;
        Cell first, sec;
        first = pathS.Pop();
        sec = pathS.Pop();
        pathL.Add(first);
        pathL.Add(sec);
        for (int i = 2; i < count; i++)
        {
            TotalCost = TotalCost + CalMovementCost(first, sec);
            first = sec;
            sec = pathS.Pop();
            pathL.Add(sec);

        }
        TotalCost = TotalCost + CalMovementCost(first, sec);
        //PrintDebugPath(pathL);
        return pathL;
    }

    public void PrintDebugPath(List<Cell> path)
    {

        string str = "";
        for (int i = 0; i < path.Count; i++)
        {
            Vector2 vec = path[i].GetCoordinates();
            str += string.Format("{0}: [{1},{2}]  ", i, vec.x, vec.y);
        }
        Debug.Log(str);
    }

    public bool GetCheapestPathRec(Cell currCell, Cell endCell)
    {
        List<Cell> unvistedNeighborCells = GetUnvisitedNeighCells(currCell);
        int currCost = GetCost(currCell);
        for (int i = 0; i < unvistedNeighborCells.Count; i++)
        {
            Cell cell = unvistedNeighborCells[i];
            int cost = GetCost(cell);
            int moveCost = CalMovementCost(currCell, cell);
            if (moveCost == int.MaxValue)
                continue;
            if (cost > currCost + moveCost)
            {
                SetCost(cell, currCost + moveCost);
                Vector2 cellVec = cell.GetCoordinates();
                Unvisited[cellVec].parent = currCell;
            }
        }
        MarkAsVisited(currCell);
        Cell newCurrCell = selectCurrCell();
        if (newCurrCell == null)
        {
            return false;
        }
        Vector2 newCoord = newCurrCell.GetCoordinates();
        Vector2 endCoord = endCell.GetCoordinates();
        if ((newCoord[0] == endCoord[0]) && (newCoord[1] == endCoord[1]))
        {
            MarkAsVisited(newCurrCell);
            return true;
        }
        bool bret = GetCheapestPathRec(newCurrCell, endCell);
        return bret;
    }

    private void InitWalkBoards(){
        this.Unvisited = new Dictionary<Vector2, CellElem>();
        for (int c = 0; c < 10; c++){
            for (int r = 0; r < 8; r++){
                CellElem elem = new CellElem();
                this.Unvisited[new Vector2(c, r)] = elem;
            }
        }
        Visited = new Dictionary<Vector2, CellElem>();
    }

    private void SetCost(Cell cell, int cost){
        Vector2 vec = cell.GetCoordinates();
        if (Visited.ContainsKey(vec))
            Visited[vec].cost = cost;
        else if (Unvisited.ContainsKey(vec))
            Unvisited[vec].cost = cost;
    }

    private int GetCost(Cell cell){
        Vector2 vec = cell.GetCoordinates();
        if (Visited.ContainsKey(vec))
            return Visited[vec].cost;
        else if (Unvisited.ContainsKey(vec))
            return Unvisited[vec].cost;
        return 0;
    }

    private void MarkAsVisited(Cell cell){
        Vector2 vec = cell.GetCoordinates();
        Visited.Add(vec, Unvisited[vec]);
        Unvisited.Remove(vec);
    }

    private Cell GetNeighborCell (Direction dir, Cell currCell){
        Vector2 coords = currCell.GetCoordinates();
        int x = (int)coords.x;
        int y = (int)coords.y;
        Cell neigh = null;
        bool bret;
        if (dir == Direction.RIGHT){
            if (x == 9)
                return null;
            bret = Game.GetCell(x + 1, y, ref neigh);
            return neigh;

        }
        else if (dir == Direction.LEFT) {
            if (x == 0)
                return null;
            bret = Game.GetCell(x - 1, y, ref neigh);
            return neigh;
        }
        else if (dir == Direction.UP) {
            if (y == 0)
                return null;
            bret = Game.GetCell(x, y - 1, ref neigh);
            return neigh;
        }
        else
        {
            if (y == 7)
                return null;
            bret = Game.GetCell(x, y + 1, ref neigh);
            return neigh;
        }
    }

    private bool IsCellVisited(Cell cell){
        Vector2 coords = cell.GetCoordinates();
        int x = (int)coords.x;
        int y = (int)coords.y;
        Vector2 vec = new Vector2(x, y);
        return Visited.ContainsKey(vec);
    }

    private Direction getOppDir(Direction dir){
        if (Direction.UP == dir)
            return Direction.DOWN;
        else if (Direction.DOWN == dir)
            return Direction.UP;
        else if (Direction.RIGHT == dir)
            return Direction.LEFT;
        else
            return Direction.RIGHT;

    }

    private List<Cell> GetUnvisitedNeighCells(Cell currCell){
        Cell neigh = null;
        List <Cell> unvistedNeighborCells = new List<Cell>();

        for (int i = 0; i < 4; i++){
            neigh = currCell.GetAdjacent((Direction)i);
            if (neigh == null)
                continue;
            Edge edge = neigh.GetEdge(getOppDir((Direction) i));
            if ((neigh != null) && (!IsCellVisited(neigh)) && (edge != null) && (edge.IsTraversable()))
            {
                unvistedNeighborCells.Add(neigh);
            }

        }
        return unvistedNeighborCells; 
    }

    #endregion

    #region Public Methods

    public void SetLocation(Cell cell)
    {
        this.currentCell = cell;
        this.gameObject.transform.SetParent(currentCell.GetGameObjectsContainer().transform, false);
        
        int[] coords = { (int)cell.coords.x, (int)cell.coords.y };
        gameObject.GetPhotonView().RPC("RPCSetFiremanLocation", RpcTarget.Others, coords);
    }

    public Cell GetLocation()
    {
        return this.currentCell;
    }
    
    public void SetAP(int newAP)
    {
        if ( (0 <= newAP) && (newAP <= 8)){ 
            this.AP = newAP;
        }
        Game.Instance.UpdateLocalAPText(this.AP);
        gameObject.GetPhotonView().RPC("RPCSetAP", RpcTarget.Others, AP);
    }
    
    public void SetMoveAP(int APvalue)
    {
        if ( (0 <= APvalue) && (APvalue <= 3)){ 
            this.moveAP = APvalue;
        }

    }
    public void SetExtinguishAP(int APvalue)
    {
        if ( (0 <= APvalue) && (APvalue <= 3)){ 
            this.extinguishAP = APvalue;
        }

    }

    public int GetMoveAP()
    {
        return this.moveAP;
    }
    
    public int GetExtinguishAP()
    {
        return this.extinguishAP;
    }

    public int GetAP()
    {
        return this.AP;
    }

    public void RefillAP()
    {
        if(this.AP > 3)
        {
            this.SetAP(8);
        }
        else
        {
            if(this.specialist==SpecialistType.GENERAL)
            {
                this.SetAP(this.AP +5);
            }
            else
            {
                this.SetAP(this.AP +4);
            }
            if(this.specialist==SpecialistType.RESCUE)
            {
                this.SetMoveAP(3);
            }
            else if(this.specialist==SpecialistType.CAFS)
            {
                this.SetExtinguishAP(3);
            }
        }
    }

    public bool CanDeductAP(ActionType actionType)
    {
        //CAFS have extinguishAP
        if (actionType == ActionType.EXTINGUISH)
        {
            return (this.AP+this.extinguishAP) >= determineCost(actionType);
        }        
        //all the other actions do not require special AP, and move checks in its deduct function
        else
        {
            return this.AP >= determineCost(actionType);
        }
    }
    
    
    public int determineCost(ActionType actionType)
    {
        int cost = 0;
        if (actionType == ActionType.EXTINGUISH)
        {
            if(this.specialist == SpecialistType.RESCUE)
            {
                cost=2;
            }
            else
            {
                cost=1;
            }
        }
        else if (actionType == ActionType.CHOP)
        {
            if(this.specialist == SpecialistType.RESCUE)
            {
                cost=1;
            }
            else
            {
                cost=2;
            }
        }
        else if(actionType == ActionType.TOGGLE_DOOR)
        {
            cost=1;
        }
        return cost;
        //else if(actionType==ActionType.FIRE_GUN)
        //{
        //    if(SpecialistType.DRIVER)
        //    {
        //        cost=2;
        //    }
        //    else
        //    {
        //        cost=4;
        //    }
        //}
    }

    public void DeductAP(ActionType actionType)
    {
        int cost;
        cost=determineCost(actionType);
        if(CanDeductAP(actionType))
        {
            if (actionType == ActionType.EXTINGUISH)
            {
                if(cost<=this.extinguishAP)
                {
                    this.SetExtinguishAP(this.GetExtinguishAP() - cost);
                }
                else if(cost>extinguishAP)
                {
                    this.SetAP(this.AP - (cost - this.GetExtinguishAP()));
                    this.SetExtinguishAP(0);
                }
            }
            //only move and extinguish and command have special APs, everything else deducts normally
            else
            {
                this.SetAP(this.GetAP() - cost);
            }
        }
    }
    //move works differently so it uses a different deduction function
    public void MoveDeductAP(int cost)
    {
        if((this.moveAP)+(this.AP)>=cost)
            {
            if(cost<=this.moveAP)
            {
                this.SetMoveAP(this.moveAP-cost);
            }
            else if(cost>moveAP)
            {
                this.SetAP(this.AP - (cost-this.moveAP));
                this.SetMoveAP(0);
            }
        }
    }

    public void MoveToClosestAmbulance()
    {
        this.SetLocation(this.currentCell.GetClosestAmbulance());
    }

    public void Pickup(Victim victim)
    {
        this.victim = victim;
        photonView.RPC("RPCPickup", RpcTarget.Others, victim.photonView.ViewID);
    }

    public void Drop()
    {
        if (victim == null) return;
        
        if (currentCell.GetKind() == CellKind.OUTDOOR)
        {
            victim.Rescue();
        }
        victim = null;
        photonView.RPC("RPCDrop", RpcTarget.Others);
    }

    public Victim GetVictim()
    {
        return this.victim;
    }

    public void ExtinguishFire(Cell cell)
    {
        bool temp = this.currentCell == cell;
        foreach (Direction direction in Enum.GetValues(typeof(Direction)))
        {
            temp = temp || (this.currentCell.GetAdjacent(direction) == cell && this.currentCell.GetEdge(direction).IsTraversable());
        }
        if (temp && this.CanDeductAP(ActionType.EXTINGUISH))
        {
            this.DeductAP(ActionType.EXTINGUISH);
            CellStatus status = cell.GetStatus();
            if (status == CellStatus.FIRE)
            {
                EventManager.i.SetCellStatusEvent(cell, CellStatus.SMOKE);
            }
            else if (status == CellStatus.SMOKE)
            {
                EventManager.i.SetCellStatusEvent(cell, CellStatus.NO_FIRE);
            }
        }
    }

    public void ToggleDoor(Door door)
    {
        if (this.CanDeductAP(ActionType.TOGGLE_DOOR))
        {
            this.DeductAP(ActionType.TOGGLE_DOOR);

            //I had to do this because otherwise I would need to have make Toggle()
            //and SetStatus() events, but these are used so infrequently that I dont want to
            DoorStatus status = (door.status == DoorStatus.CLOSED) ? DoorStatus.OPEN : DoorStatus.CLOSED;
            Direction dir = Direction.DOWN;
            if (!currentCell.GetEdgeDirection(door, ref dir))
            {
                Debug.LogError("unable to find a matching edge");
            }
            EventManager.i.SetDoorStatusEvent(currentCell, dir, status);
        }
    }

    public void ChopWall(Wall wall)
    {
        if (this.CanDeductAP(ActionType.CHOP))
        {
            this.DeductAP(ActionType.CHOP);

            Direction dir = Direction.DOWN;
            currentCell.GetEdgeDirection(wall, ref dir);
            if (!currentCell.GetEdgeDirection(wall, ref dir))
            {
                Debug.LogError("unable to find a matching edge");
            }

            EventManager.i.DamageWallEvent(currentCell, dir);
        }
    }

    public bool IsNextTo(Cell cell)
    {
        if (this.currentCell == cell) return true;
        foreach (Direction direction in System.Enum.GetValues(typeof(Direction)))
        {
            if (cell.GetEdge(direction).IsTraversable() && cell.GetAdjacent(direction) == this.currentCell)
            {
                return true;
            }
        }
        return false;
    }

    #endregion

    #region PunRPCs

    [PunRPC]
    void RPCSetFiremanLocation(int[] coords)
    {
        Cell newCell = null;
        Game.GetCell(coords[0], coords[1], ref newCell);

        currentCell = newCell;
        gameObject.transform.SetParent(currentCell.GetGameObjectsContainer().transform, false);
    }

    [PunRPC] 
    void RPCSetAP(int newAP)
    {
        AP = newAP;
    }

    [PunRPC]
    void RPCPickup(int viewId) 
    {
        GameObject victimGo = PhotonView.Find(viewId).gameObject;
        victim = victimGo.GetComponent<Victim>();
    }

    [PunRPC]
    void RPCDrop() 
    {
        if (currentCell.GetKind() == CellKind.OUTDOOR)
        {
            victim.Rescue();
        }
        victim = null;
    }

    #endregion
}
