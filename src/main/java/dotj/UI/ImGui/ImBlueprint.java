package dotj.UI.ImGui;

import dotj.UI.ImGui.nodes.*;
import dotj.Utilities;
import dotj.input.GLFWKey;
import imgui.extension.imnodes.ImNodesContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.extension.imnodes.flag.ImNodesPinShape;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import imgui.internal.ImRect;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

import static imgui.ImGui.*;
import static imgui.ImGui.beginPopup;
import static imgui.extension.imnodes.ImNodes.*;

public class ImBlueprint {

    private static TextEditor EDITOR = new TextEditor();

    public static ImRect getItemRect(){
        return new ImRect(getItemRectMin(), getItemRectMax());
    }

    public static ImRect getRectExpanded(ImRect rect, float x, float y){
        ImRect result = rect;
        result.min.x -= x;
        result.min.y -= y;
        result.max.x -= x;
        result.max.y -= y;
        return result;
    }

    static int s_PinIconSize = 24;
    Vector<Link> s_Links;
//    ImTextureID

    private static final ImNodesContext CONTEXT = new ImNodesContext();
    private static final ImInt LINK_A = new ImInt();
    private static final ImInt LINK_B = new ImInt();

    private static float curNodeSize;

    /**
     * used to update all nodes and values
     */
    private static void executeFlow(BPGraph graph, BPNode executeNode){
        BPPin executePin = null;
        for(BPPin curPin : executeNode.outputPins){
            if(curPin.getDataType() == BPPin.DataType.Flow){
                executePin = curPin;
                break;
            }
        }

        if(executePin != null){
            BPPin connectedPin = graph.findPinById(executePin.connectedTo);
            System.out.println(connectedPin);
        }
    }

    public static void show(final BPGraph graph){
        updateNodes(graph);
        graph.update();
        setNextWindowSize(1000, 800, ImGuiCond.Once);
        setNextWindowPos(getMainViewport().getPosX() + 500, getMainViewport().getPosY() + 100, ImGuiCond.Once);

        String title = "Blueprints";

        if(begin(title)){

            if(button("Create Source")){
                try {
                    createSource(graph, title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            text("Graph");
            if(beginTabBar("GraphTabBar")) {
                if (beginTabItem("Graph")) {
                    beginNodeEditor();
                    {
                        //Render Nodes and Pins
                        for (BPNode g : graph.getNodes().values()) {
                            beginNode(g.getID());
                            {
                                beginNodeTitleBar();
                                text(g.getName());
                                endNodeTitleBar();

                                //TODO temporary code, this is just to test the node ... When it's implemented
                                if (g.getName().startsWith("function")) {
                                    if (button("Execute")) {
                                        executeFlow(graph, g);
                                    }
                                }

//                    curNodeSize = getItemRectSizeX();

                                int max = Math.max(g.outputPins.size(), g.inputPins.size());

                                for (int i = 0; i < max; i++) {

                                    if (g.inputPins.size() > i) {
                                        BPPin inPin = g.inputPins.get(i);
//                            addPin(inPin.getID(), inPin.getName(), inPin.getDataType(), inPin.getPinType());
                                        addPin(inPin);
                                    }

                                    dummy(150, 0);

                                    if (g.outputPins.size() > i) {
                                        BPPin outPin = g.outputPins.get(i);
//                            addPin(outPin.getID(), outPin.getName(), outPin.getDataType(), outPin.getPinType());
                                        addPin(outPin);
                                    }
                                    newLine();
                                }


//                    for(BPPin pin : g.getPins())
//                    {
//                        System.out.println("Pin: " + pin.getID() + " : " + g.ID);
//                        addPin(pin.getID(), pin.getName(), pin.getDataType(), pin.getPinType());
//                    }
                            }
                            endNode();
                        }

                        int uniqueLinkId = 1;
                        for (BPNode g : graph.getNodes().values()) {
                            for (BPPin pin : g.outputPins) {
                                if (pin.connectedTo != -1) {
                                    link(uniqueLinkId++, pin.getID(), pin.connectedTo);
                                }
                            }
                        }
                        miniMap(0.2f, ImNodesMiniMapLocation.BottomRight);
                    }
                    endNodeEditor();
                    endTabItem();
                }

                if(beginTabItem("Output")){
                    EDITOR.render("Output");
                    EDITOR.setReadOnly(true);
                    endTabItem();
                }
            }
            endTabBar();
            sameLine();

            //check if a link was attempted while dragging from a pin
            if(isLinkCreated(LINK_A, LINK_B)){
                final BPPin sourcePin = graph.findPinById(LINK_A.get());
                final  BPPin targetPin = graph.findPinById(LINK_B.get());

                //check if the pins are of the same type
                if(!(sourcePin.getDataType() == targetPin.getDataType())){
                    System.out.println("Types are not the same");
                }else {

                    //disconnect old pins
                    if (sourcePin.connectedTo != -1) {
                        BPPin oldPin = graph.findPinById(sourcePin.connectedTo);
                        oldPin.connectedTo = -1;
                    }

                    if (targetPin.connectedTo != -1) {
                        BPPin oldPin = graph.findPinById(targetPin.connectedTo);
                        oldPin.connectedTo = -1;
                    }

                    if (sourcePin != null && targetPin != null) {
                        //TODO do a check here to limit the number connections to the pins
                        if (sourcePin.connectedTo != targetPin.connectedTo || (targetPin.connectedTo == -1 || sourcePin.connectedTo == -1)) {
                            sourcePin.connectedTo = targetPin.getID();
                            targetPin.connectedTo = sourcePin.getID();
                        }
                    }
                }
            }

            if(isLinkDropped(LINK_A, false)) {

                BPPin pin1 = graph.findPinById(LINK_A.get());
                if (pin1.connectedTo != -1) {
                    BPPin pin2 = graph.findPinById(pin1.connectedTo);
                    System.out.println("Dropped: " + pin1.connectedTo + " : " + pin2.connectedTo);

                    pin1.connectedTo = -1;
                    pin2.connectedTo = -1;
                }
            }


            //mouse events
            if(isMouseClicked(ImGuiMouseButton.Right)){
                final int hoveredNode = getHoveredNode();
                if(hoveredNode != -1){
                    //create popup
                    openPopup("node_menu");
                    //stores the ID of the node that was Right clicked
                    getStateStorage().setInt(getID("delete_node_id"), hoveredNode);
                }else{
                    openPopup("context_menu");
//                    getStateStorage().setInt(getID("context_menu"), 0);
                }
            }

            //Open Popups
            if(isPopupOpen("node_menu")){
                //gets the ID of the selected Node
                final int targetNode = getStateStorage().getInt(getID("delete_node_id"));

                //visualizes open popup Popup
                if(beginPopup("node_menu")){
                    if(button("Delete " + graph.getNodes().get(targetNode).getName())) {
//                        graph.nodes.remove(targetNode);
                        graph.removeNode(targetNode);
                        closeCurrentPopup();
                    }
                    ArrayList<BPPin> pinList = graph.getNodes().get(targetNode).outputPins;
                    for(BPPin out : pinList){
                        if(button("Pin: " + out.getName())) {
                            NodeData<Integer> d = out.getData();
                            closeCurrentPopup();
                        }
                    }
                    endPopup();
                }
            }

            //Opens Graph Right click menu
            if(isPopupOpen("context_menu")) {
                if (beginPopup("context_menu")) {

                    //Add <name> Should only be a variable type and be in another list, this menu should only add functions such as adding 2 numbers together
                    if (menuItem("Add Flow")) {
//                        Node_Function.create(graph);
                        Node_Function func = new Node_Function(graph);
                        closeCurrentPopup();
                    }

                    if(menuItem("Call Function")){
                        Node_CallFunction callFunc = new Node_CallFunction(graph);
                        closeCurrentPopup();
                    }

                    if (menuItem("Bool Check")) {
                        Node_Boolean bool = new Node_Boolean(graph);
//                        Node_Boolean.create(graph);
                        closeCurrentPopup();
                    }

                    if (menuItem("Add")) {
                        Node_Add node = new Node_Add(graph);
                        closeCurrentPopup();
                    }

                    if(menuItem("Print")){
//                        Node_PrintConsole.create(graph);
                        Node_PrintConsole console = new Node_PrintConsole(graph);
                        closeCurrentPopup();
                    }

                    if(menuItem("Int To String")){
                        Node_IntToString intToString = new Node_IntToString(graph);
                        closeCurrentPopup();
                    }
                    endPopup();
                }
            }

            //TODO Handle connectedTo variable when delete nodes 

            //Remove Selected nodes using the delete key
            if(isKeyPressed(GLFWKey.KEY_DELETE.getKeyCode()))
            {
                int[] selectedNodes = new int[numSelectedNodes()];
                getSelectedNodes(selectedNodes);
                for(int i = 0; i < selectedNodes.length; i++){
//                    graph.nodes.remove(selectedNodes[i]);
                    graph.removeNode(selectedNodes[i]);
                }
            }

        }
        end();
    }

    private static void createSource(BPGraph graph, String title) throws IOException {
        NodeCompiler.compile(title, graph);
        EDITOR.setText(NodeCompiler.lastOutput.toString());
    }

    //Old code from visual coding
    {

//    private static void handleNode(BPGraph graph, BPNode curNode, PrintWriter pw){
//        handleFlowPins(curNode, graph, pw);
//
//        for(BPPin outputPin : curNode.outputPins){
//            if(outputPin.getDataType() == BPPin.DataType.Flow){
//                if(outputPin.connectedTo != -1) {
//                    BPNode node = graph.findPinById(outputPin.connectedTo).getNode();
//                    System.out.println("Flow: " + node.getName() + " : " + outputPin.connectedTo);
//                    handleNode(graph, node, pw);
//                }
//            }
//        }
//    }

        /**
         * runs down the line of flow pins creating everything required inside a function
         */
//    public static void handleFlowPins(BPNode nodes, BPGraph graph, PrintWriter pw){
//        for(BPPin flowPin : nodes.outputPins){
//            if(flowPin.getDataType() == BPPin.DataType.Flow) {
//                //go down line and find all other nodes connected to the function
//                BPPin pin = graph.findPinById(flowPin.connectedTo);
//                if(pin != null){
//                    pin.getNode().printSource(pw);
////                    writeLine(pw, pin.getNode().getCodeOutput());
//                }
//                break;
//            }
//        }
//    }

//    private static void oldCreate(BPGraph graph, String title) throws IOException{
//        File file = new File("GraphOutput/" + title + ".java");
//
//        if(!file.exists()){
//            file.createNewFile();
//        }
//
//        PrintWriter pw = new PrintWriter(file);
//        writeLine(pw, "public class " + title);
//        writeLine(pw, "{");
//
//        for(BPNode curNode : graph.getNodes().values()){
//            //Start of function
//            //Find functions
//            if(curNode instanceof Node_Function){
//                writeLine(pw, "");
//                writeLine(pw, "private void " + curNode.getName() + "()");
//                writeLine(pw, "{");
//                handleNode(graph, curNode, pw);
//                writeLine(pw, "}");
//                writeLine(pw, "");
//            }
//        }
//
//        writeLine(pw, "}");
//        pw.flush();
//        pw.close();
//
//        //make pretty
//        BufferedReader br = new BufferedReader(new FileReader(file));
//        StringBuilder sb = new StringBuilder();
//        String line;
//        int tabCount = 0;
//        String previousLine = "";
//        while((line = br.readLine()) != null){
//            char previousChar = 0;
//            for(char c : previousLine.toCharArray()){
//                if(c == '{' && previousChar != '"'){
//                    tabCount++;
//                }
//            }
//
//            //may run into a bug here with previousChar... possible solution create a previousChar variable for toCharArray
//            for(char c : line.toCharArray()){
//                if(c == '}' && previousChar != '"'){
//                    tabCount --;
//                }
//                previousChar = c;
//            }
//
//            previousLine = line;
//
//            for(int i = 0; i < tabCount; i++){
//                sb.append("\t");
//            }
//            sb.append(line);
//            sb.append("\n");
//        }
//
//        PrintWriter formatedPw = new PrintWriter(file);
//        formatedPw.write(sb.toString());
//        formatedPw.flush();
//        formatedPw.close();
//    }

        /**
         * writes to the print writer and creates a new line
         */
//    private static void writeLine(PrintWriter pw, String text){
//        pw.write(text + "\n");
//    }
    }

    private static void updateNodes(BPGraph graph){
        for(BPNode nodes : graph.getNodes().values()){
            for (int i = 0; i < nodes.outputPins.size(); i++) {
                BPPin pin = nodes.outputPins.get(i);
//                System.out.println(pin.connectedTo);
                if(pin.connectedTo != -1){
                    BPPin output = graph.findPinById(pin.connectedTo);
//                    if(Utilities.notNull(outData, pin.getData())) {
                    switch (pin.getDataType()){
                        case Int:
//                                pin.getData().value = outData.value;
                            NodeData<ImInt> intOutData = output.getData();
                            NodeData<ImInt> intInputValue = pin.getData();
                            intOutData.getValue().set(intInputValue.value.get());
//                                outData.setValue();
                            break;
                        case String:
                            NodeData<ImString> stringOutData = output.getData();
                            NodeData<ImString> stringInputValue = pin.getData();
                            stringOutData.getValue().set(stringInputValue.value.get());
                            break;
                    }
//                    }
                }

            }
            nodes.execute();
        }
    }

    private static void addPin(BPPin pin){
        switch (pin.getPinType()){
            case Input:
                switch (pin.getDataType()){
                    case Flow:
                        beginInputAttribute(pin.getID(), ImNodesPinShape.Triangle);
                        break;
                    default:
                        beginInputAttribute(pin.getID(), ImNodesPinShape.CircleFilled);
                }
                configurePinType(pin);
                endOutputAttribute();
                sameLine();
                break;
            case Output:
                switch (pin.getDataType()){
                    case Flow:
                        beginOutputAttribute(pin.getID(), ImNodesPinShape.Triangle);
                        break;
                    default:
                        beginOutputAttribute(pin.getID(), ImNodesPinShape.CircleFilled);
                }
                sameLine(curNodeSize / 2);
//                configurePinType(pin);
                text(pin.getName());
                endOutputAttribute();
                sameLine();
                break;
        }
    }

    private static void configurePinType(BPPin pin){
        switch (pin.getDataType()){
            case Flow:
                break;
            case Bool:
                if(pin.connectedTo == -1){
                    if(checkbox(pin.getName(), pin.getBoolean()))
                    {

                    }
                }
                break;
            case Int:
//                if(pin.connectedTo == -1) {
                    pushItemWidth(100);
                    NodeData<ImInt> data = pin.getData();
                    if(Utilities.notNull(data)) {
                        if (inputInt(pin.getName(), data.value)) {

                        }
                    }
                    popItemWidth();
//                }
                break;
            case Float:
                break;
            case String:
                pushItemWidth(50);
                NodeData<ImString> stringData = pin.getData();
                if(inputText(pin.getName(), stringData.value)){

                }
                popItemWidth();
                break;
            case Object:
                break;
            case Function:
                break;

        }
    }
}
