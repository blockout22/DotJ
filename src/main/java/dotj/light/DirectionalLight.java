package dotj.light;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class DirectionalLight {

    private Vector3f direction;
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;

    public DirectionalLight(Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular) {
        this.direction = position;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getAmbient() {
        return ambient;
    }

    public void setAmbient(Vector3f ambient) {
        this.ambient = ambient;
    }

    public Vector3f getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(Vector3f diffuse) {
        this.diffuse = diffuse;
    }

    public Vector3f getSpecular() {
        return specular;
    }

    public void setSpecular(Vector3f specular) {
        this.specular = specular;
    }
}
