package dotj;

import dotj.UI.Nano.vg.NanoVGRenderer;
import dotj.gameobjects.*;
import dotj.gameobjects.components.MeshInstance;
import dotj.input.GLFWKey;
import dotj.input.Input;
import dotj.interfaces.OnFinishedListener;
import dotj.light.DirectionalLight;
import dotj.light.PointLight;
import dotj.physics.PhysicsWorld;
import dotj.shaders.DepthShader;
import dotj.shaders.OutlineColorShader;
import dotj.shaders.WorldShader;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class JApp extends App {

    private Random r = new Random();

    private GLFWWindow window;

    private long last_time = 0;
    private int fps = 0;

    private PerspectiveCamera camera;

    //Shaders
    private WorldShader worldShader;
    private OutlineColorShader outlineColorShader;

    //private ArrayList<MeshInstance> instances = new ArrayList<>();

    //create a texture that will have the very first ID and any mesh without a texture assigned will use this one
    private Texture defaultTexture;

    private float SPEED = 0.01f;

    private DirectionalLight light;
    private PointLight pointLight, pointLight2, pointLight3, pointLight4;

    private Level level;
    private PhysicsWorld physicsWorld;

    private Floor floor;
    private Monkey monkey;
    private Sphere sphere;
    private Cube cube;

    private NanoVGRenderer vgRenderer;

    private Mesh stencilTestMesh;
    private MeshInstance stencilTestMeshInstance;

    public JApp(){
        init();
        update();
        close();
    }

    @Override
    public void init() {

//        Vector2f value = new Vector2f(1920 / 2, 800);
//        Vector2f inA = new Vector2f(0, 0);
//        Vector2f inB = new Vector2f(1920, 1080);
//        Vector2f outA = new Vector2f(0, 0);
//        Vector2f outB = new Vector2f(800, 600);
//        Vector2f returnFloat = Utilities.remapVector2f(value, inA, inB, outA, outB);
//        System.out.println(returnFloat.x + " : " + returnFloat.y);

        float rValue = 1920 / 2;
        float rinA = 0;
        float rinB = 1920;
        float routA = 0;
        float routB = 800;
        float res = Utilities.remapFloat(rValue, rinA, rinB, routA, routB);
//        System.out.println(res);


        physicsWorld = new PhysicsWorld();

        window = new GLFWWindow(800, 600, "GLFW Window");
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        camera = new PerspectiveCamera(window, 70, 0.1f, 100000f);
        camera.setPosition(new Vector3f(0, 10f, 10f));

        worldShader = new WorldShader();
        worldShader.bind();
        worldShader.loadMatrix(worldShader.getProjectionMatrix(), camera.getProjectionMatrix());

        outlineColorShader = new OutlineColorShader();
        outlineColorShader.bind();
        outlineColorShader.loadMatrix(outlineColorShader.getProjectionMatrix(), camera.getProjectionMatrix());

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

        Vector3f direction = new Vector3f(-1, 0, -1f);
        Vector3f ambient = new Vector3f(0.2f, 0.2f, 0.2f);
        Vector3f diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f specular = new Vector3f(1f, 1f, 1f);
        light = new DirectionalLight(direction, ambient, diffuse, specular);

        pointLight = new PointLight();
        pointLight2 = new PointLight();
        pointLight3 = new PointLight();
        pointLight4 = new PointLight();

        pointLight2.setPosition(new Vector3f(0, 0, -50));
        pointLight3.setPosition(new Vector3f(0, 0, 20));
        pointLight4.setPosition(new Vector3f(50, 0, 50));

        pointLight2.setAmbient(new Vector3f(1f, 0, 0));
        pointLight3.setDiffuse(new Vector3f(0f, 0, 1));

        pointLight.setPosition(new Vector3f(0, 0, -20));
        pointLight.setAmbient(new Vector3f(5, 0, 0));
        pointLight.setDiffuse(new Vector3f(0, 50, 0));
        pointLight.setSpecular(new Vector3f(0, 0, 100));
        pointLight.setConstant(r.nextFloat());
        pointLight.setLinear(r.nextFloat());
        pointLight.setQuadratic(r.nextFloat());

        level = new Level();

        floor = new Floor(camera, worldShader);
        level.addGameObject(floor);
        monkey = new Monkey(camera, worldShader);
        level.addGameObject(monkey);
        sphere = new Sphere(camera, worldShader, physicsWorld);
        level.addGameObject(sphere);
        cube = new Cube(camera, worldShader, physicsWorld);
        level.addGameObject(cube);

        this.vgRenderer = new NanoVGRenderer(window);


        level.load(new OnFinishedListener() {
            @Override
            public void finished() {
                System.out.println("Finished Loading Level");
            }
        });

        stencilTestMesh = ModelLoader.load("cube.fbx");
        stencilTestMeshInstance = new MeshInstance(null, stencilTestMesh);
        stencilTestMeshInstance.getWorldTransform().setPosition(new Vector3f(-25, 5, 0));
    }


    @Override
    public void update() {
        while(!window.shouldClose() && !Global.shouldClose){

            Time.setDelta();
            if (Time.getTime() - last_time >= 1000) {
                last_time = Time.getTime();
                window.setTitle("[FPS: " + fps + "]");
                //System.out.println(fps);
                vgRenderer.setFPS(fps);
                vgRenderer.setPos(camera.getPosition());
//                light.setDirection(new Vector3f(r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1, r.nextFloat() * 2 - 1));
                fps = 0;
//                physicsWorld.step();
            }
            fps++;

            glClearColor(.48828125f, 0.8046875f, .91796875f, 1f);
            glEnable(GL_DEPTH_TEST);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glEnable(GL_MULTISAMPLE);
            glEnable(GL_FRAMEBUFFER_SRGB);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);


            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            worldShader.bind();
            {




                worldShader.setViewPos(camera);

                worldShader.setPointLight(pointLight, 0);
                worldShader.setPointLight(pointLight2, 1);
                worldShader.setPointLight(pointLight3, 2);
//                shader.setPointLight(pointLight4, 3);
                worldShader.setLight(light);

//                shader.setMaterial(new Vector3f(1.0f, 0.5f, 0.31f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);
                worldShader.setMaterial(0, 1, 32.0f);
                worldShader.loadViewMatrix(camera);

                level.update();

                stencilTest_Outline();

            }
            Shader.unbind();

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

            Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_H, () -> {
                System.out.println("go transform- " + cube.getTransform() + " : inst transform- " + cube.instance.getTransform() + " : inst world transform- " + cube.instance.getWorldTransform());
            }, () -> {

            });

            Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_L, () -> {

                sphere.getTransform().setPosition(new Vector3f(10, 10, 10));
            }, () -> {

            });

            Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_M, () -> {

                Transform transform = new Transform();
                transform.setPosition(new Vector3f(1.2f, 2, -5.6f));
                transform.setScale(new Vector3f(.5f, .5f, .5f));
                cube.instance.setWorldTransform(transform);
//                physicsWorld.step();
            }, () -> {

            });

            camera.update();

            window.update();
        }
    }

    private void stencilTest_Outline(){
        //things to draw but not to the stencil buffer
        glStencilMask(0x00);
        {

        }

        //write to the stencil buffer
        glStencilFunc(GL_ALWAYS, 1, 0xFF);
        glStencilMask(0xFF);

        stencilTestMesh.enable();
        {
            stencilTestMeshInstance.setColor(new Vector3f(100, 0, 0));
            worldShader.setColor(stencilTestMeshInstance.getColor());
            stencilTestMeshInstance.setWorldScale(1.0f);
            stencilTestMesh.render(worldShader.getModelMatrix(), stencilTestMeshInstance, camera);
        }
        stencilTestMesh.disable();

        glStencilFunc(GL_NOTEQUAL, 1, 0xFF);
        glStencilMask(0x00);

        glDisable(GL_DEPTH_TEST);
        outlineColorShader.bind();
        outlineColorShader.loadViewMatrix(camera);
        stencilTestMesh.enable();
        {
            stencilTestMeshInstance.setWorldScale(1.05f);
            stencilTestMesh.render(outlineColorShader.getModelMatrix(), stencilTestMeshInstance, camera);
        }
        stencilTestMesh.disable();
        glStencilMask(0xFF);
        glStencilFunc(GL_ALWAYS, 0, 0xFF);
        glEnable(GL_DEPTH_TEST);
    }

    @Override
    public void close() {

        //handle the cleanup of all textures in the texturePool
        for(Texture t : Global.texturePool.values()){
            t.cleanup();
        }

        stencilTestMesh.cleanup();
        level.unload();
        vgRenderer.cleanup();
        defaultTexture.cleanup();
        outlineColorShader.cleanup();
        worldShader.cleanup();
        window.close();
    }
}
