package games.temporalstudio.timecapsule.Entity;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.physics.Collider2D;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.physics.shapes.AABB;
import org.joml.Vector2f;

public class Enemy extends Entity{


    public Enemy(Scene scene, Vector2f[] coords,
		Vector2f player_death_position
	){
        super("dracula",
			new Vector2f(coords[0].x, coords[0].y),
			new Vector2f(1,1),
            new float[]{ 0.1f, 10, 0.1f, 0},
			"bat"
		);

        if(coords.length == 1)
            throw new IllegalArgumentException(
				"Not enough coordinate points"
			);

		getRender().setTileName("fly");

        Collider2D collider = new Collider2D(new AABB(getTransform()));
        collider.setOnIntersects((context, other) -> {
			if (!(other instanceof GameObject objet)) return;

			if(objet.getName().equals("player")){
				objet.getComponent(Transform.class)
					.setPosition(new Vector2f(player_death_position));
			}
		});
        getGameObject().addComponent(collider);

        for(int i = 0; i < coords.length; i++){
			final int finalI = i;
            int nextIdx = i + 1 >= coords.length ? 0 : i + 1;

            GameObject anchor = new GameObject("enemyAnchor");
            Transform anchorTrans = new Transform(coords[i]);
            Collider2D anchorCol = new Collider2D((new AABB(getTransform())));
            anchorCol.setOnIntersects((context, other) -> {
				if(other instanceof GameObject objet){
					if(objet.getName() =="dracula"){
						float XDistance = coords[nextIdx].x - coords[finalI].x;
						float YDistance = coords[nextIdx].y - coords[finalI].y;
						Vector2f vitesse = this.getPhysicsBody().getVelocity();
						this.getPhysicsBody().applyForce(
							-vitesse.x, -vitesse.y
						);
						this.getPhysicsBody().applyForce(
							XDistance * 2, YDistance * 2
						);
					}
				}
			});
            anchor.addComponent(anchorCol);
            anchor.addComponent(anchorTrans);
            scene.addGameObject(anchor);
        }
    }
}