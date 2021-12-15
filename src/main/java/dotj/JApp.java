package dotj;

import dotj.UI.Nano.vg.NanoVGRenderer;
import dotj.gameobjects.Floor;
import dotj.gameobjects.GameObject;
import dotj.gameobjects.Monkey;
import dotj.gameobjects.Sphere;
import dotj.input.GLFWKey;
import dotj.input.Input;
import dotj.interfaces.OnFinishedListener;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class JApp extends App {

    private GLFWWindow window;

    private long last_time = 0;
    private int fps = 0;

    private PerspectiveCamera camera;
    private WorldShader shader;
    //private ArrayList<MeshInstance> instances = new ArrayList<>();

    //create a texture that will have the very first ID and any mesh without a texture assigned will use this one
    private Texture defaultTexture;

    private float SPEED = 0.01f;

    private Light light;

    private Level level;
    private PhysicsWorld physicsWorld;


    private NanoVGRenderer vgRenderer;

    public JApp(){
        init();
        update();
        close();
    }

    @Override
    public void init() {

        Vector2f value = new Vector2f(1920 / 2, 800);
        Vector2f inA = new Vector2f(0, 0);
        Vector2f inB = new Vector2f(1920, 1080);
        Vector2f outA = new Vector2f(0, 0);
        Vector2f outB = new Vector2f(800, 600);
        Vector2f returnFloat = Utilities.remapVector2f(value, inA, inB, outA, outB);
        System.out.println(returnFloat.x + " : " + returnFloat.y);

        float rValue = 1920 / 2;
        float rinA = 0;
        float rinB = 1920;
        float routA = 0;
        float routB = 800;
        float res = Utilities.remapFloat(rValue, rinA, rinB, routA, routB);
        System.out.println(res);


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

        level = new Level();

        Floor floor = new Floor(camera, shader);
        level.addGameObject(floor);
        Monkey monkey = new Monkey(camera, shader);
        level.addGameObject(monkey);
        Sphere sphere = new Sphere(camera, shader);
        level.addGameObject(sphere);

        this.vgRenderer = new NanoVGRenderer(window);


        level.load(new OnFinishedListener() {
            @Override
            public void finished() {
                System.out.println("Finished Loading Level");
            }
        });
    }

    @Override
    public void update() {
        while(!window.shouldClose()){

            Time.setDelta();
            if (Time.getTime() - last_time >= 1000) {
                last_time = Time.getTime();
                window.setTitle("[FPS: " + fps + "]");
                //System.out.println(fps);
                vgRenderer.setFPS(fps);
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

                level.update();
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
        level.unload();
        vgRenderer.cleanup();
        defaultTexture.cleanup();
        shader.cleanup();
        window.close();
    }
}
