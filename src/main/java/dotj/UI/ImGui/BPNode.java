package dotj.UI.ImGui;

import java.util.ArrayList;

public class BPNode {

    public final BPGraph graph;
    public final int ID;
    private String name = "";
    private int linkID = 0;

    private ArrayList<BPPin> pins = new ArrayList<BPPin>();


    public BPNode(BPGraph graph, int id){
        this.graph = graph;
        this.ID = id;
    }

    public BPPin addPin(BPPin.DataType dataType, BPPin.PinType pinType){
        int id = graph.getNextAvailablePinID();
        BPPin pin = new BPPin(id, dataType, pinType, linkID++);
        pins.add(pin);
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

    public ArrayList<BPPin> getPins()
    {
        return pins;
    }

}
