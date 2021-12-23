package dotj.shaders;

import dotj.Matrix4;
import dotj.Shader;

public class SkyboxShader extends Shader {

    public int projection;
    public int view;
    public int skybox;

    public SkyboxShader() {
        super("SkyboxShader_vs.glsl", "SkyboxShader_fs.glsl");

        bindAttribLocation(0, "aPos");
        linkAndValidate();

        projection = getUniformLocation("projection");
        view = getUniformLocation("view");
        skybox = getUniformLocation("skybox");
    }

    public void loadProjection(Matrix4 matrix){
        loadMatrix(projection, matrix);
    }

    public void loadView(Matrix4 matrix) {
        loadMatrix(view, matrix);
    }
}
