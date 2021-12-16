package dotj;

import org.joml.Vector3f;

public class Material {

    private Vector3f ambientColor = new Vector3f(1, 1, 1);
    private Vector3f diffuseColor = new Vector3f(1, 1, 1);;
    private Vector3f specularColor = new Vector3f(1, 1, 1);;
    private float shininess = 32f;

    public Material(Vector3f ambientColor, Vector3f diffuseColor, Vector3f specularColor, float shininess) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.shininess = shininess;
    }

    public Material(){

    }

    public Vector3f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector3f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector3f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector3f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector3f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector3f specularColor) {
        this.specularColor = specularColor;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }
}
