package dotj;

import dotj.gameobjects.Floor;
import dotj.gameobjects.GameObject;
import dotj.gameobjects.Monkey;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;

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


    public JApp(){
        init();
        update();
        close();
    }

    @Override
    public void init() {

        physicsWorld = new PhysicsWorld();

        window = new GLFWWindow(800, 600, "GLFW Window");

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
    }

    @Override
    public void update() {
        while(!window.shouldClose()){

            Time.setDelta();
            if (Time.getTime() - last_time >= 1000) {
                last_time = Time.getTime();
                window.setTitle("[FPS: " + fps + "]");
                System.out.println(fps);
                fps = 0;
            }
            fps++;

            GL11.glClearColor(.2f, 0.8f, .2f, 1f);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);


            shader.bind();
            {
                shader.loadSkyColour(.5f, .5f, .5f);
                shader.loadLight(light);
                shader.loadViewMatrix(camera);

                for(GameObject root : rootObjects){
                    root.update();
                }
            }
            shader.unbind();



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
        defaultTexture.cleanup();
        shader.cleanup();
        window.close();
    }
}
