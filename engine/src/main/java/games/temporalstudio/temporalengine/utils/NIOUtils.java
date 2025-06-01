package games.temporalstudio.temporalengine.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;

import org.lwjgl.BufferUtils;

/**
 * @author Adnan FAIZE
 */
public final class NIOUtils{

	private static final int BUFFER_SIZE = 4096;

	// FUNCTIONS
    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newSize){
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newSize);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

	// TODO: Make this code more memory efficient.
	/**
     * Reads a resource from the classpath or file system into a ByteBuffer.
     * @param resource The resource path.
     * @param bufferSize The initial size of the ByteBuffer.
     * @return A ByteBuffer containing the contents of the resource.
     * @throws IOException If failed to read the resource (resource not found or
	 * no permissions).
     */
    public static ByteBuffer getResourceAsByteBuffer(String resource)
		throws IOException
	{
		ByteBuffer buffer;

        Optional<InputStream> opURL = Optional.ofNullable(
			ClassLoader.getSystemResourceAsStream(resource)
		);
		if(opURL.isEmpty())
			throw new FileNotFoundException(resource);

		try(ReadableByteChannel rbc = Channels.newChannel(opURL.get())){
			buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

			while(rbc.read(buffer) != -1)
				if(buffer.remaining() == 0)
					buffer = resizeBuffer(buffer,
						buffer.capacity() + BUFFER_SIZE
					);
		}

        return buffer.flip();
    }
}
