package dotj.shaders;

import dotj.Matrix4;
import dotj.PerspectiveCamera;
import dotj.Shader;

public class OutlineColorShader extends Shader {

    private int viewMatrix, modelMatrix, projectionMatrix;

    public OutlineColorShader() {
        super("OutlineColor_vs.glsl", "OutlineColor_fs.glsl");

        bindAttribLocation(0, "position");
        linkAndValidate();

        modelMatrix = getUniformLocation("modelMatrix");
        projectionMatrix = getUniformLocation("projectionMatrix");
        viewMatrix = getUniformLocation("viewMatrix");
    }

    public void loadViewMatrix(PerspectiveCamera camera) {
        Matrix4 matrix = createViewMatrix(camera);
        loadMatrix(viewMatrix, matrix);
    }

    public int getViewMatrix() {
        return viewMatrix;
    }

    public int getModelMatrix() {
        return modelMatrix;
    }

    public int getProjectionMatrix() {
        return projectionMatrix;
    }
}
