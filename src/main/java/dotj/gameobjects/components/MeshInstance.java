package dotj.gameobjects.components;

import dotj.Material;
import dotj.Mesh;
import dotj.Shader;
import dotj.Transform;
import dotj.debug.DebugInstance;
import dotj.debug.DebugRender;
import dotj.gameobjects.GameObject;
import dotj.gameobjects.components.Component;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

public class MeshInstance implements Component {

    private GameObject parent = null;
    private Mesh mesh;
//    private Shader shader;
    private Transform transform;
//    protected Vector3f positiontion;
//    private Vector3f rotation;
//    private Vector3f scale;

    private Transform worldTransform;

    private Vector3f color;
    private Material material;

    private int textureID = 1;
    private int specularTextureID = 0;

    private boolean shouldUpdateOutsideBounds = false;

    public MeshInstance(GameObject gameObject, Mesh mesh, Transform transform){
        this.parent = gameObject;
        this.mesh = mesh;
        this.transform = transform;
//        this.position = position;
//        this.rotation = rotation;
//        this.scale = scale;
        color = new Vector3f(1, 1, 1);
//        material = new Material();
        material = mesh.getMaterial();

        this.worldTransform = new Transform();
        calculateWorldTransform();

        transform.setOnChangedListener(() ->{
            calculateWorldTransform();
        });
    }

    public MeshInstance(GameObject gameObject, Mesh mesh, Vector3f position, Vector3f rotation, Vector3f scale) {
        this(gameObject, mesh, new Transform(position, rotation, scale));
    }

    public MeshInstance(GameObject gameObject, Mesh mesh){
        this(gameObject, mesh, new Vector3f(0f,0f,0f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f));

    }

    /**
     * only call once, use DebugInstances[] if Transform needs updated
     */
    public DebugInstance[] showBoundingBox(){
        return showBoundingBox(new Vector3f(255, 0, 0));
    }

    public DebugInstance[] showBoundingBox(Vector3f color){
        DebugInstance[] DebugInstances = DebugRender.addCubeRender(mesh.getBoundingBox().getMin(), mesh.getBoundingBox().getMax());

        for(DebugInstance c : DebugInstances){
            c.setColor(color);
            c.setPosition(getWorldTransform().getPosition());
            c.setRotation(getWorldTransform().getRotation());
            c.setScale(getWorldTransform().getScale());
        }

        return DebugInstances;
    }

//    public Shader getShader() {
//        return shader;
//    }
//
//    public void setShader(Shader shader) {
//        this.shader = shader;
//    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

//    public void update(){
//
//    }

    @Override
    public void execute() {
//        shader.setMaterial(material);
    }

    public void calculateWorldTransform() {
        if(parent != null) {
            worldTransform.add(parent.getTransform(), transform);
        }
//        System.out.println(worldTransform);
    }

    public void setWorldTransform(Transform transform){
        getTransform().position.x = transform.getPosition().x - getWorldTransform().getPosition().x;
        getTransform().position.y = transform.getPosition().y - getWorldTransform().getPosition().y;
        getTransform().position.z = transform.getPosition().z - getWorldTransform().getPosition().z;

        getTransform().rotation.x = transform.getRotation().x - getWorldTransform().getRotation().x;
        getTransform().rotation.y = transform.getRotation().y - getWorldTransform().getRotation().y;
        getTransform().rotation.z = transform.getRotation().z - getWorldTransform().getRotation().z;

        getTransform().setScale(transform.scale);
    }

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

    public Transform getTransform(){
        return transform;
    }

    public void setScale(float scl){
        transform.scale.x = scl;
        transform.scale.y = scl;
        transform.scale.z = scl;
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