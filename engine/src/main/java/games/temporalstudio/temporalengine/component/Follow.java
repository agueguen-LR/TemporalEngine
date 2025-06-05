package games.temporalstudio.temporalengine.component;

import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.LifeCycleContext;
import games.temporalstudio.temporalengine.physics.Transform;

public class Follow implements Component{

	private GameObject target;

	public Follow(GameObject target){
		this.target = target;
	}

	// FUNCTIONS
	public void updatePosition(GameObject subject){
		Transform sTrans = subject.getComponent(Transform.class);
		Transform tTrans = target.getComponent(Transform.class);

		sTrans.setPosition(tTrans.getPosition());
	}

	// LIFECYLE FUNCTIONS
	@Override
	public void init(LifeCycleContext context){}
	@Override
	public void start(LifeCycleContext context){}
	@Override
	public void update(LifeCycleContext context, float delta){
		if(!(context instanceof GameObject go)){
			Game.LOGGER.warning(
				"Trigger can only be used with GameObject context."
			);
			return;
		}

		updatePosition(go);
	}
	@Override
	public void destroy(LifeCycleContext context){}
}
