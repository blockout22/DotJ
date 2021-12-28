package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

public class Node_Boolean {

    public static void create(BPGraph graph){
        BPNode node = graph.addNode("Boolean");
        if(node == null){
            return;
        }

        BPPin execIn = node.addInputPin(BPPin.DataType.Flow);
        execIn.setName("Exec");
        BPPin boolIn = node.addInputPin(BPPin.DataType.Bool);
        boolIn.setName("Boolean");

        BPPin execTrueOut = node.addOutputPin(BPPin.DataType.Flow);
        execTrueOut.setName("TRUE");
        BPPin execFalseOut = node.addOutputPin(BPPin.DataType.Flow);
        execFalseOut.setName("FALSE");

    }
}
