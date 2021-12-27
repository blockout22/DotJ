package dotj.UI.ImGui;

import imgui.ImGui;
import imgui.ImGuiStyle;
import imgui.extension.nodeditor.NodeEditor;
import imgui.extension.nodeditor.NodeEditorConfig;
import imgui.extension.nodeditor.NodeEditorContext;
import imgui.extension.nodeditor.flag.NodeEditorPinKind;
import imgui.flag.ImGuiCond;
import imgui.type.ImBoolean;
import imgui.type.ImLong;

public class ExampleImGuiNodeEditor {
    private static final NodeEditorContext CONTEXT;
    private static final String URL = "https://github.com/thedmd/imgui-node-editor/tree/687a72f940";

    static {
        NodeEditorConfig config = new NodeEditorConfig();
        config.setSettingsFile(null);
        CONTEXT = new NodeEditorContext(config);
    }

    public static void show(final ImBoolean showImNodeEditorWindow, final Graph graph, float posX, float posY) {
        ImGui.setNextWindowSize(500, 400, ImGuiCond.Once);
        ImGui.setNextWindowPos(ImGui.getMainViewport().getPosX() + posX, ImGui.getMainViewport().getPosY() + posY, ImGuiCond.Once);
        if (ImGui.begin("imgui-node-editor Demo", showImNodeEditorWindow)) {
            ImGui.text("This a demo graph editor for imgui-node-editor");

            ImGui.alignTextToFramePadding();

            if (ImGui.button("Navigate to content")) {
                NodeEditor.navigateToContent(1);
            }

            NodeEditor.setCurrentEditor(CONTEXT);
            NodeEditor.begin("Node Editor");

            for (Graph.GraphNode node : graph.nodes.values()) {
                NodeEditor.beginNode(node.nodeId);

                ImGui.text(node.getName());

                NodeEditor.beginPin(node.getInputPinId(), NodeEditorPinKind.Input);
                ImGui.text("-> In");
                NodeEditor.endPin();

                ImGui.sameLine();

                NodeEditor.beginPin(node.getOutputPinId(), NodeEditorPinKind.Output);
                ImGui.text("Out ->");
                NodeEditor.endPin();

                NodeEditor.endNode();
            }

            if (NodeEditor.beginCreate()) {
                final ImLong a = new ImLong();
                final ImLong b = new ImLong();
                if (NodeEditor.queryNewLink(a, b)) {
                    final Graph.GraphNode source = graph.findByOutput(a.get());
                    final Graph.GraphNode target = graph.findByInput(b.get());
                    if (source != null && target != null && source.outputNodeId != target.nodeId && NodeEditor.acceptNewItem()) {
                        source.outputNodeId = target.nodeId;
                    }
                }
            }
            NodeEditor.endCreate();

            int uniqueLinkId = 1;
            for (Graph.GraphNode node : graph.nodes.values()) {
                if (graph.nodes.containsKey(node.outputNodeId)) {
                    NodeEditor.link(uniqueLinkId++, node.getOutputPinId(), graph.nodes.get(node.outputNodeId).getInputPinId());
                }
            }

            NodeEditor.suspend();

            final long nodeWithContextMenu = NodeEditor.getNodeWithContextMenu();
            if (nodeWithContextMenu != -1) {
                ImGui.openPopup("node_context");
                ImGui.getStateStorage().setInt(ImGui.getID("delete_node_id"), (int) nodeWithContextMenu);
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

            if (NodeEditor.showBackgroundContextMenu()) {
                ImGui.openPopup("node_editor_context");
            }

            if (ImGui.beginPopup("node_editor_context")) {
                if (ImGui.button("Create New Node")) {
                    final Graph.GraphNode node = graph.createGraphNode();
                    final float canvasX = NodeEditor.toCanvasX(ImGui.getMousePosX());
                    final float canvasY = NodeEditor.toCanvasY(ImGui.getMousePosY());
                    NodeEditor.setNodePosition(node.nodeId, canvasX, canvasY);
                    ImGui.closeCurrentPopup();
                }
                ImGui.endPopup();
            }

            NodeEditor.resume();
            NodeEditor.end();
        }
        ImGui.end();
    }
}
