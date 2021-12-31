package dotj.levels;

import dotj.*;
import dotj.UI.ImGui.nodes.Node_Function;
import dotj.debug.DebugRender;
import dotj.gameobjects.Cube;
import dotj.gameobjects.Floor;
import dotj.gameobjects.Monkey;
import dotj.gameobjects.Sphere;
import dotj.gameobjects.components.Component;
import dotj.gameobjects.components.MeshInstance;
import dotj.input.GLFWKey;
import dotj.input.Input;
import dotj.interfaces.OnFinishedListener;
import dotj.light.DirectionalLight;
import dotj.light.PointLight;
import dotj.physics.PhysicsWorld;
import dotj.shaders.OutlineColorShader;
import dotj.shaders.WorldInstancedShader;
import dotj.shaders.WorldShader;
import org.joml.Random;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;

public class TestLevel extends Level {

    private PerspectiveCamera camera;

    //Shaders
    private OutlineColorShader outlineColorShader;
    private WorldShader worldShader;
    private WorldInstancedShader worldInstancedShader;

    //Lights
    private DirectionalLight light;
    //Point Lights, currently limited to 4 by the Fragments Shader
    private PointLight pointLight, pointLight2, pointLight3, pointLight4;

    //Game Objects
    private Floor floor;
    private Monkey monkey;
    private Sphere sphere;
    private Cube cube;

    //Stencil Testing
    private Mesh stencilTestMesh;
    private MeshInstance stencilTestMeshInstance;

    public TestLevel(PhysicsWorld physicsWorld, PerspectiveCamera camera, GLFWWindow window){
        super(physicsWorld, camera);
        this.camera = camera;
        this.window = window;

        /**
         * Init Shaders
         */
        worldShader = new WorldShader();
        worldShader.bind();
        worldShader.loadMatrix4f(worldShader.getProjectionMatrix(), camera.getProjectionMatrix());

        worldInstancedShader = new WorldInstancedShader();
        worldInstancedShader.bind();
        worldInstancedShader.loadMatrix4f(worldInstancedShader.getProjectionMatrix(), camera.getProjectionMatrix());

        outlineColorShader = new OutlineColorShader();
        outlineColorShader.bind();
        outlineColorShader.loadMatrix4f(outlineColorShader.getProjectionMatrix(), camera.getProjectionMatrix());

        /**
         * Init Lights
         */
        Vector3f direction = new Vector3f(1, 1, 1f);
        Vector3f ambient = new Vector3f(0.1f, 0.1f, 0.1f);
        Vector3f diffuse = new Vector3f(0.5f, 0.5f, 0.5f);
        Vector3f specular = new Vector3f(1f, 1f, 1f);
        light = new DirectionalLight(direction, ambient, diffuse, specular);

//        setupPointLights();

        pointLight = new PointLight();
        pointLight.setPosition(new Vector3f(0, 25, -10));

        pointLight.setDiffuse(new Vector3f(25, 25, 25));
        pointLight.setAmbient(new Vector3f(5, 5, 5));
        /**
         * Init Game Objects
         */
        floor = new Floor(physicsWorld, camera, worldShader);
        monkey = new Monkey(camera, worldInstancedShader);
        sphere = new Sphere(camera, worldShader, physicsWorld);
        cube = new Cube(camera, worldShader, physicsWorld);

        addGameObject(floor);
        addGameObject(sphere);
        addGameObject(cube);

        addGameObjectInstanced(monkey);

        //Create Test Model for stencil testing
        Model model = ModelLoader.load("cube.fbx");
        stencilTestMesh = new Mesh(model);
        stencilTestMeshInstance = new MeshInstance(null, stencilTestMesh);
        stencilTestMeshInstance.getWorldTransform().setPosition(new Vector3f(-25, 5, 0));

        setOnLevelLoaded(new OnFinishedListener() {
            @Override
            public void finished() {
                System.out.println("Finished Loading Test Level");
            }
        });
        load();
    }

    private void setupPointLights()
    {
        pointLight = new PointLight();
        pointLight2 = new PointLight();
        pointLight3 = new PointLight();
        pointLight4 = new PointLight();

        pointLight2.setPosition(new Vector3f(0, 0, -50));
        pointLight3.setPosition(new Vector3f(0, 0, 20));
        pointLight4.setPosition(new Vector3f(50, 0, 50));

//        pointLight2.setAmbient(new Vector3f(1f, 0, 0));
//        pointLight3.setDiffuse(new Vector3f(0f, 0, 1));

        pointLight.setPosition(new Vector3f(0, 0, -20));
//        pointLight.setAmbient(new Vector3f(5, 0, 0));
//        pointLight.setDiffuse(new Vector3f(0, 50, 0));
//        pointLight.setSpecular(new Vector3f(0, 0, 100));
//        pointLight.setConstant(r.nextFloat());
//        pointLight.setLinear(r.nextFloat());
//        pointLight.setQuadratic(r.nextFloat());

    }

    float value = 0;
    GLFWWindow window;

    @Override
    public void update() {
//        Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_Y, () -> {
//            sphere.getTransform().getRotation().y += 1;
//            sphere.getTransform().apply();
//
////            for(Component c : cube.getComponents()){
////                MeshInstance instance = (MeshInstance) c;
////                instance.calculateWorldTransform();
////            }
//        }, () -> {});

        if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_Y)){
            sphere.getTransform().getRotation().y += 0.1f * Time.getDelta();
            sphere.getTransform().apply();
        }

//        cube.getTransform().setPosition(new Vector3f(camera.getPosition().x, camera.getPosition().y, camera.getPosition().z - 10));
//        if(cube.getComponents().size() > 0) {
//            cube.getComponents().get(0).getLocalTransform().setPosition(0, 0, 5);
//            cube.getComponents().get(0).getLocalTransform().apply();
//            cube.getTransform().setPosition(camera.getPosition());
//            cube.getTransform().setRotation(new Vector3f(camera.getPitch(), camera.getYaw(), camera.getRoll()));
//            cube.getTransform().apply();
//            System.out.println(cube.getComponents().get(0).getWorldTransform());
//        }
        worldShader.bind();
        {
            light.setDirection(camera.getPosition());
            worldShader.setViewPos(camera);
            worldShader.setColor(1f, 1f, 1f);

                worldShader.setPointLight(pointLight, 0);
//                worldShader.setPointLight(pointLight2, 1);
//                worldShader.setPointLight(pointLight3, 2);
//                shader.setPointLight(pointLight4, 3);
            worldShader.setLight(light);

//                shader.setMaterial(new Vector3f(1.0f, 0.5f, 0.31f), new Vector3f(0.5f, 0.5f, 0.5f), 32.0f);
            worldShader.setMaterial(0, 1, 32.0f);
            worldShader.loadViewMatrix(camera);
            
            super.update();



            DebugRender.render(worldShader, camera);
            stencilTest_Outline();

        }
        Shader.unbind();
    }

    @Override
    public void prepareInstancedRender() {
        worldInstancedShader.bind();
        worldInstancedShader.setViewPos(camera);
        worldInstancedShader.setColor(1f, 1f, 1f);
        worldInstancedShader.setLight(light);
        worldInstancedShader.setMaterial(0, 1, 32.0f);
        worldInstancedShader.loadViewMatrix(camera);
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
    public void cleanup() {
        outlineColorShader.cleanup();
        worldShader.cleanup();
        worldInstancedShader.cleanup();
        stencilTestMesh.cleanup();
    }
}
