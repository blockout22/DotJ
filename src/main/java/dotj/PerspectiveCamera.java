package dotj;

import dotj.input.GLFWKey;
import dotj.input.Input;
import dotj.interfaces.PressedEvent;
import dotj.interfaces.ReleasedEvent;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.nio.DoubleBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

public class PerspectiveCamera {

    private GLFWWindow window;

    public float y_height = 0;
    private Vector3f position = new Vector3f(0, y_height, 0);
    private float pitch = 0;
    private float yaw = 0;
    private float roll = 0;
    private float pitch_min = -90;
    private float pitch_max = 90;

    private float FOV;
    private float z_near;
    private float z_far;

    private float sensitivity = 0.07f;
    private boolean mouseGrabbed = false;
    private Vector2f previousPos = new Vector2f(-1, -1);
    private Vector2f curPos = new Vector2f(0, 0);

    private Matrix4f projectionMatrix;

    public PerspectiveCamera(GLFWWindow glfwWindow, float fov, float z_near, float z_far) {
        window = glfwWindow;
        this.FOV = fov;
        this.z_near = z_near;
        this.z_far = z_far;

        createProjectionMatrix(window.getWidth(), window.getHeight());
    }
    public void update() {
//        float speed = 0.01f;
//        float x = 0;
//        float z = 0;
//		float y = 0;


        /**
         * grab mouse and allow 3d mouse movement
         */
//        if (Input.isKeyPressed(window.getWindowID(), GLFWKey.KEY_G)) {
//            System.out.println("KEY G");
//            if(!mouseGrabbed)
//            {
//                grabCursor();
//            }else{
//                releaseCursor();
//            }
//        }
//        Input.isKeyReleased(window.getWindowID(), GLFWKey.KEY_G);
        boolean pressed = false;
        boolean released = false;
        Input.KeyEvent(window.getWindowID(), GLFWKey.KEY_G, new PressedEvent() {
            @Override
            public void exec() {
                if(!mouseGrabbed)
                {
                    grabCursor();
                }else{
                    releaseCursor();
                }
            }
        }, new ReleasedEvent() {
            @Override
            public void exec() {

            }
        });

        if(pressed){
            System.out.println("Hello");
        }


        curPos = getCursorPos();
        if (mouseGrabbed) {
            double dx = curPos.x - previousPos.x;
            double dy = curPos.y - previousPos.y;
            yaw += dx * sensitivity;
            pitch += dy * sensitivity;
        }
        previousPos.x = curPos.x;
        previousPos.y = curPos.y;

        if (getPitch() > pitch_max) {
            setPitch(pitch_max);
        } else if (getPitch() < pitch_min) {
            setPitch(pitch_min);
        }
        if(yaw > 360)
        {
            yaw = 0;
        }else if(yaw < 0){
            yaw = 360;
        }
    }

    public boolean isInBounds(float x, float y, float z) {
        if (x - position.x < z_far && x - position.x > -z_far) {
            if (y - position.y < z_far && y - position.y > -z_far - 10) {
                if (z - position.z < z_far && z - position.z > -z_far) {
                    return true;
                }
            }
        }

        return false;
    }

    public Vector2f getCursorPos()
    {
        DoubleBuffer xpos = BufferUtils.createDoubleBuffer(2);
        DoubleBuffer ypos = BufferUtils.createDoubleBuffer(1);
        xpos.rewind();
        xpos.rewind();
        GLFW.glfwGetCursorPos(window.getWindowID(), xpos, ypos);

        double x = xpos.get();
        double y = ypos.get();

        xpos.clear();
        ypos.clear();
        Vector2f result = new Vector2f((float) x, (float) y);
        return result;
    }

    public void grabCursor()
    {
        System.out.println("GRABBED");
        mouseGrabbed = true;
        GLFW.glfwSetInputMode(window.getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public void releaseCursor()
    {
        System.out.println("RELASED");
        mouseGrabbed = false;
        GLFW.glfwSetInputMode(window.getWindowID(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    protected void createProjectionMatrix(int width, int height) {
        // width / height
        float aspectRatio = (float) width / height;
        float y_scale = 1f / (float) Math.tan(Math.toRadians(FOV / 2f)) * aspectRatio;
        float x_scale = y_scale / aspectRatio;
        float frustum_length = z_far - z_near;
//
//        projectionMatrix = new Matrix4();
//        projectionMatrix.m00 = x_scale;
//        projectionMatrix.m11 = y_scale;
//        projectionMatrix.m22 = -((z_far + z_near) / frustum_length);
//        projectionMatrix.m23 = -1;
//        projectionMatrix.m32 = -((2 * z_near * z_far) / frustum_length);
//        projectionMatrix.m33 = 0;

        projectionMatrix = new Matrix4f();
        projectionMatrix.identity();
        projectionMatrix.perspective(70, aspectRatio, z_near, z_far, false, projectionMatrix);

    }

    protected void createOrthographicMatrix(){
    }

    public void moveX(float amt) {
        this.getPosition().x += amt;
    }

    public void moveY(float amt) {
        this.getPosition().y += amt;
    }

    public void moveZ(float amt) {
        this.getPosition().z += amt;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getFOV() {
        return FOV;
    }

    public void setFOV(float fOV) {
        FOV = fOV;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}