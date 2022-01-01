package dotj.UI.ImGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BPGraph {

    private static int localeVariableID = 0;
    private final Map<Integer, BPNode> nodes = new HashMap<>();
//    private final Map<Integer, BPNode> queuedForRemoval = new HashMap<>();
    private ArrayList<Integer> queuedForRemoval = new ArrayList<>();
    private int nextNodeID = 1;
    private int nextPinID = 100;

//    public BPNode addNode(String name){
//        if(!validateName(name)){
//            return null;
//        }
//        BPNode node = new BPNode(this, nextNodeID++);
//        node.setName(name);
//        nodes.put(node.getID(), node);
//        return node;
//    }

    public void update(){
        for(Integer q : queuedForRemoval){
            BPNode n = nodes.get(q);
            for(BPPin pin : n.outputPins){
                if (pin.connectedTo != -1) {
                    BPPin oldPin = findPinById(pin.connectedTo);
                    oldPin.connectedTo = -1;
                }
            }
            for(BPPin pin : n.inputPins){
                if (pin.connectedTo != -1) {
                    BPPin oldPin = findPinById(pin.connectedTo);
                    oldPin.connectedTo = -1;
                }
            }
            nodes.remove(q);
        }
        queuedForRemoval.clear();
    }

    public boolean addUniqueNode(String name, BPNode node) {
        if (!validateName(name)) {
            return false;
        }
        return addNode(name, node);
    }

    public boolean addNode(String name, BPNode node){
        node.setID(nextNodeID++);
        node.setName(name);
        nodes.put(node.getID(), node);
        return true;
    }

    public void removeNode(int node){
        queuedForRemoval.add(node);
    }

    public Map<Integer, BPNode> getNodes()
    {
        return nodes;
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

    public BPPin findPinById(final int ID){
        for(BPNode node : nodes.values()){
//            for(BPPin pin : node.getPins()){
//                if(pin.getID() == ID){
//                    return pin;
//                }
//            }

            for(BPPin p : node.inputPins){
                if(p.getID() == ID){
                    return p;
                }
            }

            for(BPPin p : node.outputPins){
                if(p.getID() == ID){
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * A unique ID for variables that are inside Functions that have been created without user input or a variable node
     */
    public static int getNextLocalVariableID(){
        return localeVariableID++;
    }

    public int getNextAvailablePinID(){
        return nextPinID++;
    }

}
