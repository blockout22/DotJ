package dotj.gameobjects;

import dotj.Mesh;
import dotj.ModelLoader;
import dotj.Texture;
import dotj.TextureLoader;
import dotj.gameobjects.components.MeshInstance;

public class Cube extends GameObject{

    public Mesh mesh;
    private MeshInstance instance;
    private Texture texture;

    @Override
    public void init() {
        mesh = ModelLoader.load("cube.fbx");

        texture = TextureLoader.loadTexture("container2.png");
    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {
        mesh.cleanup();
    }
}
