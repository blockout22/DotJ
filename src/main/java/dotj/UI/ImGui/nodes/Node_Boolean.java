package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

public class Node_Boolean extends BPNode{

    public Node_Boolean(BPGraph graph){
        super(graph);
        graph.addNode("Boolean", this);

        BPPin execIn = addInputPin(BPPin.DataType.Flow);
        execIn.setName("Exec");
        BPPin boolIn = addInputPin(BPPin.DataType.Bool);
        boolIn.setName("Boolean");

        BPPin execTrueOut = addOutputPin(BPPin.DataType.Flow);
        execTrueOut.setName("TRUE");
        BPPin execFalseOut = addOutputPin(BPPin.DataType.Flow);
        execFalseOut.setName("FALSE");

    }

    @Override
    public void execute() {
    }
}
