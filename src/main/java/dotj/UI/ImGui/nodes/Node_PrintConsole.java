package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.util.Random;

public class Node_PrintConsole {

    public static void create(BPGraph graph){
        BPNode node = graph.addNode("Print" + new Random().nextFloat());

        BPPin pin = node.addInputPin(BPPin.DataType.Flow);
    }
}
