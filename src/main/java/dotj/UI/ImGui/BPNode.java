package dotj.UI.ImGui;

import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class BPNode {

    private static int localeVariableID = 0;

    private final BPGraph graph;
    private int ID;
    private String name = "";
    private int linkID = 0;

//    private ArrayList<BPPin> pins = new ArrayList<BPPin>();
    public ArrayList<BPPin> outputPins = new ArrayList<>();
    public ArrayList<BPPin> inputPins = new ArrayList<>();


    public BPNode(BPGraph graph){
        this.graph = graph;
    }

    public BPNode(BPGraph graph, int id){
        this.graph = graph;
        this.ID = id;
    }

    public abstract void execute();

    protected void setID(int id){
        this.ID = id;
    }

    public BPPin addOutputPin(BPPin.DataType dataType, BPNode node){
        int id = graph.getNextAvailablePinID();
        BPPin pin = new BPPin(node, id, dataType, BPPin.PinType.Output, linkID++);
        outputPins.add(pin);
        return pin;
    }

    public BPPin addInputPin(BPPin.DataType dataType, BPNode node){
        int id = graph.getNextAvailablePinID();
        BPPin pin = new BPPin(node, id, dataType, BPPin.PinType.Input, linkID++);
        inputPins.add(pin);
        return pin;
    }

    public void setName(String name){
        if(!graph.validateName(name)){
            return;
        }
        if(name == null){
            name = "";
        }
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getID()
    {
        return ID;
    }

    public BPGraph getGraph()
    {
        return graph;
    }

    /**
     * should return Variable name
     */
    public abstract String printSource(PrintWriter pw);

    /**
     * A unique ID for variables that are inside Functions that have been created without user input or a variable node
     */
    public static int getNextLocalVariableID(){
        return localeVariableID++;
    }

//    public ArrayList<BPPin> getPins()
//    {
//        return pins;
//    }

}
