package dotj.gameobjects.components;

import dotj.*;
import dotj.debug.DebugInstance;
import dotj.debug.DebugRender;
import dotj.gameobjects.GameObject;
import org.joml.Vector3f;

public class MeshInstance extends Component {

    private Mesh mesh;

    private Vector3f color;
    private Material material;

    private int textureID = 1;
    private int specularTextureID = 0;

    private boolean shouldUpdateOutsideBounds = false;

    public MeshInstance(GameObject gameObject, Mesh mesh, Transform transform){
        super(gameObject);
        this.mesh = mesh;
        this.localTransform = transform;
        color = new Vector3f(1, 1, 1);
//        material = new Material();
        material = mesh.getMaterial();

        calculateWorldTransform();

        localTransform.setOnChangedListener(() ->{
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

    public void setTexture(Texture texture){
        setTextureID(texture.getID());
    }

    public int getSpecularTextureID() {
        return specularTextureID;
    }

    public void setSpecularTextureID(int specularTextureID) {
        this.specularTextureID = specularTextureID;
    }

    public void setSpecularTexture(Texture texture){
        setSpecularTextureID(texture.getID());
    }


}