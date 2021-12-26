package dotj.levels;

import dotj.PerspectiveCamera;
import dotj.gameobjects.GameObject;
import dotj.interfaces.OnFinishedListener;
import dotj.physics.PhysicsWorld;
import dotj.shaders.WorldShader;

import java.util.ArrayList;

public abstract class Level {

    // loading = true by default and will be updated to false if initLoading has been set to true & has finished loading the level
    private boolean isLoading = true;

    private boolean initLoading = false;
    private ArrayList<GameObject> GameObjects = new ArrayList<>();
    private ArrayList<GameObject> GameObjectsInstanced = new ArrayList<>();

    private OnFinishedListener onFinishedListener;
    int count = 0;

    public Level(PhysicsWorld physicsWorld, PerspectiveCamera camera){
    }

    public void update(){
        //if level hasn't been requested to load, don't do anything
        if(initLoading && isLoading){
            continueLoading();
            return;
        }

        if(!isLoading) {
            for (GameObject root : GameObjects) {
                root.update();
            }

            prepareInstancedRender();
            for(GameObject instanced : GameObjectsInstanced){
                instanced.update();
            }
        }
    }

    /**
     * called right before the list of instanced GameObjects are rendered allowing the user to switch shaders
     */
    public abstract void prepareInstancedRender();

    protected void addGameObject(GameObject object){
        GameObjects.add(object);
    }

    protected void addGameObjectInstanced(GameObject object){
        GameObjectsInstanced.add(object);
    }

    public void setOnLevelLoaded(OnFinishedListener onFinishedListener){
        this.onFinishedListener = onFinishedListener;
    }

    public void load(){
        initLoading = true;
    }

    private void continueLoading(){
        for(GameObject i : GameObjectsInstanced){
            i.init();
        }
        if(count >= GameObjects.size() - 1)
        {
            if(onFinishedListener != null){
                onFinishedListener.finished();
            }
            isLoading = false;
        }

        GameObjects.get(count).init();
        count++;
    }

    public void unload(){
        System.out.println("Level Closing...");
        for(GameObject object : GameObjects){
            object.cleanup();
        }

        for(GameObject object : GameObjectsInstanced){
            object.cleanup();
        }
    }

    public abstract void cleanup();
}
