package dotj;

public class Material {

//    private Vector3f diffuseColor = new Vector3f(1, 1, 1);
    private int diffuse = 0;
    private int specular = 1;
    private float shininess = 32f;

    public Material(int diffuseColor, int specularColor, float shininess) {
        this.diffuse = diffuseColor;
        this.specular = specularColor;
        this.shininess = shininess;
    }

    public Material(){

    }

    public int getDiffuse() {
        return diffuse;
    }

    public void setDiffuse(int diffuse) {
        this.diffuse = diffuse;
    }

    public int getSpecular() {
        return specular;
    }

    public void setSpecular(int specular) {
        this.specular = specular;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }
}
