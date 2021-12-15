package dotj;

import dotj.gameobjects.GameObject;
import dotj.interfaces.OnFinishedListener;

import java.util.ArrayList;

public class Level {

    private boolean isLoading = true;
    private ArrayList<GameObject> GameObjects = new ArrayList<>();

    private OnFinishedListener onFinishedListener;
    int count = 0;

    public Level(){

    }

    public void update(){
        if(!isLoading) {
            for (GameObject root : GameObjects) {
                root.update();
            }
        }else{
            load(onFinishedListener);
        }
    }

    public void addGameObject(GameObject object){
        GameObjects.add(object);
    }

    public void load(OnFinishedListener onFinishedListener){
        this.onFinishedListener = onFinishedListener;
        if(count == GameObjects.size() - 1)
        {
            if(onFinishedListener != null){
                onFinishedListener.finished();
                isLoading = false;
            }
        }

        GameObjects.get(count).init();
        count++;



    }

    public void unload(){

    }
}
