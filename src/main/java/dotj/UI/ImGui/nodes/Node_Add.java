package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.util.Random;

public class Node_Add {

    public static void create(BPGraph graph){
        BPNode node = graph.addNode("Add" + new Random().nextFloat());

        if(node == null){
            return;
        }

        BPPin in1Pin = node.addInputPin(BPPin.DataType.Int);
        in1Pin.setName("Input 1");
        BPPin in2Pin = node.addInputPin(BPPin.DataType.Int);
        in2Pin.setName("Input 2");

        BPPin output = node.addOutputPin(BPPin.DataType.Int);
    }
}
