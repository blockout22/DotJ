package dotj.UI.ImGui.nodes;

public class NodeData<E> {

    public E value;

    public void setValue(E value){
        this.value = value;
    }

    public E getValue(){
        return value;
    }
}
