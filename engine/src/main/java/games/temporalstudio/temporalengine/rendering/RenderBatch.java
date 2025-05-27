package games.temporalstudio.temporalengine.rendering;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

import org.lwjgl.system.MemoryUtil;

import games.temporalstudio.temporalengine.LifeCycleContext;

public class RenderBatch implements RenderLifeCycle{

	private static final int SHAPE_VERTEX_COUNT = 4;
	private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
	private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE*Float.BYTES;

	private float[] vertices = new float[]{
		-1,  1,	1, 0, 0, 1,
		 1,  1,	0, 1, 0, 1,
		 1, -1,	0, 0, 1, 1,
		-1, -1,	1, 0, 1, 1
	};
	private int[] indices = new int[]{
		3, 1, 0,
		3, 2, 1
	};

	private int vao, vbo, ebo;
	
	public RenderBatch(int size){}

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		// VertexArrayObject + Vertex attribute pointers
		vao = glGenVertexArrays();
		// VertexBufferObject
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);

		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false,
			VERTEX_SIZE*Float.BYTES, POS_OFFSET
		);
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false,
			VERTEX_SIZE*Float.BYTES, COLOR_OFFSET
		);

		glBindBuffer(GL_ARRAY_BUFFER, (int) MemoryUtil.NULL);
		glBindVertexArray((int) MemoryUtil.NULL);

		// ElementArrayBufferObject
		ebo = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, (int) MemoryUtil.NULL);
	}
	@Override
	public void start(LifeCycleContext context){
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}
	@Override
	public void render(LifeCycleContext context){
		glDrawElements(GL_TRIANGLES, indices.length,
			GL_UNSIGNED_INT, MemoryUtil.NULL
		);
	}
	@Override
	public void destroy(LifeCycleContext context){
		glBindVertexArray((int) MemoryUtil.NULL);
		glBindBuffer(GL_ARRAY_BUFFER, (int) MemoryUtil.NULL);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, (int) MemoryUtil.NULL);

		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
	}
}
