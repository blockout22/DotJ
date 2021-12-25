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

    private OnFinishedListener onFinishedListener;
    int count = 0;

    public Level(PhysicsWorld physicsWorld, PerspectiveCamera camera, WorldShader shader){
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
        }
    }

    protected void addGameObject(GameObject object){
        GameObjects.add(object);
    }

    public void setOnLevelLoaded(OnFinishedListener onFinishedListener){
        this.onFinishedListener = onFinishedListener;
    }

    public void load(){
        initLoading = true;
    }

    private void continueLoading(){
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
    }
}
