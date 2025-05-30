package games.temporalstudio.temporalengine.utils;

import org.lwjgl.BufferUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Adnan FAIZE
 */
public final class NIOUtils{

	// FUNCTIONS
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

		Optional<URL> opURL = Optional.ofNullable(
			ClassLoader.getSystemResource(resource)
		);
		if(opURL.isEmpty())
			throw new FileNotFoundException();

		try(SeekableByteChannel channel = Files.newByteChannel(
			Path.of(opURL.get().getPath())
		)){
			buffer = BufferUtils.createByteBuffer((int) channel.size() + 1);

			while(channel.read(buffer) != -1);
		}

		System.out.println(buffer);

        buffer.flip();
        return buffer;
    }
}
