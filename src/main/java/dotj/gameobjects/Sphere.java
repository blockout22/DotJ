package dotj.gameobjects;

import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.MeshInstance;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import dotj.shaders.WorldShader;
import org.joml.Random;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;

public class Sphere extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;
    private PhysicsWorld physicsWorld;

    private PhysicsBox box;

    public Mesh mesh;
    public MeshInstance instance;

    private Texture sphereTexture, specularTexture;

    public Sphere(PerspectiveCamera camera, WorldShader shader, PhysicsWorld physicsWorld){
        this.camera = camera;
        this.shader = shader;
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void init() {
        Model model = ModelLoader.load("cube.fbx");
        mesh = new Mesh(model);
//        mesh = ModelLoader.load("cube.obj");

        Random r = new Random();
        instance = new MeshInstance(this, mesh, new Vector3f(0, 5, -15), new Vector3f(r.nextInt(360), r.nextInt(360), r.nextInt(360)), new Vector3f(1.13f, 1.13f, 1.13f));

        MeshInstance instance2 = new MeshInstance(this, mesh, new Vector3f(-2, 0, 0), new Vector3f(-0, 0, 0), new Vector3f(2f, 2f, 2f));

        MeshInstance lightInstance = new MeshInstance(this, mesh, new Vector3f(4, 15, 45), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), new Vector3f(.2f, .2f, .2f));

        MeshInstance instance4 = new MeshInstance(this, mesh, new Vector3f(4, 10, -25), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), new Vector3f(3f, 3f, 3f));

        MeshInstance instance5 = new MeshInstance(this, mesh, new Vector3f(15, 5, -11), new Vector3f(r.nextInt(360),r.nextInt(360),r.nextInt(360)), new Vector3f(1f, 1f, 1f));

        MeshInstance instance6 = new MeshInstance(this, mesh, new Vector3f(5, 5, -1), new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));

        MeshInstance instance7 = new MeshInstance(this, mesh, new Vector3f(15, 10, -15), new Vector3f(0, 0, 0), new Vector3f(2f, 2f, 2f));

//        instance.setShader(shader);
//        instance2.setShader(shader);
//        lightInstance.setShader(shader);
//        instance4.setShader(shader);
//        instance5.setShader(shader);
//        instance6.setShader(shader);
//        instance7.setShader(shader);
        addComponent(instance);
        addComponent(instance2);
        addComponent(lightInstance);
        addComponent(instance4);
        addComponent(instance5);
        addComponent(instance6);
        addComponent(instance7);

        sphereTexture = TextureLoader.loadTexture("container2.png");
        instance.setTexture(sphereTexture);
//        instance2.setTexture(sphereTexture);
        lightInstance.setTexture(sphereTexture);
        instance4.setTexture(sphereTexture);
        instance5.setTexture(sphereTexture);
        instance6.setTexture(sphereTexture);
        instance7.setTexture(sphereTexture);

        specularTexture = TextureLoader.loadTexture("container2_specular.png");
        instance.setSpecularTexture(specularTexture);
        instance2.setSpecularTexture(specularTexture);
        lightInstance.setSpecularTexture(specularTexture);
        instance4.setSpecularTexture(specularTexture);
        instance5.setSpecularTexture(specularTexture);
        instance6.setSpecularTexture(specularTexture);
        instance7.setSpecularTexture(specularTexture);

//        instance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance2.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        lightInstance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance4.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance5.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance6.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//        instance7.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));

        getTransform().setPosition(new Vector3f(0, 2, 0));
        getTransform().apply();

//        instance.showBoundingBox();
//        instance2.showBoundingBox(new Vector3f(0, 0, 255));
//        lightInstance.showBoundingBox(new Vector3f(0, 255, 0));
//        instance4.showBoundingBox();
//        instance5.showBoundingBox();
//        instance6.showBoundingBox();
//        instance7.showBoundingBox();

//        lightInstance.setWorldTransform(new Transform(new Vector3f(), new Vector3f(), new Vector3f(1f, 1f, 1f)));

//        BoundingBox bb = mesh.getBoundingBox();
//        box = new PhysicsBox(physicsWorld, (bb.max.x-bb.min.x) / 2, (bb.max.y-bb.min.y) / 2, (bb.max.z-bb.min.z) / 2, 1);
//        box = new PhysicsBox(physicsWorld, 10, 10, 10, 1);
//        addComponent(box);
    }

    private HashMap<Float, MeshInstance> sorted = new HashMap<>();

    @Override
    public void render() {
        mesh.enable();
        {
            sorted.clear();
            for (Component component : getComponents()) {
                if (component instanceof MeshInstance) {
                    MeshInstance instance = (MeshInstance) component;
                    Vector3f vec = new Vector3f();
                    float distance = camera.getPosition().distance(instance.getWorldTransform().getPosition());
                    sorted.put(distance, instance);

                    mesh.render(shader.getModelMatrix(), instance, camera);
                    shader.setColor(instance.getColor());
                    shader.setMaterial(instance.getMaterial());
                }
            }

            /**
             * render instance in a sorted fashion
             */
//            for (MeshInstance inst : sorted.values()) {
//                shader.setColor(inst.getColor());
//                shader.setMaterial(inst.getMaterial());
//                mesh.render(inst.getShader().getModelMatrix(), inst, camera);
//            }
            mesh.disable();
        }

    }

    @Override
    public void cleanup() {
//        specularTexture.cleanup();
//        sphereTexture.cleanup();
//        mesh.cleanup();
        Utilities.cleanup(mesh);
    }

}
