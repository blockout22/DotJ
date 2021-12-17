package dotj.gameobjects;

import dotj.*;
import dotj.gameobjects.components.Component;
import dotj.shaders.WorldShader;
import org.joml.Vector3f;

import java.util.Random;

public class Monkey extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;
    private Mesh mesh;

    public Monkey(PerspectiveCamera camera, WorldShader shader) {
        this.camera = camera;
        this.shader = shader;
    }

    public void init(){


        //load monkey model
        mesh = ModelLoader.load("test.obj");//new Mesh(); //OBJLoader.load("test.obj");

        Random r = new Random();
        for(int i = 0; i < 1000; i++){
            MeshInstance instance = new MeshInstance(mesh, new Vector3f(r.nextFloat() * 100, r.nextFloat() * 100, r.nextFloat() * 100), new Vector3f(r.nextFloat() * 360f, r.nextFloat() * 360f, r.nextFloat() * 360f), 1f) {
                @Override
                public void execute() {

                }
            };
            instance.setColor(new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat()));
            instance.setShader(shader);
            addComponent(instance);
        }
    }

    @Override
    public void render() {
        mesh.enable();
        {
            for(Component component : getComponents()){
                MeshInstance instance = (MeshInstance) component;
                shader.setColor(instance.getColor());
                mesh.render(instance.getShader(), instance.getShader().getModelMatrix(), instance, camera);
            }
        }
        mesh.disable();
    }

    @Override
    public void cleanup() {

    }
}
