package dotj;

import example.util.IOUtil;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            try (InputStream source = IOUtil.class.getClassLoader().getResourceAsStream(resource); ReadableByteChannel rbc = Channels.newChannel(source)) {
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
}
