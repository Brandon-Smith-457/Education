using Enums;

public interface POI {
    void SetLocation(Cell cell);
    POI Reveal(Cell cell);
    POI Remove(Cell cell);
    void SetStatus(POIstatus status);
    POIstatus GetStatus();
}
