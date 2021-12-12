import org.joml.Vector3f;

public class Material {

    private Vector3f ambientColor;
    private Vector3f diffuseColor;
    private Vector3f specularColor;
    private boolean isTextured;
    private float reflectance;

    public Material(Vector3f ambientColor, Vector3f diffuseColor, Vector3f specularColor, boolean isTextured, float reflectance) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.isTextured = isTextured;
        this.reflectance = reflectance;
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

    public boolean isTextured() {
        return isTextured;
    }

    public void setTextured(boolean textured) {
        isTextured = textured;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }
}
