package dotj;

import dotj.gameobjects.components.Component;
import org.joml.Vector3f;

public abstract class MeshInstance implements Component {

    private Mesh mesh;
    private WorldShader shader;
    protected Vector3f position;
    private Vector3f rotation;

    private Vector3f color;
    private Material material;

    private float scale;
    private int textureID = 1;
    private int specularTextureID = 0;

    private boolean shouldUpdateOutsideBounds = false;

    public MeshInstance(Mesh mesh, Vector3f position, Vector3f rotation, float scale) {
        this.mesh = mesh;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        color = new Vector3f(1, 1, 1);
        material = new Material();
    }

    public MeshInstance(Mesh mesh){
        this(mesh, new Vector3f(0f,0f,0f), new Vector3f(0f, 0f, 0f), 1f);

    }

    public WorldShader getShader() {
        return shader;
    }

    public void setShader(WorldShader shader) {
        this.shader = shader;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

//    public void update(){
//
//    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
     * updates the Meshobject even if its outside the render distance of camera;
     */
    public void updateOutsideBounds(boolean update)
    {
        shouldUpdateOutsideBounds = update;
    }

    public boolean getUpdateOutsideBounds()
    {
        return shouldUpdateOutsideBounds;
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    public int getTextureID() {
        return textureID;
    }

    public void setTextureID(int id){
        this.textureID = id;
//        this.material.setDiffuse(id);
//        System.out.println(material.getDiffuse());
    }

    public int getSpecularTextureID() {
        return specularTextureID;
    }

    public void setSpecularTextureID(int specularTextureID) {
        this.specularTextureID = specularTextureID;
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}