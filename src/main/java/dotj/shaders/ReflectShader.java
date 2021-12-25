package dotj.shaders;

import dotj.Matrix4;
import dotj.PerspectiveCamera;
import dotj.Shader;

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
        Matrix4 matrix = createViewMatrix(camera);
        loadMatrix(view, matrix);
    }
}
