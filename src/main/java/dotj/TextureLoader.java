package dotj;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TextureLoader {
    public static Texture loadTexture(String textureFile) {

        Texture gbTexture = Global.texturePool.get(textureFile);

        if(gbTexture != null){
            System.out.println("Loaded Texture from pool");
            return gbTexture;
        }

        BufferedImage image = null;
        try {
            image = Utilities.loadImage(textureFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);

        for (int h = 0; h < image.getHeight(); h++) {
            for (int w = 0; w < image.getWidth(); w++) {
                int pixel = pixels[h * image.getWidth() + w];

                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        buffer.flip();

        Texture texture = new Texture(image.getWidth(), image.getHeight());
        texture.genTextureID(buffer);

        Global.texturePool.put(textureFile, texture);

        return texture;

    }
}