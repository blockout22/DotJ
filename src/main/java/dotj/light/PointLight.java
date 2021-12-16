package dotj.light;

import org.joml.Vector3f;

public class PointLight {

    private Vector3f position;
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    private float constant;
    private float linear;
    private float quadratic;

    public PointLight(Vector3f position, Vector3f ambient, Vector3f diffuse, Vector3f specular, float constant, float linear, float quadratic) {
        this.position = position;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.constant = constant;
        this.linear = linear;
        this.quadratic = quadratic;
    }

    public PointLight(){
        this.position = new Vector3f(1f, 1f, 1f);
        this.ambient = new Vector3f(1f, 1f, 1f);
        this.diffuse = new Vector3f(1f, 1f, 1f);
        this.specular = new Vector3f(1f, 1f, 1f);
        this.constant = 1.0f;
        this.linear = 0.09f;
        this.quadratic = 0.032f;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
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

    public float getConstant() {
        return constant;
    }

    public void setConstant(float constant) {
        this.constant = constant;
    }

    public float getLinear() {
        return linear;
    }

    public void setLinear(float linear) {
        this.linear = linear;
    }

    public float getQuadratic() {
        return quadratic;
    }

    public void setQuadratic(float quadratic) {
        this.quadratic = quadratic;
    }
}
