package dotj.shaders;

import dotj.Shader;
import org.joml.Vector2f;

public class FrameBufferShader extends Shader {

    public int screenTexture, textureOffset;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    public FrameBufferShader() {
        super("FrameBuffer_vs.glsl", "FrameBuffer_fs.glsl");

        bindAttribLocation(0, "aPos");
        bindAttribLocation(1, "aTexCoords");
        linkAndValidate();

        screenTexture = getUniformLocation("screenTexture");
        textureOffset = getUniformLocation("textureOffset");
//        bind();
//        loadInt(screenTexture, 0);
    }

    public void addTextureOffset(Vector2f offset){
//        System.out.println(offsetX + " : " + offsetY);
        if(offsetX > 1){
            offsetX = 0;
        }

        if(offsetY > 1){
            offsetY = 0;
        }
        offsetX += offset.x;
        offsetY += offset.y;
        loadVector2f(textureOffset, offsetX, offsetY);
    }
}
