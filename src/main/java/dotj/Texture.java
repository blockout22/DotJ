package dotj;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

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
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        this.ID = textureID;
    }

    public Texture createBlankTexture() {
        Texture texture = new Texture(width, height);
        ByteBuffer buffer = ByteBuffer.allocateDirect(texture.getWidth() * texture.getHeight() * 4);
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