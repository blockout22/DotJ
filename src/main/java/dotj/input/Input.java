package dotj.input;

import java.util.ArrayList;

import dotj.interfaces.PressedEvent;
import dotj.interfaces.ReleasedEvent;
import org.lwjgl.glfw.GLFW;


public class Input {
    public static ArrayList<GLFWKey> downKeys = new ArrayList<GLFWKey>();
    public static ArrayList<GLFWKey> downMouseButtons = new ArrayList<GLFWKey>();

    public static void update(long window) {
        for (int i = 0; i < downKeys.size(); i++) {
            isKeyReleased(window, downKeys.get(i));
        }

        for(int i = 0; i < downMouseButtons.size(); i++)
        {
            isMouseReleased(window, downMouseButtons.get(i));
        }
    }

    public static boolean isKeyDown(long window, GLFWKey keyCode) {
        int key = GLFW.glfwGetKey(window, keyCode.getKeyCode());
        boolean isDown;

        if (key == GLFW.GLFW_PRESS) {
            isDown = true;
        } else {
            isDown = false;
        }

        return isDown;
    }

    public static void KeyEvent(long window, GLFWKey key, PressedEvent evtPressed, ReleasedEvent evtReleased){
       if(isKeyPressed(window, key)){
           evtPressed.exec();
       }
       if(isKeyReleased(window, key)){
           evtReleased.exec();
       }
    }

//    public static void KeyEventDown(long window, GLFWKey key, PressedEvent evtPressed, ReleasedEvent evtReleased){
//        if(isKeyDown(window, key)){
//            evtPressed.exec();
//        }
//
//        if(isKeyReleased(window, key)){
//            evtReleased.exec();
//        }
//    }


    private static boolean isKeyPressed(long window, GLFWKey keyCode) {
        if (isKeyDown(window, keyCode)) {
            for (int i = 0; i < downKeys.size(); i++) {
                if (downKeys.get(i).equals(keyCode)) {
                    return false;
                }
            }
            downKeys.add(keyCode);
            return true;
        }

        return false;
    }

    /**
     * requires isKeyPressed called before
     * @param window
     * @param keyCode
     * @return
     */
    private static boolean isKeyReleased(long window, GLFWKey keyCode) {
        isKeyPressed(window, keyCode);
        if (!isKeyDown(window, keyCode)) {
            for (int i = 0; i < downKeys.size(); i++) {
                if (downKeys.get(i).equals(keyCode)) {
                    downKeys.remove(i);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isMousePressed(long window, GLFWKey keyCode)
    {
        if(isMouseDown(window, keyCode))
        {
            for(int i = 0; i < downMouseButtons.size(); i++){
                if(downMouseButtons.get(i).equals(keyCode)){
                    return false;
                }
            }
            downMouseButtons.add(keyCode);
            return true;
        }
        return false;
    }

    public static boolean isMouseReleased(long window, GLFWKey keyCode)
    {
        isMousePressed(window, keyCode);
        if(!isMouseDown(window, keyCode))
        {
            for(int i = 0; i < downMouseButtons.size(); i++)
            {
                if(downMouseButtons.get(i).equals(keyCode))
                {
                    downMouseButtons.remove(i);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean isMouseDown(long window, GLFWKey keyCode){
        int key = GLFW.glfwGetMouseButton(window, keyCode.getKeyCode());
        boolean isDown;

        if(key == GLFW.GLFW_PRESS){
            isDown = true;
        }else{
            isDown = false;
        }

        return isDown;
    }
}