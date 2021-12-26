package dotj.shaders;

import dotj.Shader;

public class SkyboxShader extends Shader {

    public int projection;
    public int view;
    public int skybox;
    public int cameraPos;

    public SkyboxShader() {
        super("SkyboxShader_vs.glsl", "SkyboxShader_fs.glsl");

        bindAttribLocation(0, "aPos");
        linkAndValidate();

        projection = getUniformLocation("projection");
        view = getUniformLocation("view");
        cameraPos = getUniformLocation("cameraPos");
        skybox = getUniformLocation("skybox");
    }
}
