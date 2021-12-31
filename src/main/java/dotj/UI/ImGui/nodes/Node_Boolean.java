package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import imgui.type.ImBoolean;

import java.io.PrintWriter;

public class Node_Boolean extends BPNode{

    private BPPin boolIn, execTrueOut, execFalseOut;

    public Node_Boolean(BPGraph graph){
        super(graph);
        graph.addNode("Boolean", this);

        BPPin execIn = addInputPin(BPPin.DataType.Flow, this);
        execIn.setName("Exec");
        boolIn = addInputPin(BPPin.DataType.Bool, this);
        boolIn.getData().setValue(new ImBoolean());
        boolIn.setName("Boolean");

        execTrueOut = addOutputPin(BPPin.DataType.Flow, this);
        execTrueOut.setName("TRUE");

        execFalseOut = addOutputPin(BPPin.DataType.Flow, this);
        execFalseOut.setName("FALSE");

    }

    @Override
    public void execute() {
    }

    @Override
    public String printSource(PrintWriter pw) {
        NodeData<ImBoolean> data = boolIn.getData();

        String input = String.valueOf(data.value);

        if(boolIn.connectedTo != -1){
            BPPin pin = getGraph().findPinById(boolIn.connectedTo);
            input = pin.getNode().printSource(pw);
        }

        String outputVar = "bool" + getGraph().getNextLocalVariableID();
        pw.write("boolean " + outputVar + " = " + input + ";\n");
        pw.write("if(" + outputVar + "){\n");
        if(execTrueOut.connectedTo != -1) {
            BPPin pin = getGraph().findPinById(execFalseOut.connectedTo);
            String outTrue = pin.getNode().printSource(pw);
            if(outTrue != null) {
                pw.write(outTrue + "\n");
            }
        }
        pw.write("}else{\n");
        if(execFalseOut.connectedTo != -1) {
            BPPin pin = getGraph().findPinById(execFalseOut.connectedTo);
            String outFalse = pin.getNode().printSource(pw);
            if(outFalse != null) {
                pw.write(outFalse + "\n");
            }
        }
        pw.write("}\n");
        return null;
    }
}
