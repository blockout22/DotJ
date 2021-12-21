package dotj;

import dotj.interfaces.OnChangedListener;
import org.joml.Vector3f;

public class Transform {

    private OnChangedListener onChangedListener;
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

    public void setOnChangedListener(OnChangedListener onChangedListener){
        this.onChangedListener = onChangedListener;
    }

    public void add(Transform t1, Transform t2){
        float posx = t1.getPosition().x + t2.getPosition().x;
        float posy = t1.getPosition().y + t2.getPosition().y;
        float posz = t1.getPosition().z + t2.getPosition().z;

        float rotx = t1.getRotation().x + t2.getRotation().x;
        float roty = t1.getRotation().y + t2.getRotation().y;
        float rotz = t1.getRotation().z + t2.getRotation().z;

        float sclx = t1.getScale().x * t2.getScale().x;
        float scly = t1.getScale().y * t2.getScale().y;
        float sclz = t1.getScale().z * t2.getScale().z;

        setPosition(posx, posy, posz);
        setRotation(rotx, roty, rotz);
        setScale(sclx, scly, sclz);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;

        if(onChangedListener != null){
            onChangedListener.onChange();
        }
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;

        if(onChangedListener != null){
            onChangedListener.onChange();
        }
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;

        if(onChangedListener != null){
            onChangedListener.onChange();
        }
    }

    public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;

        if(onChangedListener != null){
            onChangedListener.onChange();
        }
    }

    public void setRotation(float x, float y, float z){
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;

        if(onChangedListener != null){
            onChangedListener.onChange();
        }
    }

    public void setScale(float x, float y, float z){
        scale.x = x;
        scale.y = y;
        scale.z = z;

        if(onChangedListener != null){
            onChangedListener.onChange();
        }
    }

    public String toString(){
        String pos = "[" + getPosition().x + ", "  + getPosition().y + ", " + getPosition().z + "] ";
        String rot = "[" + getRotation().x + ", "  + getRotation().y + ", " + getRotation().z + "] ";
        String scl = "[" + getScale().x + ", "  + getScale().y + ", " + getScale().z + "] ";
        return (pos + rot + scl);
    }
}
