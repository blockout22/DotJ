package dotj;

import dotj.UI.Nano.vg.NanoVGRenderer;
import dotj.UI.UIRenderer;
import dotj.gameobjects.Floor;
import dotj.gameobjects.GameObject;
import dotj.gameobjects.Monkey;
import example.GLFWDemo;
import org.joml.Vector3f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL11.*;

public class JApp extends App {

    private GLFWWindow window;

    private long last_time = 0;
    private int fps = 0;

    private PerspectiveCamera camera;
    private WorldShader shader;
    private ArrayList<MeshInstance> instances = new ArrayList<>();

    //create a texture that will have the very first ID and any mesh without a texture assigned will use this one
    private Texture defaultTexture;

    private float SPEED = 0.01f;

    private Light light;

    private PhysicsWorld physicsWorld;

    private ArrayList<GameObject> rootObjects = new ArrayList<>();

    private NanoVGRenderer vgRenderer;

    public JApp(){
        init();
        update();
        close();
    }

    @Override
    public void init() {

        physicsWorld = new PhysicsWorld();

        window = new GLFWWindow(800, 600, "GLFW Window");
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        camera = new PerspectiveCamera(window, 70, 0.1f, 100000f);
        camera.setPosition(new Vector3f(0, 10f, 10f));
        shader = new WorldShader("vertexShader.glsl", "fragmentShader.glsl");

        shader.bind();
        shader.loadMatrix(shader.getProjectionMatrix(), camera.getProjectionMatrix());

        /** a way to load difference types of models using Assimp
        File file = new File("E:/LWJGL/DotJ/src/main/resources/test.obj");
        mesh = ModelLoader.load(file);//new Mesh(); //OBJLoader.load("test.obj");
         */

        defaultTexture = TextureLoader.loadTexture("white.png");


//        Random r = new Random();
//        for(int i = 0; i < 1000; i++) {
//
//            MeshInstance instance = new MeshInstance(mesh, new Vector3f(r.nextFloat() * 100, r.nextFloat() * 100, r.nextFloat() * 100), new Vector3f(r.nextFloat() * 360f, r.nextFloat() * 360f, r.nextFloat() * 360f), 1f);
//            instances.add(instance);
//        }

        light = new Light(new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));

        Floor floor = new Floor(camera, shader);
        rootObjects.add(floor);

        Monkey monkey = new Monkey(camera, shader);
        rootObjects.add(monkey);

        this.vgRenderer = new NanoVGRenderer(window);


    }

    @Override
    public void update() {
        while(!window.shouldClose()){

            Time.setDelta();
            if (Time.getTime() - last_time >= 1000) {
                last_time = Time.getTime();
                window.setTitle("[FPS: " + fps + "]");
                //System.out.println(fps);
                fps = 0;
            }
            fps++;

            glClearColor(.2f, 0.8f, .2f, 1f);
            glEnable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);


            shader.bind();
            {
                shader.loadSkyColour(1f, 1f, 1f);
                shader.loadLight(light);
                shader.loadViewMatrix(camera);

                for(GameObject root : rootObjects){
                    root.update();
                }
            }
            shader.unbind();

            vgRenderer.update();


            //camera movement
            if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_W))
            {
                camera.getPosition().x += Math.sin(camera.getYaw() * Math.PI / 180) * SPEED * Time.getDelta();
                camera.getPosition().z += -Math.cos(camera.getYaw() * Math.PI / 180) * SPEED * Time.getDelta();
            }else if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_S))
            {
                camera.getPosition().x -= Math.sin(camera.getYaw() * Math.PI / 180) * SPEED * Time.getDelta();
                camera.getPosition().z -= -Math.cos(camera.getYaw() * Math.PI / 180) * SPEED * Time.getDelta();
            }

            if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_A))
            {
                camera.getPosition().x += Math.sin((camera.getYaw() - 90) * Math.PI / 180) * SPEED * Time.getDelta();
                camera.getPosition().z += -Math.cos((camera.getYaw() - 90) * Math.PI / 180) * SPEED * Time.getDelta();
            }else if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_D))
            {
                camera.getPosition().x += Math.sin((camera.getYaw() + 90) * Math.PI / 180) * SPEED * Time.getDelta();
                camera.getPosition().z += -Math.cos((camera.getYaw() + 90) * Math.PI / 180) * SPEED * Time.getDelta();
            }

            camera.update();

            window.update();
        }
    }

    @Override
    public void close() {
        vgRenderer.cleanup();
        defaultTexture.cleanup();
        shader.cleanup();
        window.close();
    }
}
