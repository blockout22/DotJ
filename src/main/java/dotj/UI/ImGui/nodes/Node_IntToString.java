package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.io.PrintWriter;
import java.util.Random;

public class Node_IntToString extends BPNode {

    private BPPin input, output;

    public Node_IntToString(BPGraph graph) {
        super(graph);
        graph.addNode("Int To String ", this);

        input = addInputPin(BPPin.DataType.Int, this);
        output = addOutputPin(BPPin.DataType.String, this);
    }

    @Override
    public void execute() {
        NodeData<ImInt> in = input.getData();
        NodeData<ImString> out = output.getData();

        out.getValue().set(String.valueOf(in.getValue().get()));
        output.setName(out.value.get());
    }

    @Override
    public String printSource(PrintWriter pw) {
        //this is the variable as the value to use if pin is connected
        String variable = null;
        if(input.connectedTo != -1){
            BPPin pin = getGraph().findPinById(input.connectedTo);
            variable = pin.getNode().printSource(pw);
        }else{
            NodeData<ImInt> data = input.getData();
            variable = String.valueOf(data.value.get());
        }

        //this is the variable to set to store the above variable
        //variable should be randomized
        String outputvar = "intToString" + getGraph().getNextLocalVariableID();
        pw.write("String "+ outputvar +" = String.valueOf(" + variable + ");\n");
        return outputvar;
    }
}
