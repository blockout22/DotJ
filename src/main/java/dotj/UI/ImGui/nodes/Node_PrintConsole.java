package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import dotj.UI.ImGui.ImBlueprint;
import imgui.type.ImString;

import java.io.PrintWriter;
import java.util.Random;

public class Node_PrintConsole extends BPNode{

    private BPPin execPin, valuePin, output;

    public Node_PrintConsole (BPGraph graph){
        super(graph);
        graph.addNode("Print" + new Random().nextFloat(), this);

        execPin = addInputPin(BPPin.DataType.Flow, this);
        valuePin = addInputPin(BPPin.DataType.String, this);
        output = addOutputPin(BPPin.DataType.Flow, this);
    }

    @Override
    public void execute() {
        NodeData<ImString> data = valuePin.getData();
        NodeData<ImString> out = output.getData();

//        valuePin.setName(data.value.get());

//        out.getValue().set(data.value.get());
//        output.setName(out.value.get());
//        output.setName(data.value.get());

    }

    @Override
    public String printSource(PrintWriter pw) {
        NodeData<ImString> data = valuePin.getData();
        String strOutput = "\"" + data.value.get() + "\"";
        if(valuePin.connectedTo != -1) {
            BPPin pin = getGraph().findPinById(valuePin.connectedTo);
            strOutput = pin.getNode().printSource(pw);
        }

//        if(output.connectedTo != -1){
//            ImBlueprint.h
//        }

        pw.write("System.out.println(" + strOutput + ");\n");

        return "";
    }
}
