package dotj.gameobjects;

import dotj.Transform;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.MeshInstance;
import dotj.interfaces.OnChangedListener;

import java.util.ArrayList;

public abstract class GameObject {

//    private GameObject parent;
//    private ArrayList<GameObject> children = new ArrayList<>();

    public ArrayList<Component> components = new ArrayList<>();

    private Transform transform;

    public GameObject(){
        transform = new Transform();

        transform.setOnChangedListener(new OnChangedListener() {
        @Override
        public void onChange() {
            for(Component component : components){
//                component.
                //TODO add transform to components then update there transforms according to GameObject Transform

                component.calculateWorldTransform();
            }
//            System.out.println(getTransform().getPosition().y);
        }
    });}

    /**
     * Will be called by the Level
     */
    public abstract void init();
    public abstract void render();
    public abstract void cleanup();

//    public GameObject getParent() {
//        return parent;
//    }

    public void update(){
        for(Component component : components){
            component.execute();
        }

//        for(GameObject gameObject : children){
//            gameObject.update();
//        }
        render();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    //    public void setParent(GameObject parent) {
//        this.parent = parent;
//    }
//
//    public ArrayList<GameObject> getChildren() {
//        return children;
//    }
//
//    public void addChild(GameObject gameObject) {
//        children.add(gameObject);
//    }
//
//    public void removeChild(GameObject gameObject){
//        children.remove(gameObject);
//    }

    public ArrayList<Component> getComponents()
    {
        return components;
    }

    public void addComponent(Component component){
        components.add(component);
    }

    public void removeComponent(Component component){
        components.remove(component);
    }
}
