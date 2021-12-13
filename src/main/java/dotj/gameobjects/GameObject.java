package dotj.gameobjects;

import dotj.gameobjects.components.Component;

import java.util.ArrayList;

public abstract class GameObject {

    private GameObject parent;
    private ArrayList<GameObject> children = new ArrayList<>();

    public ArrayList<dotj.gameobjects.components.Component> components = new ArrayList<>();

    public GameObject(){

    }

    public abstract void cleanup();
    public abstract void render();

    public GameObject getParent() {
        return parent;
    }

    public void update(){
        for(dotj.gameobjects.components.Component component : components){
            component.execute();
        }

        for(GameObject gameObject : children){
            gameObject.update();
        }
        render();
    }



    public void setParent(GameObject parent) {
        this.parent = parent;
    }

    public ArrayList<GameObject> getChildren() {
        return children;
    }

    public void addChild(GameObject gameObject) {
        children.add(gameObject);
    }

    public void removeChild(GameObject gameObject){
        children.remove(gameObject);
    }

    public ArrayList<dotj.gameobjects.components.Component> getComponents()
    {
        return components;
    }

    public void addComponent(dotj.gameobjects.components.Component component){
        components.add(component);
    }

    public void removeComponent(Component component){
        components.remove(component);
    }
}
