package dotj.UI.Nano.vg;

import dotj.GLFWKey;
import dotj.GLFWWindow;
import dotj.Input;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.nanovg.NVGColor;

import java.nio.DoubleBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

public class NanoVGButton {

    private long ctx;
    private GLFWWindow glfwWindow;
    private NVGColor nvgColor = NVGColor.create();

    private float posX = 50;
    private float posY = 50;
    private float width = 50;
    private float height = 50;

    public NanoVGButton(long ctx, GLFWWindow window){
        this.ctx = ctx;
        this.glfwWindow = window;
    }

    public void update(){
        nvgBeginPath(ctx);
        nvgRect(ctx, posX, posY, width, height);
        nvgColor.r(.75f);
        nvgColor.g(.75f);
        nvgColor.b(75f);
        nvgColor.a(1f);
        nvgFillColor(ctx, nvgColor);
        nvgFill(ctx);

        if(Input.isMousePressed(glfwWindow.getWindowID(), GLFWKey.MOUSE_BUTTON_LEFT)){
            DoubleBuffer xpos = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
            GLFW.glfwGetCursorPos(glfwWindow.getWindowID(), xpos, ypos);

            if((xpos.get(0) > posX && xpos.get(0) < posX + width) && (ypos.get(0) > posY && ypos.get(0) < posY + height)){
                System.out.println("NanoVG Button clicked");
            }
        }

        Input.isMouseReleased(glfwWindow.getWindowID(), GLFWKey.MOUSE_BUTTON_LEFT);
    }

    public void cleanup()
    {
        nvgColor.free();
    }
}
