package games.temporalstudio.temporalengine.rendering.component;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.component.Component;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Transform;

public class View implements Component{

	private static final float Z_NEAR = 0;
	private static final float Z_FAR = 100;
	private static final float DEFAULT_DISTANCE = 20f;
	private static final Vector3f CAMERA_FRONT = new Vector3f(0, 0, -1);
	private static final Vector3f CAMERA_UP = new Vector3f(0, 1, 0);

	private final float zNear;
	private final float zFar;
	private float zoom;
	private float distance;
	private final Vector2f scale = new Vector2f();
	private final Vector2f eyePos = new Vector2f();

	public View(float zNear, float zFar, float zoom){
		this.zNear = zNear;
		this.zFar = zFar;
		this.zoom = zoom;
		this.distance = DEFAULT_DISTANCE;
	}
	public View(float zoom){
		this(Z_NEAR, Z_FAR, zoom);
	}

	// GETTERS
	public Matrix4f getProjection(int width, int height){
		Vector2f sc = new Vector2f(scale).mul(((float) width)/height, 1);

		return new Matrix4f().ortho(
			0, sc.x(), 0, sc.y(),
			zNear, zFar
		);
	}
	public Matrix4f getView(int width, int height){
		Vector2f pos = new Vector2f(eyePos)
			.add(new Vector2f(.5f))
			.sub(.5f/zoom, .5f/zoom)
			.mul(((float) width)/height, 1);

		return new Matrix4f().lookAt(
			new Vector3f(pos, distance),
			new Vector3f(pos, 0).add(CAMERA_FRONT),
			CAMERA_UP
		);
	}

	// LIFECYCLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){
		if(!(context instanceof GameObject go)){
			Game.LOGGER.severe(
				"View update method requires a GameObject context."
			);
			return;
		}

		Transform transform = new Transform();
		if(go.hasComponent(Transform.class))
			transform = go.getComponent(Transform.class);

		// Matrices
		scale.set(transform.getScale()).div(zoom);
		eyePos.set(transform.getPosition());
	}
	@Override
	public void destroy(LifeCycleContext context){}
}
