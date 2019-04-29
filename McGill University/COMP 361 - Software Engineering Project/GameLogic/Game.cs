using System.Collections;
using System.Collections.Generic;
using Enums;
using ExitGames.Client.Photon;
using Photon.Pun;
using Photon.Realtime;
using UnityEngine;
using UnityEngine.UI;
using Random = System.Random;

public class Game : MonoBehaviour
{
    public static Game Instance = null;
    private Random rand = new Random();

    string gameName, gamePassword;
    int numPlayers;
    public int playerTurn;
    public GameStatus status;
    int victimsLost, victimsSaved, structDamage;
    Ruleset ruleset;

    public GameObject escapeMenu, localAPText;

    public ArrayList players = new ArrayList();
    public ArrayList fireMen = new ArrayList();
    public ArrayList activePOIs = new ArrayList();
    public ArrayList inactivePOIs = new ArrayList();
    private System.Collections.Hashtable playerToFireman = new System.Collections.Hashtable();
    public GameObject victimsLostText, victimsSavedText, structDamageText;
    private GameObject topPanel, gameFinishedPanel;
    private RectTransform topPanelTransform;
    private Dictionary<Vector2, Cell> cells;

    private Camera cam;


    // Use this for initialization
    void Start()
    {
        Instance = this;

        cam = Camera.main;

        //Initializing the escapeMenu to be invisible
        escapeMenu = GameObject.Find("escapeMenuPanel");
        escapeMenu.SetActive(false);
        gameFinishedPanel = GameObject.Find("GameFinishedPanel");
        gameFinishedPanel.SetActive(false);

        //Taking in all of the data from the Game Lobby
        //GameLobby gameLobby = GameObject.Find("GameLobbyHandle").GetComponent<GameLobby>();
        SetNumPlayers(PhotonNetwork.PlayerList.Length);
        SetRules((Ruleset)PhotonNetwork.CurrentRoom.CustomProperties["ruleset"]);
        SetStatus(GameStatus.SETUP);
        //gameLobby.Destroy();
        //gameLobby = null;

        foreach (Player player in PhotonNetwork.PlayerList)
        {
            players.Add(player);
        }

        //TODO this should have an argument so we can specify which board
        BoardLoader boardLoader = BoardLoader.Instance;
        cells = boardLoader.InitBoard1();
        StartCoroutine(RestOfStart());
    }

    IEnumerator RestOfStart()
    {
        //waits until the BoardLoader has finished instantiating cells
        yield return new WaitUntil(() => cells.Count == 80);

        int i, j;
        //Initializing all of the Fire Men and storing them


        Vector3 initialPosition = new Vector3(-102, 910, 0);
        i = 1;

        if (PhotonNetwork.IsMasterClient)
        {
            foreach (Player player in players)
            {
                //Associating the players to specific FireMen (This step will have to be modified when we add the "choose who's what color in the gameLobby")
                GameObject firemanGo = PhotonNetwork.Instantiate("FireFighter" + i, initialPosition, Quaternion.identity);
                firemanGo.GetPhotonView().TransferOwnership(player.ActorNumber);

                FireMan fireman = firemanGo.GetComponent<FireMan>();
                AddFiremanAndAssociate(fireman, player.ActorNumber);

                //inform the other clients of how to associate fireman
                EventManager.i.FindFiremanAndAssociateEvent(firemanGo, player);
                i++;
            }

            //Instantiate the vicims. Randomization will occur when we take from it
            for (i = 0; i < 10; i++)
            {
                Victim newVictim = PhotonNetwork.Instantiate("Victim", Vector3.zero, Quaternion.identity).GetComponent<Victim>();
                inactivePOIs.Add(newVictim);
                EventManager.i.FindPOI(newVictim);
            }

            //Instantiate the falseAlarms
            for (i = 0; i < 5; i++)
            {
                FalseAlarm newFa = PhotonNetwork.Instantiate("FalseAlarm", Vector3.zero, Quaternion.identity).GetComponent<FalseAlarm>();
                inactivePOIs.Add(newFa);
                EventManager.i.FindPOI(newFa);
            }

            //THIS IS WHERE WE WILL WANT TO ADD ALL OF THE STARTING FIRES AND POIs
            Vector2[] coords = { new Vector2(2,2),
                new Vector2(3,2),
                new Vector2(2,3),
                new Vector2(3,3),
                new Vector2(4,3),
                new Vector2(5,3),
                new Vector2(4,4),
                new Vector2(6,5),
                new Vector2(7,5),
                new Vector2(6,6),
                new Vector2(4,2),
                new Vector2(1,5),
                new Vector2(8,5)};

            for (i = 0; i < 10; i++)
            {
                Cell cell = null;
                cells.TryGetValue(coords[i], out cell);
                EventManager.i.SetCellStatusEvent(cell, CellStatus.FIRE);
            }

            for (i = i; i < 13; i++)
            {
                int randomizer = rand.Next(0, inactivePOIs.Count);
                Cell cell = null;
                cells.TryGetValue(coords[i], out cell);

                ActivatePOI(randomizer, cell);
                EventManager.i.ActivatePOIEvent(randomizer, cell);
            }
        }

        topPanel = GameObject.Find("TopPanel");
        topPanelTransform = topPanel.GetComponent<RectTransform>();

        UpdateText();

        playerTurn = 0;
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            if (!escapeMenu.activeSelf) escapeMenu.SetActive(true);
            else escapeMenu.SetActive(false);
        }
        if (status == GameStatus.SETUP)
        {
            //This is where we're going to prompt the user to set starting position
        }
        if (status == GameStatus.MAIN_GAME)
        {
            //This is where we're going to sit while current players perform their actions
        }
        if (status == GameStatus.COMPLETED)
        {
            gameFinishedPanel.SetActive(true);
        }
    }

    //Adds a FireMan.cs object to the list of firemen and associate it with the 
    //given actor number
    public void AddFiremanAndAssociate(FireMan fireman, int actorNumber)
    {
        fireMen.Add(fireman);
        playerToFireman.Add(actorNumber, fireman);
    }

    //gets a player's corresponding fireman
    public FireMan GetFireMan(Player player)
    {
        if (playerToFireman[player.ActorNumber] == null)
        {
            Debug.Log("No such fireman: " + player.ActorNumber);
        }
        return (FireMan)playerToFireman[player.ActorNumber];
    }

    public Player GetCurrentPlayer()
    {
        return (Player)players[playerTurn];
    }

    public bool IsLocalPlayersTurn()
    {
        return PhotonNetwork.LocalPlayer.ActorNumber == GetCurrentPlayer().ActorNumber;
    }

    public FireMan GetLocalFireman()
    {
        return GetFireMan(PhotonNetwork.LocalPlayer);
    }

    //public int MoveFireFighter(string playerUserName, Cell cellClicked)
    //{
    //    int cost = -1;
    //    int i = 0;
    //    //Call move on ONLY the one FireMan who clicked on the cell
    //    foreach (Player player in players)
    //    {
    //        if (playerUserName.Equals(player.GetUsername()) && ((FireMan)fireMen[i]).CanMove(cellClicked, out cost) && ((FireMan)fireMen[i]).GetPlayerNumber() == playerTurn)
    //        {
    //            ((FireMan)fireMen[i]).Move(cellClicked);
    //        }
    //        i++;
    //    }
    //    return cost;
    //}


    #region PathFinder

    public int PathFinder(Cell cell)
    {
        int cost = -1;
        int i = 0;
        //Check the pathfinder on ONLY the one FireMan who clicked on the cell
        foreach (Player player in players)
        {
           if (IsLocalPlayersTurn()) //(playerId.Equals(player.ActorNumber))
            {
                List<Cell> path = ((FireMan)fireMen[i]).CanMove(cell, out cost);
                //This is where an arrow to the pointed at square will go and some code to determine if you can walk to a cell
                //if (path != null)
                //    DrawShortestPath(path);
            }
            i++;
        }
        return cost;
    }


    public bool isPathDrawn()
    {
       // if (lRend == null)
       //     return false;
       // else
            return true;
    }

    public void TestShortestPath(FireMan fireman)
    {
        //fireman.SetAP(100);
        Vector2 start = new Vector2(4, 0);
        Vector2 end = new Vector2(0, 0);
        Cell startCell = cells[start];
        Cell endCell = cells[end];
        int cost;
        List<Cell> path = fireman.GetCheapestPath(startCell, endCell, out cost);
        DrawShortestPath(path);
    }

    public void DrawShortestPath(List<Cell> path)
    {
        if (path == null)
            return;
        int count = path.Count;
        Vector3[] vecs = new Vector3[count];
        for (int i = 0; i < count; i++)
        {
            Cell cell = path[i];
            vecs[i] = GetCellCenter(cell);
        }
        DrawPath(path, vecs);
    }

    public Direction getArrowheadDir(List<Cell> path)
    {
        if (path == null || path.Count < 2)
            return Direction.UP;
        int length = path.Count;
        Vector2 vec1 = new Vector2(); // represented the second to last cell coord
        Vector2 vec2 = new Vector2(); // represented the tail cell coord
        int i = 0;
        foreach (Cell cell in path)
        {
            if (i == length - 2)
            {
                vec1 = cell.GetCoordinates();
            }
            if (i == length - 1)
            {
                vec2 = cell.GetCoordinates();
            }
            i++;
        }

        float x1 = vec1.x;
        float y1 = vec1.y;
        float x2 = vec2.x;
        float y2 = vec2.y;

        if (x1 == x2)
        {
            if (y1 > y2)
                return Direction.UP;
            else
                return Direction.DOWN;
        }
        else //(y1 == y2)
        {
            if (x1 > x2)
                return Direction.LEFT;
            else
                return Direction.RIGHT;
        }
    }

    public Vector3 getArrowheadPoint(List<Cell> path)
    {
        int i = 0;
        Vector3 vec = new Vector3(0f, 0f, 0f);
        foreach (Cell cell in path)
        {
            if (i == path.Count - 1)
            {
                vec = GetCellCenter(cell);
            }
            i++;
        }
        return vec;

    }

    public Vector3[] GetArrowHead(List<Cell> path)
    {

        Cell targetCell = path[path.Count - 1];

        Direction dir = getArrowheadDir(path);
        Vector3 head = getArrowheadPoint(path);
        Vector3 tip1 = new Vector3();
        Vector3 tip2 = new Vector3();

        float q = 12f; //0.3f;
        float p = 9f; //0.17f;
        if (dir == Direction.UP) 
        {
            tip1.x = head.x + p;
            tip2.x = head.x - p;
            tip1.y = head.y - q;
            tip2.y = head.y - q;
        }
        else if (dir == Direction.DOWN)  // good
        {
            tip1.x = head.x - p;
            tip2.x = head.x + p;
            tip1.y = head.y + q;
            tip2.y = head.y + q;
        }
        else if (dir == Direction.LEFT) //finish this
        {
            tip1.x = head.x + q;
            tip2.x = head.x + q;
            tip1.y = head.y + p;
            tip2.y = head.y - p;
        }
        else
        {
            tip1.x = head.x - q;
            tip2.x = head.x - q;
            tip1.y = head.y + p;
            tip2.y = head.y - p;
        }

        Vector3[] linePoints = new Vector3[4];
        linePoints[0] = tip1;
        linePoints[1] = head;
        linePoints[2] = tip2;
        linePoints[3] = head;

        return linePoints;
    }

    private void DrawPath(List<Cell> path, Vector3[] linePoints)
    {

        Cell cell = path[0];
        GameObject myLine = new GameObject();
        myLine.AddComponent<LineRenderer>();
        LineRenderer lRend = myLine.GetComponent<LineRenderer>();

        lRend.SetColors(Color.green, Color.green);
        lRend.SetWidth(0.05f, 0.05f);
        lRend.useWorldSpace = true;

        Vector3[] arrowhead = GetArrowHead(path);


        Vector3[] vecs = new Vector3[linePoints.Length + arrowhead.Length];
        for (int i = 0; i < linePoints.Length; i++)
        {
            vecs[i] = linePoints[i];
        }
        for (int i = 0; i < arrowhead.Length; i++)
        {
            vecs[linePoints.Length + i] = arrowhead[i];
        }

        Vector3[] points = new Vector3[vecs.Length];
        for (int i = 0; i < vecs.Length; i++)
        {
            points[i] = new Vector3();
        }
        for (int i = 0; i < vecs.Length; i++)
        {
            points[i].z = 10;
            points[i] = (cam.ScreenToWorldPoint(new Vector3(vecs[i].x, vecs[i].y, cam.nearClipPlane)));
        }

        lRend.positionCount = points.Length;
        lRend.SetPositions(points);

        // Destroy line after 5 seconds.
        Destroy(lRend, 2);
        //HidePath();
    }

//    public void HidePath()
//    {

//        Destroy(lRend, 4);
//        Cell cell = cells[new Vector2(0, 0)];
//        lRend = cell.GetComponent<LineRenderer>();
//        Vector3[] vecs = new Vector3[2];
//        vecs[0] = Vector3.zero;
//        vecs[1] = Vector3.zero;
//        lRend.positionCount = vecs.Length;
//        lRend.SetPositions(vecs); 
//        lRend = null;
//    } 


    public Vector3 GetCellCenter(Cell cell)
    {
        Vector3 cellVec = cell.gameObject.transform.position;
        Vector3 midVec = new Vector3(cellVec.x + 60, cellVec.y - 45);
        return midVec;
    }

    #endregion

    public void UpdateText()
    {
        victimsLostText.GetComponent<Text>().text = "Victims Lost: " + victimsLost;
        victimsSavedText.GetComponent<Text>().text = "Victims Saved: " + victimsSaved;
        structDamageText.GetComponent<Text>().text = "Structure Damage: " + structDamage;
    }

    public void UpdateLocalAPText(int AP)
    {
        localAPText.GetComponent<Text>().text = "Current AP = " + AP;
    }

    public void SetStatus(GameStatus status)
    {
        this.status = status;
    }

    public void SetRules(Ruleset ruleSet)
    {
        this.ruleset = ruleSet;
    }

    public void SetNumPlayers(int numPlayers)
    {
        this.numPlayers = numPlayers;
    }
    
    public void SetFiremanStartingSpace(FireMan f, Cell startingCell)
    {
        if (startingCell.GetKind() == CellKind.OUTDOOR)
        {
            f.SetLocation(startingCell);
            //we have both because the event version is sent to all clients except this one
            IncrementTurn();
            EventManager.i.IncrementTurnEvent();
        }
    }

    public void AddVictimRescued()
    {
        victimsSaved++;
        if (victimsSaved >= 7) status = GameStatus.COMPLETED;
        UpdateText();
    }

    public void AddVictimLost()
    {
        victimsLost++;
        UpdateText();
    }

    public void AddStructDamage()
    {
        structDamage++;
        if(structDamage >= 24){
            status = GameStatus.COMPLETED;
        }
        UpdateText();
    }

    public int RollRedDie()
    {
        int value = 1;
        string input = GameObject.Find("RedInput").GetComponent<InputField>().text;
        if (input != "") value = int.Parse(input);
        else value = rand.Next(1, 9);
        print(value);
        return value;
    }

    public int RollBlackDie()
    {
        int value = 1;
        string input = GameObject.Find("BlackInput").GetComponent<InputField>().text;
        if (input != "") value = int.Parse(input);
        else value = rand.Next(1, 7);
        print(value);
        return value;
    }

    public void AdvanceFire(){
        Cell targetCell = null;
        if (GetCell(RollRedDie(), RollBlackDie(), ref targetCell))
        {
            CellStatus status = targetCell.GetStatus();

            if (status == CellStatus.NO_FIRE)
            {
                EventManager.i.SetCellStatusEvent(targetCell, CellStatus.SMOKE);
            }
            else if (status == CellStatus.SMOKE)
            {
                EventManager.i.SetCellStatusEvent(targetCell, CellStatus.FIRE);
            }
            else
            {
                targetCell.Explosion();
            }
        }
    }

    public void RemovePOIs()
    {
        foreach (KeyValuePair<Vector2, Cell> cell in cells)
        {
            if (cell.Value.GetStatus() == CellStatus.FIRE)
            {
                ArrayList pois = cell.Value.GetPOI();
                ArrayList removeThese = new ArrayList();
                foreach (POI poi in pois)
                {
                    poi.Reveal(cell.Value);
                    if (typeof(Victim).IsInstanceOfType(poi))
                    {
                        ((Victim)poi).Kill();
                    }
                    if (poi != null)
                    {
                        removeThese.Add(poi);
                    }
                }
                foreach (POI poi in removeThese)
                {
                    cell.Value.RemovePOI(poi);
                }
            }
        }
    }

    public void ActivatePOI(int index, Cell cell)
    { 
        POI poi = (POI)inactivePOIs[index];
        inactivePOIs.RemoveAt(index);

        cell.AddPOI(poi);
        activePOIs.Add(poi);
        poi.SetStatus(POIstatus.HIDDEN);
        poi.SetLocation(cell);
    }

    public void DeactivatePOI(POI poi)
    {
        activePOIs.Remove(poi);
    }

    public void KnockDownRelocate()
    {
        foreach (FireMan f in fireMen)
        {
            Cell c = f.GetLocation();
            CellStatus status = c.GetStatus();

            if (status == CellStatus.FIRE)
            {
                f.Drop();
                f.MoveToClosestAmbulance();
            }
        }
    }

    public void ReplacePOIs()
    {
        while (activePOIs.Count < 3)
        {
            if (inactivePOIs.Count > 0)
            {
                Cell cell = null;
                int randomizer = rand.Next(0, inactivePOIs.Count);
                while (true)
                {
                    if (GetCell(RollRedDie(), RollBlackDie(), ref cell))
                    {
                        if (cell.GetStatus() != CellStatus.FIRE) break;
                    }
                }

                ActivatePOI(randomizer, cell);
            }
            else
            {
                break;
            }
        }
    }

    public void ReplaceSmokeWithFire()
    {
        ArrayList fire = new ArrayList();
        foreach (KeyValuePair<Vector2, Cell> cell in cells)
        {
            if (cell.Value.GetStatus() == CellStatus.FIRE)
            {
                RecursiveSmokeToFire(cell.Value);
            }
        }
    }

    public void RecursiveSmokeToFire(Cell cell)
    {
        foreach (Direction direction in System.Enum.GetValues(typeof(Direction)))
        {
            Edge edge = cell.GetEdge(direction);
            if (edge != null)
            {
                if (edge.IsTraversable())
                {
                    Cell adj = cell.GetAdjacent(direction);
                    if (adj != null)
                    {
                        if (adj.GetStatus() == CellStatus.SMOKE)
                        {
                            EventManager.i.SetCellStatusEvent(adj, CellStatus.FIRE);
                            RecursiveSmokeToFire(adj);
                        }
                    }
                }
            }
        }
    }

    public void RemoveFireOutdoors()
    {
        foreach (Cell cell in cells.Values)
        {
            if (cell.GetStatus() == CellStatus.FIRE && cell.GetKind() == CellKind.OUTDOOR)
            {
                EventManager.i.SetCellStatusEvent(cell, CellStatus.NO_FIRE);
            }
        }
    }

    public void EndTurn()
    {
        //prevent unwanted button presses
        if (!IsLocalPlayersTurn()) return;
        if (status != GameStatus.MAIN_GAME) return;

        //hide a popup if one was visible when EndTurn button was clicked
        PopupManager.Instance.DeleteButtonsAndHide();

        //local player can no longer move
        IncrementTurn();

        GetLocalFireman().RefillAP();
        AdvanceFire();
        ReplaceSmokeWithFire();
        RemovePOIs();
        KnockDownRelocate();
        RemoveFireOutdoors();
        ReplacePOIs();
        if( victimsLost > 4 || structDamage > 24)
        {
            SetStatus(GameStatus.COMPLETED);
        }
        //the next player can now move
        EventManager.i.IncrementTurnEvent();
    }

    public bool GetCell(int x, int y, ref Cell cell)
    {
        return cells.TryGetValue(new Vector2(x, y), out cell);
    }

    public ArrayList GetAmbulanceSpots()
    {
        ArrayList result = new ArrayList();
        foreach (KeyValuePair<Vector2, Cell> cell in cells)
        {
            if (cell.Value.GetParkingKind() == ParkingKind.AMBULANCE)
            {
                result.Add(cell.Value);
            }
        }
        return result;
    }

    public void IncrementTurn()
    {
        Debug.Log("incrementing turn");
        playerTurn++;
        if (playerTurn >= numPlayers)
        {
            playerTurn = 0;
            if (status == GameStatus.SETUP)
                status = GameStatus.MAIN_GAME;
        }
    }
}
