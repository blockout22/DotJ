package dotj.UI.ImGui;

import dotj.input.GLFWKey;
import imgui.extension.imnodes.ImNodesContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.extension.imnodes.flag.ImNodesPinShape;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import imgui.internal.ImRect;
import imgui.type.ImInt;

import java.util.Random;
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



    private static void addRandomNode(BPGraph graph){
        Random r = new Random();
        final BPNode node = graph.addNode("new Node" + r.nextFloat());
        node.setName("Some Name");

        final BPPin pin = node.addPin(BPPin.DataType.Flow, BPPin.PinType.Input);
        pin.setName("In");

        final BPPin outPin = node.addPin(BPPin.DataType.Flow, BPPin.PinType.Output);
        outPin.setName("Out");
    }

    public static void show(final BPGraph graph){
        setNextWindowSize(500, 500, ImGuiCond.Once);
        setNextWindowPos(getMainViewport().getPosX() + 800, getMainViewport().getPosY() + 100, ImGuiCond.Once);

        if(begin("Blueprints")){

            text("Blueprint Editor");

            if(button("AddNode")){
                addRandomNode(graph);
            }

            beginNodeEditor();

            for(BPNode g : graph.nodes.values()){
                beginNode(g.ID);
                {
                    beginNodeTitleBar();
                    text(g.getName());
                    endNodeTitleBar();


                    for(BPPin pin : g.getPins())
                    {
                        addPin(pin.getID(), pin.getName(), pin.getDataType(), pin.getPinType());
                    }
                }
                endNode();
            }

            int uniqueLinkId = 1;
            for(BPNode g : graph.nodes.values()){
                for(BPPin pin : g.getPins()){
                    if(pin.connectedTo != -1) {
                        if (pin.getPinType() == BPPin.PinType.Output) {
                            link(uniqueLinkId++, pin.getID(), pin.connectedTo);
//                            System.out.println("linked: " + pin.getID() + ", " + graph.findByID(pin.connectedID).connectedID);
                        }
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

                        System.out.println(sourcePin.connectedTo + " : " + targetPin.connectedTo);

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

    private static void addPin(int pinID, String vName, BPPin.DataType dataType, BPPin.PinType kind){
        switch (kind){
            case Input:
                beginInputAttribute(pinID, ImNodesPinShape.Triangle);
//                setPinColor(vName, dataType);
                text(vName);
                endOutputAttribute();
                sameLine();
                break;
            case Output:
                beginOutputAttribute(pinID, ImNodesPinShape.Triangle);
//                setPinColor(vName, dataType);
                text(vName);
                endOutputAttribute();
                sameLine();
                break;
        }
    }

    private static void setPinColor(String text, BPPin.DataType type){
        switch (type){
            case Flow:
                textColored(1, 1, 1, 1,  text);
                break;
            case Bool:
                textColored(1, 0, 0, 1,  text);
                break;
            case Int:
                textColored(0, 1, 0, 1,  text);
                break;
            case Float:
                textColored(0, 1, .5f, 1,  text);
                break;
            case String:
                textColored(1, 1, 0, 1,  text);
                break;
            case Object:
                textColored(0, 1, 1, 1,  text);
                break;
            case Function:
                textColored(1, .5f, 0, 1,  text);
                break;

        }
    }
}
