package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import imgui.type.ImInt;
import imgui.type.ImString;

public class Node_IntToString extends BPNode {

    private BPPin input, output;

    public Node_IntToString(BPGraph graph) {
        super(graph);
        graph.addNode("Int To String", this);

        input = addInputPin(BPPin.DataType.Int);
        output = addOutputPin(BPPin.DataType.String);
    }

    @Override
    public void execute() {
        NodeData<ImInt> in = input.getData();
        NodeData<ImString> out = output.getData();

        out.getValue().set(String.valueOf(in.getValue().get()));
        output.setName(out.value.get());
    }
}
