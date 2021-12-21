package dotj.gameobjects;

import com.jme3.bullet.objects.PhysicsBody;
import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.MeshInstance;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

public class Cube extends GameObject{

    public Mesh mesh;
    public MeshInstance instance;
    private Texture texture;

    private PerspectiveCamera camera;
    private WorldShader shader;
    private PhysicsWorld physicsWorld;
    private PhysicsBox box;

    public Cube(PerspectiveCamera camera, WorldShader shader, PhysicsWorld physicsWorld){
        this.camera =camera;
        this.shader = shader;
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void init() {
        mesh = ModelLoader.load("cube.fbx");
        instance = new MeshInstance(this, mesh);

        instance.setShader(shader);
        texture = TextureLoader.loadTexture("container2.png");


        instance.setTextureID(texture.getID());
        addComponent(instance);

        BoundingBox bb = mesh.getBoundingBox();
        box = new PhysicsBox(physicsWorld, (bb.max.x-bb.min.x) / 2, (bb.max.y-bb.min.y) / 2, (bb.max.z-bb.min.z) / 2, PhysicsBody.massForStatic);
        addComponent(box);



        getTransform().setPosition(new Vector3f(1.2f, 5, -5.6f));

        instance.getTransform().setPosition(new Vector3f(0, 100, 0));

        box.setPosition(instance.getWorldTransform().getPosition().x, instance.getWorldTransform().getPosition().y, instance.getWorldTransform().getPosition().z);

    }

    @Override
    public void render() {
        mesh.enable();
        {
            mesh.render(instance.getShader().getModelMatrix(), instance, camera);


//            instance.setWorldTransform(box.getTransform(instance.getTransform()));
//            Transform t = box.getTransform(instance.getTransform());
//            System.out.println(t);
        }
        mesh.disable();

    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }
}
