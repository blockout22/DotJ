import org.joml.Vector3f;

public class MeshInstance {

    private Mesh mesh;
    protected Vector3f position;
    private Vector3f rotation;
    private float scale;
    private int textureID = 1;

    private boolean shouldUpdateOutsideBounds = false;

    public MeshInstance(Mesh mesh, Vector3f position, Vector3f rotation, float scale) {
        this.mesh = mesh;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public MeshInstance(Mesh mesh){
        this(mesh, new Vector3f(0f,0f,0f), new Vector3f(0f, 0f, 0f), 1f);

    }

    public void update(){}

    /**
     * updates the Meshobject even if its outside the render distance of camera;
     */
    public void updateOutsideBounds(boolean update)
    {
        shouldUpdateOutsideBounds = update;
    }

    public boolean getUpdateOutsideBounts()
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

    public void setTextureID(int id){this.textureID = id;}

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