package dotj.shaders;

import dotj.PerspectiveCamera;
import dotj.Shader;
import org.joml.Matrix4f;

public class DepthShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    public DepthShader() {
        super("Depth_vs.glsl", "Depth_fs.glsl");

        bindAttribLocation(0, "position");
        linkAndValidate();

        modelMatrix = getUniformLocation("modelMatrix");
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");
    }

    public void loadViewMatrix(PerspectiveCamera camera) {
        Matrix4f matrix = createViewMatrix(camera);
        loadMatrix4f(viewMatrix, matrix);
    }

    public int getViewMatrix() {
        return viewMatrix;
    }

    public void setViewMatrix(int viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public int getModelMatrix() {
        return modelMatrix;
    }

    public void setModelMatrix(int modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public int getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(int projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }
}
