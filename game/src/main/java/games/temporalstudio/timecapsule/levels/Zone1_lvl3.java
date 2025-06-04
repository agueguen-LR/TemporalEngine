package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.physics.Transform;
import games.temporalstudio.temporalengine.rendering.component.MapRender;
import games.temporalstudio.timecapsule.Entity.Enemy;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Set;

public class Zone1_lvl3 implements TimeLevel {
	private Scene pastScene;
	private Set<TimeObject> pastTimeObjects;
	private Scene futurScene;
	private Set<TimeObject> futurTimeObjects;
	private Set<TimeObject> commons;

	public Zone1_lvl3(GameObject pastCamera, GameObject futurCamera, Game game, Player pastPlayer, Player futurPlayer, Medusa pastMedusa, Medusa futureMedusa) {
		this.pastScene = new Scene("Zone1_pastCapsule");
		this.pastScene.addGameObject(pastCamera);
		this.futurScene = new Scene("Zone1_futurCapsule");
		this.futurScene.addGameObject(futurCamera);

		Chest coffre = new Chest("coffre", 17.0f, 23.0f, pastPlayer);
		CompleteKey key = new CompleteKey("Key", 100.0f, 100.0f, pastPlayer.getGameObject(), pastPlayer, coffre);
		pastPlayer.setKey(key);

		commons = Set.of(
				new Wall(new Vector2f(0.0f, 3.0f), new Vector2f(30.0f, 5.3f)),
				new Wall(new Vector2f(0.0f, 3.0f), new Vector2f(0.3f, 30.0f)),
				new Wall(new Vector2f(0.0f, 30.0f), new Vector2f(6.8f, 32.3f)),
				new Wall(new Vector2f(8.2f, 30.0f), new Vector2f(30.0f, 32.3f)),
				new Wall(new Vector2f(29.7f, 3.0f), new Vector2f(30.0f, 6.0f)),
				new Wall(new Vector2f(29.7f, 9.0f), new Vector2f(30.0f, 30.0f)),
				new Wall(new Vector2f(0.0f, 25.0f), new Vector2f(10.0f, 27.3f)),
				new Wall(new Vector2f(16.0f, 25.0f), new Vector2f(30.0f, 27.3f)),
				new Wall(new Vector2f(3.0f, 20.0f), new Vector2f(10.0f, 22.3f)),
				new Wall(new Vector2f(3.0f, 15.0f), new Vector2f(13.0f, 17.3f)),
				new Wall(new Vector2f(11.0f, 10.0f), new Vector2f(19.0f, 12.3f)),
				new Wall(new Vector2f(16.0f, 20.0f), new Vector2f(24.0f, 22.3f)),
				new Wall(new Vector2f(16.0f, 15.0f), new Vector2f(24.0f, 17.3f)),
				new Wall(new Vector2f(22.0f, 10.0f), new Vector2f(27.0f, 12.3f)),
				new Wall(new Vector2f(3.0f, 17.0f), new Vector2f(3.3f, 22.3f)),
				new Wall(new Vector2f(9.7f, 22.0f), new Vector2f(10.0f, 27.3f)),
				new Wall(new Vector2f(12.7f, 17.0f), new Vector2f(13.0f, 23.0f)),
				new Wall(new Vector2f(16.0f, 22.0f), new Vector2f(16.3f, 27.3f)),
				new Wall(new Vector2f(18.7f, 8.0f), new Vector2f(19.0f, 12.3f)),
				new Wall(new Vector2f(11.0f, 12.0f), new Vector2f(11.3f, 17.3f)),
				new Wall(new Vector2f(22.0f, 12.0f), new Vector2f(22.3f, 17.3f)),
				new Wall(new Vector2f(26.7f, 12.0f), new Vector2f(27.0f, 22.3f)),
				new Wall(new Vector2f(23.7f, 17.0f), new Vector2f(24.0f, 22.3f)),
				new Wall(new Vector2f(0.0f, 5.0f), new Vector2f(2.3f, 12.3f)),
				new Wall(new Vector2f(0.0f, 5.0f), new Vector2f(5.3f, 8.3f)),
				new Wall(new Vector2f(7.7f, 9.0f), new Vector2f(11.3f, 15.0f)),
				new Wall(new Vector2f(4.7f, 12.0f), new Vector2f(7.7f, 15.0f)),
				new Wall(new Vector2f(18.7f, 26.0f), new Vector2f(30.0f, 30.0f))
		);


		pastTimeObjects = Set.of(
				new Exit(
						"Zone1_pastCapsule_Exit", 7.0f, 31.0f, pastPlayer, pastMedusa,
						"Zone1_lvl1_Past", game::changeLeftScene, new Vector2f(1.0f, 1.0f)
				),
				new Exit(
						"Zone1_pastCapsule_Exit2", 30.0f, 7.0f, pastPlayer, pastMedusa,
						"Zone1_lvl1_Past", (name) -> {
					Game.LOGGER.info("Jeu terminé !!");
				}, new Vector2f(7.0f, 28.0f)
				),
				coffre,
				new KeyFragment("Fragment_cle1", 8.0f, 23.0f, pastPlayer.getGameObject(), pastPlayer, coffre, 1),
				new KeyFragment("Fragment_cle2", 22.0f, 18.0f, pastPlayer.getGameObject(), pastPlayer, coffre, 2),
				pastPlayer,
				pastMedusa,
				key
		);

		this.futurTimeObjects = Set.of(
				new Exit(
						"Zone1_pastCapsule_Exit", 7.0f, 31.0f, futurPlayer, futureMedusa,
						"Zone1_lvl1_Past", game::changeLeftScene, new Vector2f(0.0f, 0.0f)
				),
				new Exit(
						"Zone1_pastCapsule_Exit2", 30.0f, 7.0f, futurPlayer,futureMedusa,
						"Zone1_lvl1_Past", (name) -> {
					Game.LOGGER.info("Jeu terminé !!");
				}, new Vector2f(7.0f, 28.0f)
				),
				futurPlayer,
				futureMedusa
		);

		this.commons.forEach(timeObject -> this.pastScene.addGameObject(timeObject.getGameObject()));
		this.commons.forEach(timeObject -> this.futurScene.addGameObject(timeObject.getGameObject()));

		pastScene.addGameObject(createMap("past"));
		futurScene.addGameObject(createMap("futur"));
		pastTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));
		futurTimeObjects.forEach((timeObject) -> this.pastScene.addGameObject(timeObject.getGameObject()));


	}

	public Scene getPastScene() {
		return pastScene;
	}

	@Override
	public Scene getFuturScene() {
		return futurScene;
	}

	private GameObject createMap(String textureName){
		GameObject map = new GameObject("map");
		map.addComponent(new MapRender(textureName, "zone1_lvl3"));
		return map;
	}

}
