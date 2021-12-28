package dotj.UI.ImGui;

import java.util.HashMap;
import java.util.Map;

public class BPGraph {

    public final Map<Integer, BPNode> nodes = new HashMap<>();
    private int nextNodeID = 1;
    private int nextPinID = 1;

    public BPNode addNode(String name){
        if(!validateName(name)){
            return null;
        }
        final BPNode node = new BPNode(this, nextNodeID++);
        node.setName(name);
        nodes.put(node.ID, node);
        return node;
    }

    public boolean validateName(String name){
        if(nodes.size() > 0){
            for(BPNode g : nodes.values()){
                //check if same node name exists and return null if it already exists
                if(name.equals(g.getName())){
                    System.out.println("Equal");
                    return false;
                }
            }
        }
        return true;
    }

    public BPPin findByID(final int ID){
        for(BPNode node : nodes.values()){
            for(BPPin pin : node.getPins()){
                if(pin.getID() == ID){
                    return pin;
                }
            }
        }
        return null;
    }

    public int getNextAvailablePinID(){
        return nextPinID++;
    }

}
