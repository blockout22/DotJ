package dotj;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.joml.Math.*;
import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.system.MemoryUtil.memSlice;

public class Utilities {

    public static float remapFloat(float value, float inA, float inB, float outA, float outB){
//        return inB + (value - inA) * (outB - inB) / (outA - inA);
        return outA + (outB - outA) * ((value - inA) / (inB - inA));
    }

    public static Vector2f remapVector2f(Vector2f value, Vector2f inA, Vector2f inB, Vector2f outA, Vector2f outB){
        float xRes = remapFloat(value.x, inA.x, inB.x, outA.x, outB.x);
        float yRes = remapFloat(value.y, inA.y, inB.y, outA.y, outB.y);

        return new Vector2f(xRes, yRes);
    }

    public static float clamp(float value, float min, float max)
    {
        if(value > max){
            value = max;
        }

        if(value < min){
            value = min;
        }

        return value;
    }

    public static String getAssetDir(){
        String curDir = System.getProperty("user.dir");
        String assetDir = curDir + File.separator + "Assets" + File.separator;

        return assetDir;
    }

    public static IntBuffer flip(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public static FloatBuffer flip(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public static int[] toIntArray(List<Integer> list){
        int[] arr = new int[list.size()];

        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }

        return arr;
    }

    public static float[] toFloatArray(List<Float> list){
        float[] arr = new float[list.size()];

        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }

        return arr;
    }

    public static float[] toFloatArray(Vector3f[] vectorArray){
        float[] arr = new float[vectorArray.length * 3];

        int index = 0;
        for (int i = 0; i < vectorArray.length; i++) {
            arr[index++] = vectorArray[i].x;
            arr[index++] = vectorArray[i].y;
            arr[index++] = vectorArray[i].z;
        }

        return arr;
    }

    public static BufferedImage loadImage(String fileName) throws IOException {
        InputStream in = null;
        BufferedImage image;
        File file = new File(Utilities.getAssetDir() + fileName);

        if(!file.exists()) {
            in = TextureLoader.class.getResourceAsStream("/" + fileName);
            //if filename starts with http ... try to load from URL if image isn't found in file system
            if(fileName.startsWith("http")){
                URL url = new URL(fileName);
                String urlFileName = url.getFile().substring(url.getFile().lastIndexOf("/") + 1);

                //check if saved image already exists
                File urlFile = new File(getAssetDir() + urlFileName);
                if(!urlFile.exists()) {
                    image = ImageIO.read(url);
                    ImageIO.write(image, "png", new FileOutputStream(urlFile));
                }else{
                    image = ImageIO.read(urlFile);
                }

            }else {
                image = ImageIO.read(in);
            }
        }else{
            image = ImageIO.read(file);
        }


        if(image == null){
            throw new IOException("Image Not Found!");
        }

        if (in != null) {
            in.close();
        }
        return image;
    }

    public static ByteBuffer[] loadToCubeMap(BufferedImage image, int channels){
        ByteBuffer[] buffers = new ByteBuffer[6];

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        int ww = 0;
        int hh = 0;

        //right
        ByteBuffer rightBuffer = ByteBuffer.allocateDirect((image.getWidth() / 4) * (image.getHeight() / 3) * channels);
        for(int h = image.getHeight() / 3 * 1; h < image.getHeight() / 3 * 2; h++) {
            for (int w = image.getWidth() / 4 * 2; w < image.getWidth() / 4 * 3; w++) {
                int pixel = pixels[h * image.getWidth() + w];

                if (channels > 0) {
                    rightBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    if(channels > 1){
                        rightBuffer.put((byte) ((pixel >> 8) & 0xFF));
                        if(channels > 2){
                            rightBuffer.put((byte) (pixel & 0xFF));
                            if(channels > 3){
                                rightBuffer.put((byte) ((pixel >> 24) & 0xFF));

                            }
                        }
                    }
                }

            }

        }
        rightBuffer.flip();


        //left
        ByteBuffer leftBuffer = ByteBuffer.allocateDirect((image.getWidth() / 4) * (image.getHeight() / 3) * channels);
        for(int h = image.getHeight() / 3 * 1; h < image.getHeight() / 3 * 2; h++) {
            for (int w = 0; w < image.getWidth() / 4 * 1; w++) {
                int pixel = pixels[h * image.getWidth() + w];

                if (channels > 0) {
                    leftBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    if(channels > 1){
                        leftBuffer.put((byte) ((pixel >> 8) & 0xFF));
                        if(channels > 2){
                            leftBuffer.put((byte) (pixel & 0xFF));
                            if(channels > 3){
                                leftBuffer.put((byte) ((pixel >> 24) & 0xFF));

                            }
                        }
                    }
                }

            }

        }
        leftBuffer.flip();

        //top
        ByteBuffer topBuffer = ByteBuffer.allocateDirect((image.getWidth() / 4) * (image.getHeight() / 3) * channels);
        for(int h = 0; h < image.getHeight() / 3 * 1; h++) {
            for (int w = image.getWidth() / 4 * 1; w < image.getWidth() / 4 * 2; w++) {
                int pixel = pixels[h * image.getWidth() + w];

                if (channels > 0) {
                    topBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    if(channels > 1){
                        topBuffer.put((byte) ((pixel >> 8) & 0xFF));
                        if(channels > 2){
                            topBuffer.put((byte) (pixel & 0xFF));
                            if(channels > 3){
                                topBuffer.put((byte) ((pixel >> 24) & 0xFF));

                            }
                        }
                    }
                }

            }
        }
        topBuffer.flip();

        //bottom
        ByteBuffer bottomBuffer = ByteBuffer.allocateDirect((image.getWidth() / 4) * (image.getHeight() / 3) * channels);
        for(int h = image.getHeight() / 3 * 2; h < image.getHeight() / 3 * 3; h++) {
            for (int w = image.getWidth() / 4 * 1; w < image.getWidth() / 4 * 2; w++) {
                int pixel = pixels[h * image.getWidth() + w];

                if (channels > 0) {
                    bottomBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    if(channels > 1){
                        bottomBuffer.put((byte) ((pixel >> 8) & 0xFF));
                        if(channels > 2){
                            bottomBuffer.put((byte) (pixel & 0xFF));
                            if(channels > 3){
                                bottomBuffer.put((byte) ((pixel >> 24) & 0xFF));

                            }
                        }
                    }
                }
            }
        }
        bottomBuffer.flip();

        //front
        ByteBuffer frontBuffer = ByteBuffer.allocateDirect((image.getWidth() / 4) * (image.getHeight() / 3) * channels);
        for(int h = image.getHeight() / 3 * 1; h < image.getHeight() / 3 * 2; h++) {
            for (int w = image.getWidth() / 4 * 1; w < image.getWidth() / 4 * 2; w++) {
                int pixel = pixels[h * image.getWidth() + w];

                if (channels > 0) {
                    frontBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    if(channels > 1){
                        frontBuffer.put((byte) ((pixel >> 8) & 0xFF));
                        if(channels > 2){
                            frontBuffer.put((byte) (pixel & 0xFF));
                            if(channels > 3){
                                frontBuffer.put((byte) ((pixel >> 24) & 0xFF));

                            }
                        }
                    }
                }
            }
        }
        frontBuffer.flip();

        //back
        ByteBuffer backBuffer = ByteBuffer.allocateDirect((image.getWidth() / 4) * (image.getHeight() / 3) * channels);
        for(int h = image.getHeight() / 3 * 1; h < image.getHeight() / 3 * 2; h++) {
            for (int w = image.getWidth() / 4 * 3; w < image.getWidth() / 4 * 4; w++) {
                int pixel = pixels[h * image.getWidth() + w];

                if (channels > 0) {
                    backBuffer.put((byte) ((pixel >> 16) & 0xFF));
                    if(channels > 1){
                        backBuffer.put((byte) ((pixel >> 8) & 0xFF));
                        if(channels > 2){
                            backBuffer.put((byte) (pixel & 0xFF));
                            if(channels > 3){
                                backBuffer.put((byte) ((pixel >> 24) & 0xFF));

                            }
                        }
                    }
                }
            }
        }
        backBuffer.flip();

        buffers[0] = rightBuffer;
        buffers[1] = leftBuffer;
        buffers[2] = topBuffer;
        buffers[3] = bottomBuffer;
        buffers[4] = frontBuffer;
        buffers[5] = backBuffer;

        return buffers;
    }

    public static FloatBuffer toFlippedFloatBuffer(Matrix4f matrix){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(matrix.m00());
        buffer.put(matrix.m01());
        buffer.put(matrix.m02());
        buffer.put(matrix.m03());

        buffer.put(matrix.m10());
        buffer.put(matrix.m11());
        buffer.put(matrix.m12());
        buffer.put(matrix.m13());

        buffer.put(matrix.m20());
        buffer.put(matrix.m21());
        buffer.put(matrix.m22());
        buffer.put(matrix.m23());

        buffer.put(matrix.m30());
        buffer.put(matrix.m31());
        buffer.put(matrix.m32());
        buffer.put(matrix.m33());

        buffer.flip();

        return buffer;
    }

    public static String getShaderDir(){
        return getAssetDir() + "Shaders" + File.separator;
    }

    public static String getModelDir(){
        return getAssetDir() + "Models" + File.separator;
    }

    public static ByteBuffer loadResource(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) {
                    ;
                }
            }
        } else {
            try (InputStream source = Utilities.class.getClassLoader().getResourceAsStream(resource); ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
                    }
                }
            }
        }

        buffer.flip();
        return memSlice(buffer);
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * cleanup memory if argument is NOT NULL
     */
    public static void cleanup(Mesh mesh){
        if(mesh != null){
            mesh.cleanup();
        }
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, Vector3f scale, Vector3f pivot) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();

        if(pivot == null)
        {
            pivot = new Vector3f(0, 0, 0);
        }
        matrix = matrix.translate(new Vector3f(pivot.x, pivot.y, pivot.z), matrix);
//        matrix.scale(scale, matrix);
        //reset rotation
//        matrix.setRotationXYZ(0, 0, 0);

//        matrix.rotateX((float) Math.toRadians(rotation.x), matrix);
//        matrix.rotateY((float) Math.toRadians(rotation.y), matrix);
//        matrix.rotateZ((float) Math.toRadians(rotation.z), matrix);
        matrix.setRotationXYZ((float)Math.toRadians(rotation.x), (float)Math.toRadians(rotation.y), (float)Math.toRadians(rotation.z));
//        matrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix);
//        matrix.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix);
//        matrix.rotate((float)Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix);

        matrix.translate(translation, matrix);

        return matrix;
    }
}
