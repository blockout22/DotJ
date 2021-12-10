public class JApp extends App {

    private GLFWWindow window;

    public JApp(){
        init();
        update();
        close();
    }

    @Override
    public void init() {
        window = new GLFWWindow(800, 600, "GLFW Window");
    }

    @Override
    public void update() {
        while(!window.shouldClose()){
            window.update();
        }
    }

    @Override
    public void close() {
        window.close();

    }
}
