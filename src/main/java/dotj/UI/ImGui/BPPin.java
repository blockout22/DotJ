package dotj.UI.ImGui;

import imgui.type.*;

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
        Double,
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

    private ImBoolean Boolean = new ImBoolean();
    private ImInt Int = new ImInt();
    private ImFloat Float = new ImFloat();
    private ImString String = new ImString();
    private ImDouble Double = new ImDouble();
    //this doesn't exist but possibly a Object<T> kind of class might work
//    private ImObject Object = new ImObject()

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

    public int getConnectedTo() {
        return connectedTo;
    }

    public ImBoolean getBoolean() {
        return Boolean;
    }

    public ImInt getInt() {
        return Int;
    }

    public ImFloat getFloat() {
        return Float;
    }

    public ImString getString() {
        return String;
    }

    public ImDouble getDouble() {
        return Double;
    }
}
