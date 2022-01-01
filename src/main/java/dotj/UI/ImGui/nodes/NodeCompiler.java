package dotj.UI.ImGui.nodes;

import dotj.UI.ImGui.BPGraph;
import dotj.UI.ImGui.BPNode;
import dotj.UI.ImGui.BPPin;

import java.io.*;

public class NodeCompiler {

    public static StringBuilder lastOutput = new StringBuilder();

    public static void compile(String name, BPGraph graph) throws IOException {
        File file = new File("GraphOutput/" + name + ".java");

        File dir = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));
        //create Output Dir if it doesn't exist
        if(!dir.exists()){
            dir.mkdirs();
        }

        //create Source file if it doesn't exist
        if(!file.exists()){
            file.createNewFile();
        }

        PrintWriter pw = new PrintWriter(file);
        //setup base class with 'name'
        writeLine(pw, "public class " + name);
        writeLine(pw, "{");

        //loop through all nodes in the graph and look for functions
        for(BPNode curNode : graph.getNodes().values()){
            //check if current BPNode is a function
            if(curNode instanceof Node_Function){
                //write a blank line for spacing
                writeLine(pw, "");
                //create new function with the nodes name
                writeLine(pw, "private void " + curNode.getName() + "()");
                writeLine(pw, "{");
                //check node for any additional pins and execute a specific action based on the pin
                handleNode(graph, curNode, pw);
                //close off current function
                writeLine(pw, "}");
            }
        }

        //close off class
        writeLine(pw, "}");
        //save and close file
        pw.flush();
        pw.close();

        formatFile(file);
    }

    public static void handleNode(BPGraph graph, BPNode curNode, PrintWriter pw){
        nextNode(graph, curNode, pw);
//        handlePins(graph, curNode, pw);
//        for(BPPin flowPin : curNode.outputPins){
//            if(flowPin.getDataType() == BPPin.DataType.Flow){
//                if(flowPin.connectedTo != -1){
//                    BPNode node = graph.findPinById(flowPin.connectedTo).getNode();
//                    handleNode(graph, node, pw);
//                }
//            }
//        }
    }

    public static void nextNode(BPGraph graph, BPPin flowPin, PrintWriter pw){
        //check if pin is actually a flowpin
        if(flowPin.getDataType() == BPPin.DataType.Flow){
            if(flowPin.connectedTo != -1){
                BPPin pin = graph.findPinById(flowPin.connectedTo);
                NodeCompiler.nextNode(graph, pin.getNode(), pw);
            }
        }
    }

    public static void nextNode(BPGraph graph, BPNode nextNode, PrintWriter pw){
        nextNode.printSource(pw);

        for(BPPin pin : nextNode.outputPins){
            if(pin.getDataType() == BPPin.DataType.Flow){

            }
        }
    }

    public static void handlePins(BPGraph graph, BPNode curNode, PrintWriter pw){
        for(BPPin flowPin : curNode.outputPins) {
            if (flowPin.getDataType() == BPPin.DataType.Flow) {
                BPPin pin = graph.findPinById(flowPin.connectedTo);
                if (pin != null) {
                    pin.getNode().printSource(pw);
                }
//                break;
            }
        }
    }

    /**
     * Format the text to add indentation to source file
     */
    public static void formatFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

//        StringBuilder sb = new StringBuilder();
        lastOutput.setLength(0);
        String line;
        int tabCount = 0;
        String previousLine = "";
        while((line = br.readLine()) != null){
            char previousChar = 0;
            for(char c : previousLine.toCharArray()){
                if(c == '{' && previousChar != '"'){
                    tabCount++;
                }
            }

            //may run into a bug here with previousChar... possible solution create a previousChar variable for toCharArray
            for(char c : line.toCharArray()){
                if(c == '}' && previousChar != '"'){
                    tabCount --;
                }
                previousChar = c;
            }

            previousLine = line;

            for(int i = 0; i < tabCount; i++){
                lastOutput.append("\t");
            }
            lastOutput.append(line);
            lastOutput.append("\n");
        }

        PrintWriter pw = new PrintWriter(file);
        pw.write(lastOutput.toString());
        pw.flush();
        pw.close();
    }

    public static void writeLine(PrintWriter pw, String text){
        pw.write(text + "\n");
    }
}
