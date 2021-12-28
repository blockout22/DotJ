package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.util.Random;

public class Node_Function {

    public static void create(BPGraph graph){
        BPNode node = graph.addNode("function" + new Random().nextFloat());
        BPPin pin = node.addOutputPin(BPPin.DataType.Flow);
    }
}
