package dotj.UI.ImGui;

import dotj.UI.ImGui.nodes.NodeData;
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

    private BPNode node;
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

    private NodeData data;

    public BPPin(BPNode node, int ID, DataType dataType, PinType pinType, int linkID){
        this.node = node;
        this.ID = ID;
        this.dataType = dataType;
        this.pinType = pinType;
        data = new NodeData();
        setupData();
    }

    private void setupData(){
        switch (dataType){
            case Flow:
                break;
            case Bool:
                data.setValue(Boolean);
                break;
            case Int:
                data.setValue(Int);
                break;
            case Float:
                data.setValue(Float);
                break;
            case Double:
                data.setValue(Double);
                break;
            case String:
                data.setValue(String);
                break;
            case Object:
//                data.setValue();
                break;
            case Function:
                break;
        }
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

    public NodeData getData() {
        return data;
    }

    public void setData(NodeData data) {
        this.data = data;
    }

    public BPNode getNode() {
        return node;
    }
}
