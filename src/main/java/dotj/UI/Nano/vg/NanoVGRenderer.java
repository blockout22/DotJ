package dotj.UI.Nano.vg;

import dotj.GLFWWindow;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;

public class NanoVGRenderer {
    private long vg;
    NVGColor nvgColor = NVGColor.create();

    private GLFWWindow glfwWindow;

    private NanoVGButton button;

    public NanoVGRenderer(GLFWWindow glfwWindow){
        this.glfwWindow = glfwWindow;
        this.vg = nvgCreate(NVG_STENCIL_STROKES);
        if(vg == MemoryUtil.NULL){
            System.out.println("Could not init nanovg");
        }

        button = new NanoVGButton(vg, glfwWindow);
    }

    public void update() {
        glDisable(GL_DEPTH_TEST);
        nvgBeginFrame(vg, glfwWindow.getWidth(), glfwWindow.getHeight(), 1);
        {
            nvgBeginPath(vg);
            nvgRect(vg, 0, glfwWindow.getHeight() - 100, glfwWindow.getWidth(), 50);
            nvgColor.r(.1f);
            nvgColor.g(.2f);
            nvgColor.b(1f);
            nvgColor.a(.5f);
            nvgFillColor(vg, nvgColor);
            nvgFill(vg);

            button.update();
        }
        nvgEndFrame(vg);


        handleInput();

        restore();
    }

    private void handleInput() {

    }

    public void restore(){
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public void cleanup(){
        nvgColor.free();
        button.cleanup();
    }
}
