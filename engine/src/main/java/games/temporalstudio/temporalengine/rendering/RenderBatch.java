package games.temporalstudio.temporalengine.rendering;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.joml.Matrix4f;
import org.joml.Vector3f;
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
import games.temporalstudio.temporalengine.rendering.component.View;

public class RenderBatch implements RenderLifeCycle{

	private static final int SHAPE_PER_OBJECT = 2;
	private static final int SHAPE_SIZE = 3;
	private static final int SHAPE_VERTEX_COUNT = 4;

	private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;
	private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE*Float.BYTES;

	private static final String PROJECTION_UNIFORM_NAME = "uProjection";
	private static final String VIEW_UNIFORM_NAME = "uView";

	private final Renderer renderer;
	private final int size;
	private FloatBuffer vertices = null;
	private IntBuffer indices = null;

	private int vao, vbo, ebo;

	public RenderBatch(Renderer renderer, int size){
		this.renderer = renderer;
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
		Matrix4f transformMat = new Matrix4f();

		if(renderable.hasComponent(Transform.class)){
			Transform t = renderable.getComponent(
				Transform.class
			);

			transformMat.translate(new Vector3f(t.getPosition(), 0))
				.scale(new Vector3f(t.getScale(), 0));
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
		Vector4f vertexPos;
		for(int vertex = 0; vertex < SHAPE_VERTEX_COUNT; vertex++){
			vertexOffset = offset + vertex*VERTEX_SIZE;

			// Vertex position
			vertexPos = new Vector4f(0, 0, 0, 1)
				.mul(transformMat.translate(new Vector3f(
					vertex >= 1 && vertex <= 2 ? 1 : 0,
					vertex <= 1 ? 1 : 0,
					0
				), new Matrix4f()));

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

		// Handles camera
		Set<GameObject> cameras = scene.getGOsByComponent(
			View.class
		);
		if(cameras.size() != 1){
			Game.LOGGER.severe(
				"Not any or too many cameras; Not rendering anything."
			);
			return;
		}

		View view = cameras.stream().findFirst().get().getComponent(
			View.class
		);
		int[] size = new int[4];
		glGetIntegerv(GL_VIEWPORT, size);

		renderer.getShader().uploadMatrix4f(
			PROJECTION_UNIFORM_NAME, view.getProjection(size[2], size[3])
		);
		renderer.getShader().uploadMatrix4f(VIEW_UNIFORM_NAME, view.getView());

		// Updates VBO
		AtomicInteger aInt = new AtomicInteger(0);
		boolean shouldBeUpdated = scene.getGOsByComponent(
				Render.class
			).stream().reduce(false,
				(updated, go) -> updateVerticesAt(aInt.getAndIncrement(), go),
				(a, b) -> a || b
			);

		if(shouldBeUpdated)
			glBufferSubData(GL_ARRAY_BUFFER, 0, vertices.rewind());

		// Draws
		glDrawElements(GL_TRIANGLES,
			aInt.get()*SHAPE_PER_OBJECT*SHAPE_SIZE,
			GL_UNSIGNED_INT, 0
		);
	}
	@Override
	public void destroy(LifeCycleContext context){
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);

		glBindVertexArray((int) MemoryUtil.NULL);
		glBindBuffer(GL_ARRAY_BUFFER, (int) MemoryUtil.NULL);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, (int) MemoryUtil.NULL);

		glDeleteBuffers(new int[]{vao, vbo, ebo});
	}
}
