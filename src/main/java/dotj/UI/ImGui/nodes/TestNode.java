package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;
import dotj.Utilities;
import imgui.type.ImInt;

import java.util.Random;

public class TestNode extends BPNode {

    private BPPin in1Pin, in2Pin, output;

    public TestNode(BPGraph graph) {
        super(graph);
        graph.addNode("TestNode" + new Random().nextFloat(), this);

        in1Pin = addInputPin(BPPin.DataType.Int);
        in1Pin.setName("Input 1");
        in1Pin.getData().setValue(new ImInt());
        System.out.println(in1Pin.getData() == null);

        in2Pin = addInputPin(BPPin.DataType.Int);
        in2Pin.setName("Input 2");
        in2Pin.getData().setValue(new ImInt());
        System.out.println(in2Pin.getData() == null);

        output = addOutputPin(BPPin.DataType.Int);
        output.getData().setValue(new ImInt());

        NodeData<Integer> data = new NodeData<>();
    }

    @Override
    public void execute() {
//        pin1Int.set((Integer)(in1Pin.getData().value));
//        pin2Int.set((Integer)(in2Pin.getData().value));
//        int in1 = pin1Int.get();
//        int in2 = pin2Int.get();
//
//        int value = in1 + in2;
//        outInt.set(value);
//        output.setName("answer" + output.getData().value);

        NodeData<ImInt> pin1 = in1Pin.getData();
        NodeData<ImInt> pin2 = in2Pin.getData();

        NodeData<ImInt> out = output.getData();
        out.getValue().set(pin1.value.get() + pin2.value.get());

        output.setName(output.getData().value + "");


        //update Data Values
//        in1Pin.getData().setValue(pin1Int);
//        in2Pin.getData().setValue(pin2Int);
//        output.getData().setValue(outInt);
    }
}
