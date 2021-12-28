package dotj.UI.ImGui;

import dotj.UI.ImGui.nodes.*;
import dotj.input.GLFWKey;
import imgui.extension.imnodes.ImNodesContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.extension.imnodes.flag.ImNodesPinShape;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import imgui.internal.ImRect;
import imgui.type.ImInt;

import java.util.Vector;

import static imgui.ImGui.*;
import static imgui.extension.imnodes.ImNodes.*;

public class ImBlueprint {

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
            BPPin connectedPin = graph.findByID(executePin.connectedTo);
            System.out.println(connectedPin);
        }
    }

    public static void show(final BPGraph graph){
        setNextWindowSize(1000, 800, ImGuiCond.Once);
        setNextWindowPos(getMainViewport().getPosX() + 500, getMainViewport().getPosY() + 100, ImGuiCond.Once);

        if(begin("Blueprints")){

            text("Blueprint Editor");

            beginNodeEditor();


            //Render Nodes and Pins
            for(BPNode g : graph.nodes.values()){
                beginNode(g.getID());
                {
                    beginNodeTitleBar();
                    text(g.getName());
                    endNodeTitleBar();

                    //TODO temporary code, this is just to test the node ... When it's implemented
                    if(g.getName().startsWith("function")) {
                        if (button("Execute")) {
                            executeFlow(graph, g);
                        }
                    }
//                    curNodeSize = getItemRectSizeX();

                    int max = Math.max(g.outputPins.size(), g.inputPins.size());

                    for (int i = 0; i < max; i++) {

                        if(g.inputPins.size() > i){
                            BPPin inPin = g.inputPins.get(i);
//                            addPin(inPin.getID(), inPin.getName(), inPin.getDataType(), inPin.getPinType());
                            addPin(inPin);
                        }

                        dummy(250, 0);

                        if(g.outputPins.size() > i){
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
            for(BPNode g : graph.nodes.values()){
                for(BPPin pin : g.outputPins){
                    if(pin.connectedTo != -1){
                        link(uniqueLinkId++, pin.getID(), pin.connectedTo);
                    }
                }
            }
            miniMap(0.2f, ImNodesMiniMapLocation.BottomRight);
            endNodeEditor();


            //check if a link was attempted while dragging from a pin
            if(isLinkCreated(LINK_A, LINK_B)){
                final BPPin sourcePin = graph.findByID(LINK_A.get());
                final  BPPin targetPin = graph.findByID(LINK_B.get());

                if(sourcePin != null && targetPin != null){
                    //TODO do a check here to limit the number connections to the pins
                    if(sourcePin.connectedTo != targetPin.connectedTo || (targetPin.connectedTo == -1 || sourcePin.connectedTo == -1)){
                        sourcePin.connectedTo = targetPin.getID();
                        targetPin.connectedTo = sourcePin.getID();

                    }
                }
            }

            if(isLinkDropped(LINK_A, false)) {

                BPPin pin1 = graph.findByID(LINK_A.get());
                if (pin1.connectedTo != -1) {
                    BPPin pin2 = graph.findByID(pin1.connectedTo);
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
                    if(button("Delete " + graph.nodes.get(targetNode).getName())) {
                        graph.nodes.remove(targetNode);
                        closeCurrentPopup();
                    }
                    endPopup();
                }
            }

            //Opens Graph Right click menu
            if(isPopupOpen("context_menu")) {
                if (beginPopup("context_menu")) {

                    //Add <name> Should only be a variable type and be in another list, this menu should only add functions such as adding 2 numbers together
                    if (menuItem("Add Flow")) {
                        Node_Function.create(graph);
                        closeCurrentPopup();
                    }

                    if (menuItem("Bool Check")) {
                        Node_Boolean.create(graph);
                        closeCurrentPopup();
                    }

                    if (menuItem("Add")) {
                        Node_Add.create(graph);
                        closeCurrentPopup();
                    }

                    if(menuItem("Print")){
                        Node_PrintConsole.create(graph);
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
                    graph.nodes.remove(selectedNodes[i]);
                }
            }

        }
        end();
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
                if(pin.connectedTo == -1) {
                    pushItemWidth(100);
                    if (inputInt(pin.getName(), pin.getInt())) {

                    }
                    popItemWidth();
                }
                break;
            case Float:
                break;
            case String:
                break;
            case Object:
                break;
            case Function:
                break;

        }
    }
}
