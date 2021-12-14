package dotj.UI;

import dotj.Shader;

public class NkShader extends Shader {

    public int uniform_tex;
    public int uniform_proj;

    public NkShader() {
        super("nkVertexShader.glsl", "nkFragmentShader.glsl");

        bindAttribLocation(0, "Position");
        bindAttribLocation(1, "TexCoord");
        bindAttribLocation(2, "Color");
        linkAndValidate();

        uniform_tex = getUniformLocation("Texture");
        uniform_proj = getUniformLocation("ProjMtx");
    }


}
