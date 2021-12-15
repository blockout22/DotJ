package dotj.gameobjects;

import com.jme3.bullet.objects.PhysicsBody;
import dotj.*;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import org.joml.Vector3f;

import java.io.File;

public class Floor extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;
    private PhysicsWorld physicsWorld;

    private MeshInstance floorInstance;
    private Mesh floor;
    private Texture floorTexture;

    public Floor(PerspectiveCamera camera, WorldShader shader, PhysicsWorld physicsWorld) {
        this.camera = camera;
        this.shader = shader;
        this.physicsWorld = physicsWorld;
    }

    public void init(){

        floor = ModelLoader.load("floor.obj");

        floorInstance = new MeshInstance(floor) {
            @Override
            public void execute() {

            }
        };
        floorInstance.setShader(shader);

        floorTexture = TextureLoader.loadTexture("Image.png");
        floorInstance.setScale(1f);
        floorInstance.setTextureID(floorTexture.getID());
        addComponent(floorInstance);

        BoundingBox bb = floor.getBoundingBox();
        PhysicsBox box = new PhysicsBox(physicsWorld, (bb.max.x-bb.min.x) / 2, (bb.max.y-bb.min.y) / 2, (bb.max.z-bb.min.z) / 2, PhysicsBody.massForStatic);
        addComponent(box);
    }

    @Override
    public void render() {
        floor.enable();
        floor.render(floorInstance.getShader(), floorInstance.getShader().getModelMatrix(), floorInstance, camera);
        floor.disable();
    }

    @Override
    public void cleanup() {
        floorTexture.cleanup();
        floor.cleanup();
    }
}
