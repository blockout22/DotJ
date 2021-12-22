package dotj.shaders;

import dotj.Shader;

public class ScreenShader extends Shader {

    public int screenTexture;
    public ScreenShader() {
        super("ScreenShader_vs.glsl", "ScreenShader_fs.glsl");

        bindAttribLocation(0, "aPos");
        bindAttribLocation(1, "aTexCoords");
        linkAndValidate();

        screenTexture = getUniformLocation("screenTexture");
//        bind();
//        loadInt(screenTexture, 0);
    }
}
