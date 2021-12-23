package dotj.gameobjects;

import com.jme3.bullet.objects.PhysicsBody;
import dotj.*;
import dotj.debug.DebugInstance;
import dotj.debug.DebugRender;
import dotj.gameobjects.components.MeshInstance;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Cube extends GameObject{

    public Mesh mesh;
    public MeshInstance instance;
    private Texture texture;
//    private Texture cubemapTexture;

    private PerspectiveCamera camera;
    private WorldShader shader;
    private PhysicsWorld physicsWorld;
    private PhysicsBox box;

    public Cube(PerspectiveCamera camera, WorldShader shader, PhysicsWorld physicsWorld){
        this.camera = camera;
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

//        mesh.setAABB(new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2));

        BoundingBox bb = mesh.getBoundingBox();
        box = new PhysicsBox(physicsWorld, (bb.max.x-bb.min.x) / 2, (bb.max.y-bb.min.y) / 2, (bb.max.z-bb.min.z) / 2, PhysicsBody.massForStatic);
        addComponent(box);

        Vector3f min = mesh.getBoundingBox().getMin();
        Vector3f max = mesh.getBoundingBox().getMax();

        getTransform().setPosition(new Vector3f(1.2f, 5, -5.6f));

        instance.getTransform().setPosition(new Vector3f(0, 5, 0));

        box.setPosition(instance.getWorldTransform().getPosition().x, instance.getWorldTransform().getPosition().y, instance.getWorldTransform().getPosition().z);

//        instance.showBoundingBox();

//        BufferedImage image = null;
//        try {
//             image = Utilities.loadImage("skybox.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        cubemapTexture = new Texture(256, 256);
//        ByteBuffer[] buffers = Utilities.loadToCubeMap(image);
//        cubemapTexture.genTextureID(buffers[0]);
//        instance.setTextureID(cubemapTexture.getID());
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
