package dotj.gameobjects;

import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import org.joml.Random;
import org.joml.Vector3f;

public class Sphere extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;
    private PhysicsWorld physicsWorld;

    private PhysicsBox box;

    public Mesh mesh;

    private Texture sphereTexture, specularTexture;

    public Sphere(PerspectiveCamera camera, WorldShader shader, PhysicsWorld physicsWorld){
        this.camera = camera;
        this.shader = shader;
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void init() {
        mesh = ModelLoader.load("cube.obj");
//        mesh = ModelLoader.load("cube.obj");

        Random r = new Random();
        MeshInstance instance = new MeshInstance(mesh, new Vector3f(0, 5, -15), new Vector3f(r.nextInt(360), r.nextInt(360), r.nextInt(360)), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance2 = new MeshInstance(mesh, new Vector3f(-5, 5, -25), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), 1f) {
            public void execute() {
            }
        };

        MeshInstance lightInstance = new MeshInstance(mesh, new Vector3f(4, 15, 45), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), .2f) {
            public void execute() {
            }
        };

        MeshInstance instance4 = new MeshInstance(mesh, new Vector3f(4, 5, -25), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), 3f) {
            public void execute() {
            }
        };

        MeshInstance instance5 = new MeshInstance(mesh, new Vector3f(15, 5, -11), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance6 = new MeshInstance(mesh, new Vector3f(5, 5, -1), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance7 = new MeshInstance(mesh, new Vector3f(5, 5, -15), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        instance.setShader(shader);
        instance2.setShader(shader);
        lightInstance.setShader(shader);
        instance4.setShader(shader);
        instance5.setShader(shader);
        instance6.setShader(shader);
        instance7.setShader(shader);
        addComponent(instance);
        addComponent(instance2);
        addComponent(lightInstance);
        addComponent(instance4);
        addComponent(instance5);
        addComponent(instance6);
        addComponent(instance7);

        sphereTexture = TextureLoader.loadTexture("container2.png");
        instance.setTextureID(sphereTexture.getID());
        instance2.setTextureID(sphereTexture.getID());
        lightInstance.setTextureID(sphereTexture.getID());
        instance4.setTextureID(sphereTexture.getID());
        instance5.setTextureID(sphereTexture.getID());
        instance6.setTextureID(sphereTexture.getID());
        instance7.setTextureID(sphereTexture.getID());

        specularTexture = TextureLoader.loadTexture("container2_specular.png");
        instance.setSpecularTextureID(specularTexture.getID());
        instance2.setSpecularTextureID(specularTexture.getID());
        lightInstance.setSpecularTextureID(specularTexture.getID());
        instance4.setSpecularTextureID(specularTexture.getID());
        instance5.setSpecularTextureID(specularTexture.getID());
        instance6.setSpecularTextureID(specularTexture.getID());
        instance7.setSpecularTextureID(specularTexture.getID());

        instance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
        instance2.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
        lightInstance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance4.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
        instance5.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance6.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance7.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));

//        BoundingBox bb = mesh.getBoundingBox();
//        box = new PhysicsBox(physicsWorld, (bb.max.x-bb.min.x) / 2, (bb.max.y-bb.min.y) / 2, (bb.max.z-bb.min.z) / 2, 1);
//        box = new PhysicsBox(physicsWorld, 10, 10, 10, 1);
//        addComponent(box);
    }

    @Override
    public void render() {
        mesh.enable();
        {
            for(Component component : getComponents()){
                if(component instanceof MeshInstance) {
                    MeshInstance instance = (MeshInstance) component;
                    shader.setColor(instance.getColor());
                    shader.setMaterial(instance.getMaterial());
                    mesh.render(instance.getShader(), instance.getShader().getModelMatrix(), instance, camera);
                }
            }
        }
        mesh.disable();
    }

    @Override
    public void cleanup() {
        specularTexture.cleanup();
        sphereTexture.cleanup();
        mesh.cleanup();
    }
}
