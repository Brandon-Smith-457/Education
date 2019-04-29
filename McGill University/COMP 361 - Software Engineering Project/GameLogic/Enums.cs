namespace Enums
{
    public enum CellKind
    {
        INDOOR,
        OUTDOOR
    }

    public enum CellStatus
    {
        NO_FIRE,
        SMOKE,
        FIRE
    }

    public enum POIstatus
    {
        HIDDEN,
        REVEALED,
        INACTIVE,
        REMOVED
    }

    public enum ActionType
    {
        MOVE,
        TOGGLE_DOOR,
        EXTINGUISH,
        CHOP,
        FIRE_GUN
    }

    public enum PlayerStatus
    {
        OFFLINE,
        AVAILABLE,
        READY,
        INGAME
    }

    public enum Ruleset
    {
        Family,
        Recruit,
        Veteran,
        Heroic
    }

    public enum Direction
    {
        UP=0,
        DOWN,
        LEFT,
        RIGHT
    }

    public enum GameStatus
    {
        SETUP,
        MAIN_GAME,
        COMPLETED
    }

    public enum DoorStatus
    {
        OPEN,
        CLOSED,
        DESTROYED
    }

    public enum ParkingKind
    {
        NONE,
        AMBULANCE,
        FIRE_ENGINE
    }

    public enum WallStatus
    {
        INTACT,
        DAMAGED,
        DESTROYED
    }

    public enum SpecialistType
    {
        FIRE_FIGHTER,
        DRIVER,
        CAPTAIN,
        CAFS,
        RESCUE,
        GENERAL
    }

    public enum BoardType
    {
        PRE_SET_1,
        PRE_SET_2,
        RANDOM
    }
}