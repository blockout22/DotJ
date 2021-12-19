package dotj;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TextureLoader {
    public static Texture loadTexture(String textureFile) {

        Texture gbTexture = Global.texturePool.get(textureFile);

        if(gbTexture != null){
            System.out.println("Loaded Texture from pool");
            return gbTexture;
        }

        try {
            InputStream in = null;
            BufferedImage image;
            File file = new File(Utilities.getAssetDir() + textureFile);
            if(!file.exists()) {
                in = TextureLoader.class.getResourceAsStream("/" + textureFile);
                image = ImageIO.read(in);
            }else{
                image = ImageIO.read(file);
            }

            if(image == null){
                return null;
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

            if(in != null) {
                in.close();
            }

            Texture texture = new Texture(image.getWidth(), image.getHeight());
            texture.genTextureID(buffer);

            Global.texturePool.put(textureFile, texture);

            return texture;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

    }
}