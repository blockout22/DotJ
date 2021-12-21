package dotj;

import org.lwjgl.opengl.*;

import java.nio.ByteBuffer;

public class Texture {
    private int ID;
    private int width;
    private int height;

    public Texture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void genTextureID(ByteBuffer buffer){
        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
//        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL21.GL_SRGB, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL13.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL13.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        this.ID = textureID;
    }

    public Texture createBlankTexture() {
        Texture texture = new Texture(width, height);
        ByteBuffer buffer = ByteBuffer.allocateDirect(texture.getWidth() * texture.getHeight() * 4);

        for (int h = 0; h < texture.getHeight(); h++) {
            for (int w = 0; w < texture.getWidth(); w++) {
                int pixel = 0;
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        texture.genTextureID(buffer);

        return texture;
    }

    public int getID() {
        return ID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void cleanup() {
        GL11.glDeleteTextures(ID);
    }
}