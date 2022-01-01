package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import imgui.type.ImInt;

import java.io.PrintWriter;
import java.util.Random;

public class Node_Add extends BPNode {

    private BPPin in1Pin, in2Pin, output;

    public Node_Add(BPGraph graph) {
        super(graph);
        graph.addNode("TestNode", this);

        in1Pin = addInputPin(BPPin.DataType.Int, this);
        in1Pin.setName("Input 1");
//        in1Pin.getData().setValue(new ImInt());

        in2Pin = addInputPin(BPPin.DataType.Int, this);
        in2Pin.setName("Input 2");
//        in2Pin.getData().setValue(new ImInt());
        System.out.println(in2Pin.getData() == null);

        output = addOutputPin(BPPin.DataType.Int, this);
//        output.getData().setValue(new ImInt());

        NodeData<Integer> data = new NodeData<>();
    }

    @Override
    public void execute() {
        NodeData<ImInt> pin1 = in1Pin.getData();
        NodeData<ImInt> pin2 = in2Pin.getData();

        NodeData<ImInt> out = output.getData();
        out.getValue().set(pin1.value.get() + pin2.value.get());

        output.setName(output.getData().value + "");
    }

    @Override
    public String printSource(PrintWriter pw) {
        NodeData<ImInt> pin2 = in2Pin.getData();
        NodeData<ImInt> pin1 = in1Pin.getData();

        String input1 = String.valueOf(pin1.value.get());
        String input2 = String.valueOf(pin2.value.get());

        if(in1Pin.connectedTo != -1){
            BPPin pin = getGraph().findPinById(in1Pin.connectedTo);
            input1 = pin.getNode().printSource(pw);
        }

        if(in2Pin.connectedTo != -1){
            BPPin pin = getGraph().findPinById(in2Pin.connectedTo);
            input2 = pin.getNode().printSource(pw);
        }

        String outputvar = "intAdd" + getGraph().getNextLocalVariableID();
        pw.write("int " + outputvar + " = " + input1 + " + " + input2 + ";\n");
        return outputvar;
    }
}
