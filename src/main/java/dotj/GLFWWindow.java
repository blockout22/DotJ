package dotj;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.NkColorf;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GLFWWindow {

    private long windowID;

    public GLFWWindow(int width, int height, String title){
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 4);


        windowID = glfwCreateWindow(width, height, title, NULL, NULL);
        if(windowID == NULL){
            throw new RuntimeException("Failed to create the GLFW Window");
        }

        glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(windowID, true);
            }
        });

        try ( MemoryStack stack = stackPush() ) {
                IntBuffer pWidth = stack.mallocInt(1); // int*
                IntBuffer pHeight = stack.mallocInt(1); // int*

                // Get the window size passed to glfwCreateWindow
                glfwGetWindowSize(windowID, pWidth, pHeight);

                // Get the resolution of the primary monitor
                GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

                // Center the window
                glfwSetWindowPos(windowID,(vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(windowID);
        glfwSwapInterval(1);
        glfwShowWindow(windowID);
        GL.createCapabilities();
    }

    public boolean shouldClose(){
        try (MemoryStack stack = stackPush()) {
            IntBuffer width  = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            glfwGetWindowSize(windowID, width, height);
            glViewport(0, 0, width.get(0), height.get(0));
        }
        return glfwWindowShouldClose(windowID);
    }

    public void update(){
        glfwSwapBuffers(windowID);
        glfwPollEvents();
    }

    public void close(){
        glfwFreeCallbacks(windowID);
        glfwDestroyWindow(windowID);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(getWindowID(), title);
    }

    public long getWindowID() {
        return windowID;
    }

    public int getWidth() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(getWindowID(), width, height);
        return width.get();
    }

    public int getHeight() {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(getWindowID(), width, height);
        return height.get();
    }
}
