package dotj.gameobjects;

import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.PhysicsBox;
import dotj.physics.PhysicsWorld;
import org.joml.Vector3f;

public class Sphere extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;
    private PhysicsWorld physicsWorld;

    private PhysicsBox box;

    private Mesh mesh;

    public Sphere(PerspectiveCamera camera, WorldShader shader, PhysicsWorld physicsWorld){
        this.camera = camera;
        this.shader = shader;
        this.physicsWorld = physicsWorld;
    }

    @Override
    public void init() {
        mesh = ModelLoader.load("sphere.fbx");

        MeshInstance instance = new MeshInstance(mesh, new Vector3f(0, 5, -15), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance2 = new MeshInstance(mesh, new Vector3f(-5, 5, -25), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance3 = new MeshInstance(mesh, new Vector3f(-4, 5, -5), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance4 = new MeshInstance(mesh, new Vector3f(4, 5, -25), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance5 = new MeshInstance(mesh, new Vector3f(32, 5, -11), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance6 = new MeshInstance(mesh, new Vector3f(40, 5, -1), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        MeshInstance instance7 = new MeshInstance(mesh, new Vector3f(5, 5, -15), new Vector3f(0, 0, 0), 1f) {
            public void execute() {
            }
        };

        instance.setShader(shader);
        instance2.setShader(shader);
        instance3.setShader(shader);
        instance4.setShader(shader);
        instance5.setShader(shader);
        instance6.setShader(shader);
        instance7.setShader(shader);
        addComponent(instance);
        addComponent(instance2);
        addComponent(instance3);
        addComponent(instance4);
        addComponent(instance5);
        addComponent(instance6);
        addComponent(instance7);

        BoundingBox bb = mesh.getBoundingBox();
//        box = new PhysicsBox(physicsWorld, (bb.max.x-bb.min.x) / 2, (bb.max.y-bb.min.y) / 2, (bb.max.z-bb.min.z) / 2, 1);
        box = new PhysicsBox(physicsWorld, 10, 10, 10, 1);
        addComponent(box);
    }

    @Override
    public void render() {
        mesh.enable();
        {
            MeshInstance c1 = (MeshInstance)getComponents().get(0);
            c1.setPosition(box.getPosition());

            for(Component component : getComponents()){
                if(component instanceof MeshInstance) {
                    MeshInstance instance = (MeshInstance) component;
                    mesh.render(instance.getShader(), instance.getShader().getModelMatrix(), instance, camera);
                }
            }
        }
        mesh.disable();
    }

    @Override
    public void cleanup() {

    }
}
