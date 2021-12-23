package dotj;

import dotj.shaders.FrameBufferShader;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class FrameBuffer {

    //create quad for Frame Buffer
    float vertices[] = { // vertex attributes for a quad that fills the entire screen in Normalized Device Coordinates.
            // positions   // texCoords
            -1.0f,  1.0f,  0.0f, 1.0f,
            -1.0f, -1.0f,  0.0f, 0.0f,
            1.0f, -1.0f,  1.0f, 0.0f,

            -1.0f,  1.0f,  0.0f, 1.0f,
            1.0f, -1.0f,  1.0f, 0.0f,
            1.0f,  1.0f,  1.0f, 1.0f
    };


    int vao;
    int vbo;
    int rbo;

    int frameBuffer;
    int textureColorbuffer;

    private FrameBufferShader shader;

    public FrameBuffer() {
        vao = glGenVertexArrays();
        vbo = glGenBuffers();
        frameBuffer = glGenFramebuffers();
        rbo = glGenRenderbuffers();

        //create quad
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, Utilities.flip(vertices), GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 16, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 16, 8);


        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);

        //Create texture
        textureColorbuffer = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureColorbuffer);
        int[] pixels = new int[800 * 600];
        ByteBuffer buffer = ByteBuffer.allocateDirect(800 * 600 * 4);
        for (int h = 0; h < 600; h++) {
            for (int w = 0; w < 800; w++) {
                int pixel = 0;

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, 800, 600, 0, GL_RGB, GL_UNSIGNED_BYTE, NULL);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glBindTexture(GL_TEXTURE_2D, 0);

        //bind texture to frame buffer
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureColorbuffer, 0);

        //Create Render Buffer
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, 800, 600);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);

        //check if Frame buffer successfully completed
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE){
            System.out.println("Error: FrameBuffer is not complete!");
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        shader = new FrameBufferShader();

        shader.bind();
        shader.loadInt(shader.screenTexture, 0);
    }

    public void enable(){
        //Start rendering to the frame buffer
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);

    }

    public void disable(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        shader.bind();
        glBindVertexArray(vao);
        glDisable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureColorbuffer);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }

    public void cleanup()
    {
        glDeleteBuffers(vbo);
        glDeleteFramebuffers(frameBuffer);
        glDeleteRenderbuffers(rbo);
        glDeleteVertexArrays(vao);
        shader.cleanup();
    }
}
