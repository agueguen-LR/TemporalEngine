package games.temporalstudio.temporalengine.utils;

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

/**
 * @author Adnan FAIZE
 */
public final class IOUtil {
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newSize) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newSize);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    /**
     * Reads a resource from the classpath or file system into a ByteBuffer.
     * @param resource   The resource path (can be a classpath resource or file system path).
     * @param bufferSize The initial size of the ByteBuffer.
     * @return A ByteBuffer containing the contents of the resource.
     * @throws IOException If failed to read the resource (resource not found or no permissions).
     */
    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel channel = Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) channel.size() + 1);
                while (channel.read(buffer) != -1);
            }
        } else {
            try (InputStream res = ClassLoader.getSystemResourceAsStream(resource);
                 ReadableByteChannel channel = Channels.newChannel(res)) {
                buffer = BufferUtils.createByteBuffer(bufferSize);

                while (true) {
                    int bytesRead = channel.read(buffer);
                    if (bytesRead == -1) { break; }
                    if (buffer.remaining() == 0) { buffer = resizeBuffer(buffer, buffer.capacity() * 2); }
                }
            }
        }

        buffer.flip();
        return buffer;
    }
}
