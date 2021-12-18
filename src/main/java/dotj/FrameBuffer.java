package dotj;

import dotj.Texture;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {

    protected int id;
    protected Texture texture;
    protected boolean ownsTexture;

    public FrameBuffer(Texture texture, boolean ownsTexture) throws Exception {
        this.texture = texture;
        this.ownsTexture = ownsTexture;


        glBindTexture(GL_TEXTURE_2D, texture.getID());
        id = glGenFramebuffersEXT();
        glBindFramebufferEXT(GL_FRAMEBUFFER, id);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.getID(), 0);

        int result = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        System.out.println(id);
        if(result != GL_FRAMEBUFFER_COMPLETE){
            glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
            glDeleteBuffers(id);
            throw new Exception("exception " + result + " when checking FBO status");
        }
        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
    }

    public FrameBuffer(Texture texture) throws Exception {
        this(texture, false);
    }

    public FrameBuffer(int width, int height) throws Exception {
        this(new Texture(width, height).createBlankTexture(), true);
    }

    public void begin(){
        if(id == 0){
            throw new IllegalStateException("can't use FBO as it has been destroyed..");
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER, id);
    }

    public void end(){
        if(id == 0){
            return;
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
    }

    public void cleanup(){
        if(id == 0){
            return;
        }

        glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
        glDeleteBuffers(id);
        if(ownsTexture){
            texture.cleanup();
        }

        id = 0;
    }
}
