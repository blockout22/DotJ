package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import dotj.UI.ImGui.Graph;
import imgui.type.ImString;

import java.io.PrintWriter;

public class Node_CallFunction extends BPNode {

    private BPPin input, flowIn, flowOut;

    public Node_CallFunction(BPGraph graph) {
        super(graph);
        graph.addUniqueNode("Call Function", this);

        flowIn = addInputPin(BPPin.DataType.Flow, this);
        input = addInputPin(BPPin.DataType.String, this);
        input.getData().setValue(new ImString());
        flowOut = addOutputPin(BPPin.DataType.Flow, this);
    }

    @Override
    public void execute() {

    }

    @Override
    public String printSource(PrintWriter pw) {
        NodeData<ImString> data = input.getData();
        pw.write(data.value + "();\n");

        NodeCompiler.nextNode(getGraph(), flowOut, pw);
        return null;
    }
}
