package dotj.gameobjects;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.objects.PhysicsBody;
import dotj.*;
import dotj.debug.DebugInstance;
import dotj.debug.DebugRender;
import dotj.gameobjects.components.MeshInstance;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

public class Floor extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;

    private MeshInstance floorInstance;
    private Mesh floor;
    private Texture floorTexture;

    private PhysicsWorld physicsWorld;
    private PhysicsBox physicsBox;

    public Floor(PhysicsWorld physicsWorld, PerspectiveCamera camera, WorldShader shader) {
        this.physicsWorld = physicsWorld;
        this.camera = camera;
        this.shader = shader;
    }

    public void init(){

        floor = ModelLoader.load("floor.fbx");

        floorInstance = new MeshInstance(this, floor);
        floorInstance.setShader(shader);

        floorTexture = TextureLoader.loadTexture("Image.png");
        floorInstance.setScale(1f);
//        floorInstance.setTextureID(floorTexture.getID());
        floorInstance.getTransform().setRotation(new Vector3f(-90, 0, 90));
        addComponent(floorInstance);

        floorInstance.showBoundingBox();



        Vector3f halfExtents = new Vector3f(floor.getBoundingBox().max.x - floor.getBoundingBox().min.x, floor.getBoundingBox().max.y - floor.getBoundingBox().min.y, floor.getBoundingBox().max.z - floor.getBoundingBox().min.z);
        physicsBox = new PhysicsBox(physicsWorld, halfExtents.x, halfExtents.y, halfExtents.z, 0);

//        BoundingBox physicsBounds = physicsBox.getBoundingBox();
//        com.jme3.math.Vector3f min = new com.jme3.math.Vector3f();
//        com.jme3.math.Vector3f max = new com.jme3.math.Vector3f();
//        min = physicsBounds.getMin(min);
//        max = physicsBounds.getMax(max);
//
//        DebugInstance[] boundsInstances = DebugRender.addCubeRender(new Vector3f(min.x, min.y, min.z), new Vector3f(max.x, max.y, max.z));
//
//        System.out.println(physicsBox.getRotation());
//        for(DebugInstance bi : boundsInstances){
//            bi.setColor(new Vector3f(255, 255, 255));
//            bi.setPosition(physicsBox.getPosition());
//            bi.setRotation(physicsBox.getRotation());
////            bi.setScale(physicsBox.getScale());
//        }

        floorInstance.getWorldTransform().setPosition(new Vector3f(0, 1, 0));
    }

    @Override
    public void render() {
        floor.enable();
        shader.setColor(floorInstance.getColor());
        shader.setMaterial(floorInstance.getMaterial());
        floor.render(floorInstance.getShader().getModelMatrix(), floorInstance, camera);
        floor.disable();
    }

    @Override
    public void cleanup() {
//        floorTexture.cleanup();
        floor.cleanup();
    }
}
