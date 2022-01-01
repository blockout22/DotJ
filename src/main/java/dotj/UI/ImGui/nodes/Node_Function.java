package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.io.PrintWriter;
import java.util.Random;

public class Node_Function extends BPNode{

    public BPPin flowPin;

    public Node_Function(BPGraph graph){
        super(graph);
        graph.addNode("function" + new Random().nextInt(10000), this);
        flowPin = addOutputPin(BPPin.DataType.Flow, this);
    }

    @Override
    public void execute() {

    }

    @Override
    public String printSource(PrintWriter pw) {
        if(flowPin.connectedTo != -1) {
            BPPin pin = getGraph().findPinById(flowPin.connectedTo);
            NodeCompiler.nextNode(getGraph(), pin.getNode(), pw);
        }
        return "";
    }
}
