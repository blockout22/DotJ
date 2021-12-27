package dotj.UI.ImGui;

import imgui.ImGui;
import imgui.extension.imnodes.ImNodes;
import imgui.extension.imnodes.ImNodesContext;
import imgui.extension.imnodes.flag.ImNodesMiniMapLocation;
import imgui.extension.imnodes.flag.ImNodesPinShape;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiMouseButton;
import imgui.type.ImBoolean;
import imgui.type.ImInt;

import static imgui.ImGui.*;
import static imgui.extension.imnodes.ImNodes.*;

public class ExampleImNodes {


    private static final ImNodesContext CONTEXT = new ImNodesContext();

    private static final ImInt LINK_A = new ImInt();
    private static final ImInt LINK_B = new ImInt();

    static{
        ImNodes.createContext();
    }

    public static void show(final ImBoolean showImNodesWindow, final Graph graph){
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + 100, ImGui.getMainViewport().getPosY() + 100, ImGuiCond.Once);

        if(ImGui.begin("ImNodes Demo", showImNodesWindow)){
            text("This is a demo graphi Editor for ImNodes");

            ImGui.alignTextToFramePadding();
            text("Text Label: ");
            ImGui.sameLine();

            if(ImGui.button("Click me button")){
                System.out.println("I have been clicked");
            }

            editorContextSet(CONTEXT);
            beginNodeEditor();

            for(Graph.GraphNode node : graph.nodes.values()) {
                beginNode(node.nodeId);

                beginNodeTitleBar();;
                text(node.getName());
                endNodeTitleBar();

                beginInputAttribute(node.getInputPinId(), ImNodesPinShape.CircleFilled);
                text("In");
                endInputAttribute();

                sameLine();

                beginOutputAttribute(node.getOutputPinId());
                text("Out");
                endOutputAttribute();

                endNode();
            }

            int uniqueLinkId = 1;
            for(Graph.GraphNode node : graph.nodes.values()){
                if(graph.nodes.containsKey(node.outputNodeId)){
                    link(uniqueLinkId++, node.getOutputPinId(), graph.nodes.get(node.outputNodeId).getInputPinId());
                }
            }

            final boolean isEditorHovered = isEditorHovered();

            miniMap(0.2f, ImNodesMiniMapLocation.BottomRight);
            endNodeEditor();

            if(isLinkCreated(LINK_A, LINK_B)){
                final Graph.GraphNode source = graph.findByInput(LINK_A.get());
                final Graph.GraphNode target = graph.findByInput(LINK_B.get());

                if(source != null && target != null && source.outputNodeId != target.nodeId){
                    source.outputNodeId = target.nodeId;
                }
            }

            if(isMouseClicked(ImGuiMouseButton.Right)){
                final int hoveredNode = getHoveredNode();
                if(hoveredNode != -1){
                    openPopup("node_context");
                    getStateStorage().setInt(ImGui.getID("delete_node_id"), hoveredNode);
                }else{
                    openPopup("node_editor_context");
                }
            }

            if (ImGui.isPopupOpen("node_context")) {
                final int targetNode = ImGui.getStateStorage().getInt(ImGui.getID("delete_node_id"));
                if (ImGui.beginPopup("node_context")) {
                    if (ImGui.button("Delete " + graph.nodes.get(targetNode).getName())) {
                        graph.nodes.remove(targetNode);
                        ImGui.closeCurrentPopup();
                    }
                    ImGui.endPopup();
                }
            }

            if (ImGui.beginPopup("node_editor_context")) {
                if (ImGui.button("Create New Node")) {
                    final Graph.GraphNode node = graph.createGraphNode();
                    ImNodes.setNodeScreenSpacePos(node.nodeId, ImGui.getMousePosX(), ImGui.getMousePosY());
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }

        }
        end();
    }
}
