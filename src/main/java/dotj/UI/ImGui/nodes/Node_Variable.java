package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.io.PrintWriter;

public class Node_Variable extends BPNode {

    private BPPin output;

    public Node_Variable(BPGraph graph, BPPin.DataType type) {
        super(graph);
        graph.addNode("Variable", this);

        output = addInputPin(type, this);
    }

    @Override
    public void execute() {

    }

    @Override
    public String printSource(PrintWriter pw) {
        return null;
    }
}
