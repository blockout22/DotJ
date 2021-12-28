package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import imgui.type.ImString;

import java.util.Random;

public class Node_PrintConsole extends BPNode{

    private BPPin execPin, valuePin, output;

    public Node_PrintConsole (BPGraph graph){
        super(graph);
        graph.addNode("Print" + new Random().nextFloat(), this);

        execPin = addInputPin(BPPin.DataType.Flow);
        valuePin = addInputPin(BPPin.DataType.String);
        output = addOutputPin(BPPin.DataType.String);
    }

    @Override
    public void execute() {
        NodeData<ImString> data = valuePin.getData();
        NodeData<ImString> out = output.getData();

//        valuePin.setName(data.value.get());

        out.getValue().set(data.value.get());
        output.setName(out.value.get());
//        output.setName(data.value.get());
    }
}
