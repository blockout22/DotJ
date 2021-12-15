package dotj.gameobjects;

import dotj.*;

import java.io.File;

public class Floor extends GameObject{

    private PerspectiveCamera camera;
    private WorldShader shader;
    private MeshInstance floorInstance;
    private Mesh floor;
    private Texture floorTexture;

    public Floor(PerspectiveCamera camera, WorldShader shader) {
        this.camera = camera;
        this.shader = shader;
    }

    public void init(){

        floor = ModelLoader.load("floor.obj");

        floorInstance = new MeshInstance(floor) {
            @Override
            public void execute() {

            }
        };
        floorInstance.setShader(shader);

        floorTexture = TextureLoader.loadTexture("Image.png");
        floorInstance.setScale(.2f);
        floorInstance.setTextureID(floorTexture.getID());
        addComponent(floorInstance);
    }

    @Override
    public void render() {
        floor.enable();
        floor.render(floorInstance.getShader(), floorInstance.getShader().getModelMatrix(), floorInstance, camera);
        floor.disable();
    }

    @Override
    public void cleanup() {
        floorTexture.cleanup();
        floor.cleanup();
    }
}
