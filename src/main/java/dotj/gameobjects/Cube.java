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
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Cube extends GameObject{

    public Mesh[] meshes;
    public MeshInstance[] instances;
    private Texture texture;

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
        Model[] models = ModelLoader.loadModel(new File(Utilities.getModelDir() + "cube.fbx"));

        instances = new MeshInstance[models.length];
        meshes = new Mesh[models.length];
        for (int i = 0; i < models.length; i++) {
            meshes[i] = new Mesh(models[i]);
            MeshInstance instance = new MeshInstance(this, meshes[i]);
//            instance.setShader(shader);
            instance.setTextureID(TextureLoader.loadTexture("container2.png").getID());
            instance.getTransform().setPosition(new Vector3f(-25, 9, 0));
            instances[i] = instance;
        }
        texture = TextureLoader.loadTexture("container2.png");
    }

    @Override
    public void render() {

        for (int i = 0; i < meshes.length; i++) {
            meshes[i].enable();
            {
               meshes[i].render(shader.getModelMatrix(), instances[i], camera);
            }
            meshes[i].disable();
        }

//        mesh.enable();
//        {
//            mesh.render(instance.getShader().getModelMatrix(), instance, camera);
////            instance.setWorldTransform(box.getTransform(instance.getTransform()));
////            Transform t = box.getTransform(instance.getTransform());
////            System.out.println(t);
//        }
//        mesh.disable();

    }

    @Override
    public void cleanup() {
        if(meshes == null){
            return;
        }
        for (int i = 0; i < meshes.length; i++) {
//            meshes[i].cleanup();
            Utilities.cleanup(meshes[i]);
        }
//        mesh.cleanup();
    }
}
