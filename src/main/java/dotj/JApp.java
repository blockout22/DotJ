package dotj;

import dotj.UI.ImGui.*;
import dotj.UI.ImGui.nodes.NodeData;
import dotj.UI.ImGui.nodes.Node_Function;
import dotj.UI.ImGui.nodes.Node_IntToString;
import dotj.UI.ImGui.nodes.Node_PrintConsole;
import dotj.UI.Nano.vg.NanoVGRenderer;
import dotj.example.ReflectionExample;
import dotj.input.GLFWKey;
import dotj.input.Input;
import dotj.levels.Level;
import dotj.levels.TestLevel;
import dotj.physics.PhysicsWorld;
import imgui.flag.ImGuiCond;
import imgui.type.ImString;
import org.joml.Random;
import org.joml.Vector3f;

import static imgui.ImGui.*;
import static imgui.flag.ImGuiWindowFlags.*;
import static org.lwjgl.opengl.GL32.*;

public class JApp extends App {

    private Random r = new Random();

    private GLFWWindow window;

    private long last_time = 0;
    private int fps = 0;

    private PerspectiveCamera camera;

    //create a texture that will have the very first ID and any mesh without a texture assigned will use this one
    private Texture defaultTexture;

    private float SPEED = 0.01f;

    private Level level;
    private PhysicsWorld physicsWorld;

    private NanoVGRenderer vgRenderer;

    private FrameBuffer frameBuffer;

    private SkyBox cubeMap;

    private ReflectionExample reflectionExample;

    // Im Gui
    private ImGuiApp guiApp;
    private Graph nodeEditorGraph;
    private BPGraph blueprintGraph;
    private final ImString str = new ImString(5);
    private final float[] flt = new float[1];


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


        physicsWorld = new PhysicsWorld();

        window = new GLFWWindow(1920, 1080, "GLFW Window");
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        camera = new PerspectiveCamera(window, 70, 0.1f, 100000f);
        camera.setPosition(new Vector3f(0, 10, 10));

        /** a way to load difference types of models using Assimp
        File file = new File("E:/LWJGL/DotJ/src/main/resources/test.obj");
        mesh = ModelLoader.load(file);//new Mesh(); //OBJLoader.load("test.obj");
         */

        defaultTexture = TextureLoader.loadTexture("white.png");

        level = new TestLevel(physicsWorld, camera, window);

        this.vgRenderer = new NanoVGRenderer(window);

        frameBuffer = new FrameBuffer(window.getWidth(), window.getHeight());
        cubeMap = new SkyBox("https://i.pinimg.com/originals/92/33/f4/9233f460aa6b43e937a46dff3857c812.png");
//        cubeMap = new SkyBox("skybox.png");

        reflectionExample = new ReflectionExample();

        guiApp = new ImGuiApp(window);
        nodeEditorGraph = new Graph();
        blueprintGraph = new BPGraph();

        Node_Function func = new Node_Function(blueprintGraph);
        Node_PrintConsole node1 = new Node_PrintConsole(blueprintGraph);
        Node_PrintConsole node2 = new Node_PrintConsole(blueprintGraph);
        Node_PrintConsole node3 = new Node_PrintConsole(blueprintGraph);

        //connect func to node1
        func.flowPin.connectedTo = node1.execPin.getID();
        node1.execPin.connectedTo = func.flowPin.getID();
        NodeData<ImString> data1 = node1.valuePin.getData();
        data1.value.set("Hello 1");

        //connect node1 to node2
        node1.output.connectedTo = node2.execPin.getID();
        node2.execPin.connectedTo = node1.output.getID();
        NodeData<ImString> data2 = node2.valuePin.getData();
        data2.value.set("Hello 2");

        //connect node2 to node3
        node2.output.connectedTo = node3.execPin.getID();
        node3.execPin.connectedTo = node2.output.getID();
        NodeData<ImString> data3 = node3.valuePin.getData();
        data3.value.set("Hello 3");

//        addRandomNode(blueprintGraph);
//        addRandomNode(blueprintGraph);

//        blueprintGraph.createGraphNode("BP Node");
    }

    private void addRandomNode(BPGraph graph) {
//        java.util.Random r = new java.util.Random();
//        final BPNode node = graph.addNode("new Node" + r.nextFloat());
//        node.setName("Some Name");
//
//        final BPPin pin = node.addPin(BPPin.DataType.Flow, BPPin.PinType.Input);
//        pin.setName("In");
//
//        final BPPin outPin = node.addPin(BPPin.DataType.Flow, BPPin.PinType.Output);
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

            frameBuffer.enable();
            glClearColor(.48828125f, 0.8046875f, .91796875f, 1f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
//            glEnable(GL_MULTISAMPLE);
            glEnable(GL_FRAMEBUFFER_SRGB);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            /**
             * Binds and unbinds world shaders
             */
            level.update();


//            reflectionExample.update(camera);

            cubeMap.update(camera);
            frameBuffer.disable();

            //update UI
            //ImGui framework which can be used for development mode
            guiApp.begin();
//            ImGui.showDemoWindow();
            setNextWindowSize(window.getWidth(), window.getHeight(), ImGuiCond.Always);
            setNextWindowPos(getMainViewport().getPosX() + 0, getMainViewport().getPosY() + 0, ImGuiCond.Once);
            if(begin("Hello Viewport", NoTitleBar | NoMove | NoResize | NoScrollbar | NoCollapse | NoBringToFrontOnFocus)){
                image(frameBuffer.getFrameBufferTexture(), getWindowSizeX(), getWindowSizeY(), 0, 1, 1, 0);

            ImBlueprint.show(blueprintGraph);
            ExampleImGuiNodeEditor.show(nodeEditorGraph, 1420, 250);
            ExampleImPlot.show(0, 400);

            }
            end();

            guiApp.end();
            vgRenderer.update();


            Input();

            camera.update();

            window.update();
        }
    }

    private void Input()
    {
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
        }else if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_Q)){
            camera.getPosition().y -= SPEED * Time.getDelta();
        }else if(Input.isKeyDown(window.getWindowID(), GLFWKey.KEY_E)){
            camera.getPosition().y += SPEED * Time.getDelta();
        }

        Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_H, () -> {
//                System.out.println("go transform- " + cube.getTransform() + " : inst transform- " + cube.instance.getTransform() + " : inst world transform- " + cube.instance.getWorldTransform());
        }, () -> {

        });

        Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_L, () -> {
//            sphere.getTransform().setPosition(new Vector3f(0, 0, 0));
            level.load();
        }, () -> {

        });

        Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_M, () -> {

            Transform transform = new Transform();
            transform.setPosition(new Vector3f(1.2f, 2, -5.6f));
            transform.setScale(new Vector3f(.5f, .5f, .5f));
//                cube.instance.setWorldTransform(transform);
//                physicsWorld.step();
        }, () -> {

        });
    }

    @Override
    public void close() {

        //handle the cleanup of all textures in the texturePool
        for(Texture t : Global.texturePool.values()){
            t.cleanup();
        }

        for(Mesh mesh : Global.meshPool.values())
        {
            mesh.cleanup();
        }

        guiApp.cleanup();
        cubeMap.cleanup();
        frameBuffer.cleanup();
        reflectionExample.cleanup();

        level.unload();
        level.cleanup();
        vgRenderer.cleanup();
        defaultTexture.cleanup();
        window.close();
    }
}
