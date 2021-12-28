package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.util.Random;

public class Node_Function extends BPNode{

    public Node_Function(BPGraph graph){
        super(graph);
        graph.addNode("function" + new Random().nextFloat(), this);
        BPPin pin = addOutputPin(BPPin.DataType.Flow);
    }

    @Override
    public void execute() {

    }
}
