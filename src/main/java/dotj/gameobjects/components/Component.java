package dotj.gameobjects.components;

import dotj.Transform;
import dotj.gameobjects.GameObject;

public abstract class Component {

    private GameObject parent = null;
    public Transform localTransform;
    private Transform worldTransform;

    public Component(GameObject parent){
        this.parent = parent;
        this.worldTransform = new Transform();
        this.localTransform = new Transform();
    }

    public abstract void execute();

    public void calculateWorldTransform() {
        if(parent != null) {
            worldTransform.add(parent.getTransform(), localTransform);
        }
//        System.out.println(worldTransform);
    }

    public void setWorldTransform(Transform transform){
        getLocalTransform().position.x = transform.getPosition().x - getWorldTransform().getPosition().x;
        getLocalTransform().position.y = transform.getPosition().y - getWorldTransform().getPosition().y;
        getLocalTransform().position.z = transform.getPosition().z - getWorldTransform().getPosition().z;

        getLocalTransform().rotation.x = transform.getRotation().x - getWorldTransform().getRotation().x;
        getLocalTransform().rotation.y = transform.getRotation().y - getWorldTransform().getRotation().y;
        getLocalTransform().rotation.z = transform.getRotation().z - getWorldTransform().getRotation().z;

        getLocalTransform().setScale(transform.scale);
    }

    public Transform getLocalTransform(){
        return localTransform;
    }

    public void setScale(float scl){
        localTransform.scale.x = scl;
        localTransform.scale.y = scl;
        localTransform.scale.z = scl;
    }

    public Transform getWorldTransform(){
        return worldTransform;
    }

    public void setWorldScale(float scl){
        worldTransform.scale.x = scl;
        worldTransform.scale.y = scl;
        worldTransform.scale.z = scl;
    }
}
