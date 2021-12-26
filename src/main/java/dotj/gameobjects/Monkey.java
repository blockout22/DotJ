package dotj.gameobjects;

import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.MeshInstance;
import dotj.shaders.WorldInstancedShader;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

import java.util.Random;

public class Monkey extends GameObject{

    private PerspectiveCamera camera;
    private Shader shader;
    private MeshInstanced mesh;
    //100,000 non instance (13 FPS)
    // 100,000 instanced (40 FPS)
    private int total = 10000;

    public Monkey(PerspectiveCamera camera, Shader shader) {
        this.camera = camera;
        this.shader = shader;
    }

    public void init(){
        //load monkey model
        Model model = ModelLoader.load("monkey.fbx");//new Mesh(); //OBJLoader.load("test.obj");
        mesh = new MeshInstanced(model);

        Random r = new Random();

        Vector3f[] list = new Vector3f[total];
        for(int i = 0; i < total; i++){
            Vector3f rotation = new Vector3f(r.nextFloat() * 360f, r.nextFloat() * 360f, r.nextFloat() * 360f);
            float distance = 1000;
            MeshInstance instance = new MeshInstance(this,mesh, new Vector3f(r.nextFloat() * distance, r.nextFloat() * distance, r.nextFloat() * distance), rotation, new Vector3f(1f, 1f, 1f));
            instance.setScale(.2f);
            instance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//            instance.setShader(shader);
            addComponent(instance);
            list[i] = instance.getWorldTransform().getPosition();
        }

        mesh.add(list);
    }

    @Override
    public void render() {
        mesh.enable();
        {
//            for (int i = 0; i < components.size(); i++) {
//                MeshInstance instance = (MeshInstance) components.get(i);
//                shader.bind();
//                shader.loadVector3f("offseta[" + i + "]", instance.getWorldTransform().getPosition());
//            }

            mesh.renderInstances(total);
//            for(Component component : getComponents()){
//                MeshInstance instance = (MeshInstance) component;
////                shader.setColor(instance.getColor());
//                mesh.render(shader.getUnform("modelMatrix"), instance, camera);
//            }
        }
        mesh.disable();
    }

    @Override
    public void cleanup() {
        Utilities.cleanup(mesh);
    }
}
