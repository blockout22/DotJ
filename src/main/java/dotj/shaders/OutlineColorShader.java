package dotj.shaders;

import dotj.PerspectiveCamera;
import dotj.Shader;
import org.joml.Matrix4f;

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
        Matrix4f matrix = createViewMatrix(camera);
        loadMatrix4f(viewMatrix, matrix);
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
