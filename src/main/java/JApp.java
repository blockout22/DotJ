import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class JApp extends App {

    private GLFWWindow window;

    private long last_time = 0;
    private int fps = 0;

    private PerspectiveCamera camera;
    private WorldShader shader;
    private Mesh mesh, floor;
    private MeshInstance floorInstance;

    private ArrayList<MeshInstance> instances = new ArrayList<>();

    //create a texture that will have the very first ID and any mesh without a texture assigned will use this one
    private Texture defaultTexture;
    private Texture floorTexture;


    private float SPEED = 0.01f;


    public JApp(){
        init();
        update();
        close();
    }

    @Override
    public void init() {
        window = new GLFWWindow(800, 600, "GLFW Window");

        camera = new PerspectiveCamera(window, 70, 0.1f, 100000f);
        camera.setPosition(new Vector3f(0, 10f, 10f));
        shader = new WorldShader("vertexShader.glsl", "fragmentShader.glsl");

        shader.bind();
        shader.loadMatrix(shader.getProjectionMatrix(), camera.getProjectionMatrix());

        floor = OBJLoader.load("floor.obj");

        File file = new File("E:/LWJGL/DotJ/src/main/resources/test.obj");

        mesh = ModelLoader.load(file);//new Mesh(); //OBJLoader.load("test.obj");

        defaultTexture = TextureLoader.loadTexture("white.png");
        floorTexture = TextureLoader.loadTexture("Image.png");

        floorInstance = new MeshInstance(floor);
        floorInstance.setScale(.2f);
        floorInstance.setTextureID(floorTexture.getID());

        Random r = new Random();
        for(int i = 0; i < 1000; i++) {

            MeshInstance instance = new MeshInstance(mesh, new Vector3f(r.nextFloat() * 100, r.nextFloat() * 100, r.nextFloat() * 100), new Vector3f(r.nextFloat() * 360f, r.nextFloat() * 360f, r.nextFloat() * 360f), 1f);
            instances.add(instance);
        }
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
                shader.loadViewMatrix(camera);
                mesh.enable();
                {
                    for(MeshInstance instance : instances) {
                        mesh.render(shader, shader.getModelMatrix(), instance, camera);
                    }

                }
                mesh.disable();

                floor.enable();
                {
                    floor.render(shader, shader.getModelMatrix(), floorInstance, camera);
                }
                floor.disable();
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
        floorTexture.cleanup();
        defaultTexture.cleanup();
        mesh.cleanup();
        shader.cleanup();
        window.close();
    }
}
