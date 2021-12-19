package dotj;

import org.joml.Vector3f;

public class Transform {

    public Vector3f position;
    public Vector3f rotation;
    public Vector3f scale;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Transform(Vector3f position, Vector3f rotation){
        this(position, rotation, new Vector3f(1f, 1f, 1f));
    }

    public Transform(Vector3f position){
        this(position, new Vector3f(0f, 0f, 0f));
    }

    public Transform(){
        this(new Vector3f(0f, 0f, 0f));
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
