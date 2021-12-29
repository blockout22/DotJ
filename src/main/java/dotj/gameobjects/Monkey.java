package dotj.gameobjects;

import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.MeshInstance;
import dotj.shaders.WorldInstancedShader;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class Monkey extends GameObject{

    private PerspectiveCamera camera;
    private Shader shader;
    private MeshInstanced mesh;
    //100,000 non instance (13 FPS)
    // 100,000 instanced (40 FPS)
//    private int total = 10000;
    private int total;

    public Monkey(PerspectiveCamera camera, Shader shader) {
        this.camera = camera;
        this.shader = shader;
    }

    public void init(){
        //load monkey model
        Model model = ModelLoader.load("monkey.fbx");//new Mesh(); //OBJLoader.load("test.obj");
        mesh = new MeshInstanced(model);

        Random r = new Random();
        System.out.println("Init Monkey");

        int totalX = 5;
        int totalY = 5;
        int totalZ = 5;
//        Vector3f[] list = new Vector3f[totalX * totalY * totalZ];
        ArrayList<Vector3f> list = new ArrayList<>();
        float spacing = 25;
        for(int x = 0; x < totalX; x++){
            for(int y = 0; y < totalY; y++){
                for(int z = 0; z < totalZ; z++){
                    Vector3f rotation = new Vector3f(r.nextFloat() * 360f, r.nextFloat() * 360f, r.nextFloat() * 360f);
                    float distance = 1000;
                    MeshInstance instance = new MeshInstance(this,mesh, new Vector3f(x * spacing, y * spacing, z * spacing), rotation, new Vector3f(1f, 1f, 1f));
                    instance.setScale(.2f);
                    instance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
//            instance.setShader(shader);
                    addComponent(instance);
//                    System.out.println(total + " : " + list.length);
//                    list[total++] = instance.getWorldTransform().getPosition();
                    list.add(instance.getWorldTransform().getPosition());
                    total++;
                }
            }
        }

        System.out.println("Total: " + total);
        for(int i = 0; i < total; i++){

        }

        Vector3f[] posList = Utilities.toVectorArray(list);
        mesh.add(posList);
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
