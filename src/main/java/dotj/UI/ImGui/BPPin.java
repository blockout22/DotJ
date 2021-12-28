package dotj.UI.ImGui;

public class BPPin {
    public enum PinType{
        Output,
        Input
    }

    public enum DataType{
        Flow,
        Bool,
        Int,
        Float,
        String,
        Object,
        Function
    }

    static {
        int iLinkID = 0;
    }

    private final int ID;
    private final PinType pinType;
    private final DataType dataType;
    private String name = "";
    public int connectedTo = -1;

    public BPPin(int ID, DataType dataType, PinType pinType, int linkID){
        this.ID = ID;
        this.dataType = dataType;
        this.pinType = pinType;
    }

    public int getID(){
        return ID;
    }

    public PinType getPinType() {
        return pinType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setName(String name){
        if(name == null){
            name = "";
        }
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
