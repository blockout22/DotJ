package dotj.shaders;

import dotj.PerspectiveCamera;
import dotj.Shader;
import org.joml.Matrix4f;

public class ReflectShader extends Shader {

    public int model;
    public int view;
    public int projection;

    public int cameraPos;
    public int skybox;

    public ReflectShader() {
        super("ReflectShader_vs.glsl", "ReflectShader_fs.glsl");

        bindAttribLocation(0, "aPos");
        bindAttribLocation(1, "aNormal");
        linkAndValidate();

        model = getUniformLocation("model");
        view = getUniformLocation("view");
        projection = getUniformLocation("projection");

        cameraPos = getUniformLocation("cameraPos");
        skybox = getUniformLocation("skybox");
    }

    public void loadViewMatrix(PerspectiveCamera camera) {
        Matrix4f matrix = createViewMatrix(camera);
        loadMatrix4f(view, matrix);
    }
}
