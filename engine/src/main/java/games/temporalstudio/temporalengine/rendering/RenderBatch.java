package games.temporalstudio.temporalengine.rendering;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.ColorRender;
import games.temporalstudio.temporalengine.rendering.component.Render;

public class RenderBatch implements RenderLifeCycle{

	private static final int SHAPE_PER_OBJECT = 2;
	private static final int SHAPE_SIZE = 3;
	private static final int SHAPE_VERTEX_COUNT = 4;

	private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
	private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE*Float.BYTES;

	private final int size;
	private FloatBuffer vertices = null;
	private IntBuffer indices = null;

	private int vao, vbo, ebo;

	public RenderBatch(int size){
		this.size = size;
	}

	// FUNCTIONS
	private static IntBuffer generateIndices(int count){
		IntBuffer ib = BufferUtils.createIntBuffer(
			count*SHAPE_PER_OBJECT*SHAPE_SIZE
		);

		int[] indices;
		int indexOffset, vertexOffset;
		for(int i = 0; i < count; i++){
			indices = new int[SHAPE_PER_OBJECT*SHAPE_SIZE];
			indexOffset = 0;
			vertexOffset = i*SHAPE_VERTEX_COUNT;

			indices[indexOffset++] = vertexOffset + 3;
			indices[indexOffset++] = vertexOffset + 1;
			indices[indexOffset++] = vertexOffset + 0;

			indices[indexOffset++] = vertexOffset + 3;
			indices[indexOffset++] = vertexOffset + 2;
			indices[indexOffset++] = vertexOffset + 1;

			ib.put(i*SHAPE_PER_OBJECT*SHAPE_SIZE, indices);
		}

		return ib;
	}
	private boolean updateVerticesAt(int index, GameObject renderable){
		int offset = index*SHAPE_VERTEX_COUNT*VERTEX_SIZE;

		if(renderable.getComponents(Render.class).size() > 1)
			Game.LOGGER.warning(
				"Too many way to render game object %s; Using default renderer."
					.formatted(renderable.getName())
			);


		// Position
		Vector2f position = new Vector2f();

		if(renderable.hasComponent(Transform.class)){
			Transform transform = renderable.getComponent(
				Transform.class
			);

			position = transform.getPosition();
		}

		// Visual
		List<Vector4f> colors = List.of(
			new Vector4f(1, 1, 1, 1),
			new Vector4f(1, 1, 1, 1),
			new Vector4f(1, 1, 1, 1),
			new Vector4f(1, 1, 1, 1)
		);

		switch(renderable.getComponent(Render.class)){
			case ColorRender cr -> {
				colors = cr.getColors();
			}
		}

		int vertexOffset;
		Vector2f vertexPos;
		for(int vertex = 0; vertex < SHAPE_VERTEX_COUNT; vertex++){
			vertexOffset = offset + vertex*VERTEX_SIZE;

			// Vertex position
			vertexPos = new Vector2f(position);
			if(vertex >= 1 && vertex <= 2) vertexPos.add(1, 0);
			if(vertex <= 1) vertexPos.add(0, 1);

			vertices.put(vertexOffset, new float[]{
				vertexPos.x(), vertexPos.y(), 0
			});

			// Vertex color
			vertices.put(vertexOffset + POS_SIZE, new float[]{
				colors.get(vertex).x,
				colors.get(vertex).y,
				colors.get(vertex).z,
				colors.get(vertex).w
			});
		}

		return true;
	}

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){
		// VertexArrayObject + Vertex attribute pointers
		vao = glGenVertexArrays();
		// VertexBufferObject
		vertices = BufferUtils.createFloatBuffer(
			size*SHAPE_VERTEX_COUNT*VERTEX_SIZE
		);
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER,
			vertices.capacity()*Float.BYTES, GL_DYNAMIC_DRAW
		);

		glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false,
			VERTEX_SIZE*Float.BYTES, POS_OFFSET
		);
		glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false,
			VERTEX_SIZE*Float.BYTES, COLOR_OFFSET
		);

		glBindBuffer(GL_ARRAY_BUFFER, (int) MemoryUtil.NULL);
		glBindVertexArray((int) MemoryUtil.NULL);

		// ElementArrayBufferObject
		indices = generateIndices(size).rewind();
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
		if(!(context instanceof Scene scene)){
			Game.LOGGER.severe(
				"RenderBatch update method requires a Scene context."
			);
			return;
		}

		AtomicInteger aInt = new AtomicInteger(0);
		boolean shouldBeUpdated = scene.getGameObjects().stream()
			.filter(go -> go.hasComponent(Render.class))
			.reduce(false,
				(updated, go) -> updateVerticesAt(aInt.getAndIncrement(), go),
				(a, b) -> a || b
			);

		if(shouldBeUpdated)
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices.rewind());

		glDrawElements(GL_TRIANGLES,
			aInt.get()*SHAPE_PER_OBJECT*SHAPE_SIZE,
			GL_UNSIGNED_INT, 0
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
