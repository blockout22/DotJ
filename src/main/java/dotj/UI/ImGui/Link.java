package dotj.UI.ImGui;

public class Link {

    public int ID;
    public int startPinID;
    public int endPinID;

    public Link(int ID, int startPinID, int endPinID) {
        this.ID = ID;
        this.startPinID = startPinID;
        this.endPinID = endPinID;
    }
}
