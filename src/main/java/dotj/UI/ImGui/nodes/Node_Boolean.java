package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.io.PrintWriter;

public class Node_Boolean extends BPNode{

    public Node_Boolean(BPGraph graph){
        super(graph);
        graph.addNode("Boolean", this);

        BPPin execIn = addInputPin(BPPin.DataType.Flow, this);
        execIn.setName("Exec");
        BPPin boolIn = addInputPin(BPPin.DataType.Bool, this);
        boolIn.setName("Boolean");

        BPPin execTrueOut = addOutputPin(BPPin.DataType.Flow, this);
        execTrueOut.setName("TRUE");
        BPPin execFalseOut = addOutputPin(BPPin.DataType.Flow, this);
        execFalseOut.setName("FALSE");

    }

    @Override
    public void execute() {
    }

    @Override
    public String printSource(PrintWriter pw) {
        return null;
    }
}
