package games.temporalstudio.timecapsule.levels;

import games.temporalstudio.temporalengine.Scene;
import games.temporalstudio.temporalengine.component.GameObject;
import games.temporalstudio.temporalengine.Game;
import games.temporalstudio.temporalengine.rendering.component.MapRender;
import games.temporalstudio.timecapsule.Entity.Enemy;
import games.temporalstudio.timecapsule.Entity.Medusa;
import games.temporalstudio.timecapsule.Entity.Player;
import games.temporalstudio.timecapsule.objects.*;
import org.joml.Vector2f;

import java.util.HashSet;
import java.util.List;
import java.util.SequencedCollection;
import java.util.Set;

public class Zone1_lvl3 implements TimeLevel {

	private Scene pastScene;
	private Scene futurScene;
	private Set<TimeObject> commonObjects;
	private Set<TimeObject> pastTimeObjects;
	private Set<TimeObject> futurTimeObjects;

	private Set<SequencedCollection<Vector2f>> collisions = Set.of(
		List.of(new Vector2f(0, 3), new Vector2f(30, 5.3f)),
		List.of(new Vector2f(0, 3), new Vector2f(0.3f, 30)),
		List.of(new Vector2f(0, 30), new Vector2f(6.8f, 32.3f)),
		List.of(new Vector2f(8.2f, 30), new Vector2f(30, 32.3f)),
		List.of(new Vector2f(29.7f, 3), new Vector2f(30, 6)),
		List.of(new Vector2f(29.7f, 9), new Vector2f(30, 30)),
		List.of(new Vector2f(0, 25), new Vector2f(10, 27.3f)),
		List.of(new Vector2f(16, 25), new Vector2f(30, 27.3f)),
		List.of(new Vector2f(3, 20), new Vector2f(10, 22.3f)),
		List.of(new Vector2f(3, 15), new Vector2f(13, 17.3f)),
		List.of(new Vector2f(11, 10), new Vector2f(19, 12.3f)),
		List.of(new Vector2f(16, 20), new Vector2f(24, 22.3f)),
		List.of(new Vector2f(16, 15), new Vector2f(24, 17.3f)),
		List.of(new Vector2f(22, 10), new Vector2f(27, 12.3f)),
		List.of(new Vector2f(3, 17), new Vector2f(3.3f, 22.3f)),
		List.of(new Vector2f(9.7f, 22), new Vector2f(10, 27.3f)),
		List.of(new Vector2f(12.7f, 17), new Vector2f(13, 23)),
		List.of(new Vector2f(16, 22), new Vector2f(16.3f, 27.3f)),
		List.of(new Vector2f(18.7f, 8), new Vector2f(19, 12.3f)),
		List.of(new Vector2f(11, 12), new Vector2f(11.3f, 17.3f)),
		List.of(new Vector2f(22, 12), new Vector2f(22.3f, 17.3f)),
		List.of(new Vector2f(26.7f, 12), new Vector2f(27, 22.3f)),
		List.of(new Vector2f(23.7f, 17), new Vector2f(24, 22.3f)),
		List.of(new Vector2f(0, 5), new Vector2f(2.3f, 12.3f)),
		List.of(new Vector2f(0, 5), new Vector2f(5.3f, 8.3f)),
		List.of(new Vector2f(7.7f, 9), new Vector2f(11.3f, 15)),
		List.of(new Vector2f(4.7f, 12), new Vector2f(7.7f, 15)),
		List.of(new Vector2f(18.7f, 26), new Vector2f(30, 30))
	);
	private Set<SequencedCollection<Vector2f>> enemies = Set.of(
		List.of(
			new Vector2f(8, 23),
			new Vector2f(22, 18)
		)
	);

	public Zone1_lvl3(
		Game game,
		GameObject pastCamera, GameObject futurCamera,
		Player pastPlayer, Player futurPlayer,
		Medusa pastMedusa, Medusa futureMedusa
	){
		this.pastScene = new Scene("zone1_lvl3_past");
		this.futurScene = new Scene("zone1_lvl3_futur");

		// Game objects
		Chest coffre = new Chest("coffre", 17, 23, pastPlayer);
		CompleteKey key = new CompleteKey("Key", 100, 100,
			pastPlayer.getGameObject(), pastPlayer, coffre
		);
		pastPlayer.setKey(key);

		this.commonObjects = Set.copyOf(
			this.collisions.stream()
				.map(positions -> new Wall(
					positions.getFirst(), positions.getLast()
				))
				.toList()
		);

		this.pastTimeObjects = Set.of(
			new Exit(
				"zone1_lvl3_exit", 7, 32,
				pastPlayer, pastMedusa,
				"zone1_lvl1_past",
				game::changeLeftScene,
				new Vector2f(16, 5)
			),
			new Exit(
				"zone1_lvl3_exit2", 30, 7,
				pastPlayer, pastMedusa,
				"zone1_lvl1_past",
				(name)  -> Game.LOGGER.info("Jeu terminé !!"),
				new Vector2f(7, 28)
			),
			pastPlayer, pastMedusa,
			new KeyFragment("key_fragment1", 8, 23,
				pastPlayer.getGameObject(), pastPlayer, coffre, 1
			),
			new KeyFragment("key_fragment2", 22, 18,
				pastPlayer.getGameObject(), pastPlayer, coffre, 2
			),
			key,
			coffre
		);

		this.futurTimeObjects = new HashSet<>();
		this.futurTimeObjects.addAll(Set.of(
			new Exit(
				"exit_zone1_lvl3_futur", 7, 31,
				futurPlayer, futureMedusa,
				"zone1_lvl1_futur", game::changeLeftScene,
				new Vector2f(16, 5)
			),
			new Exit(
				"exit_zone1_lvl3_futur", 30, 7,
				futurPlayer,futureMedusa,
				"zone1_lvl1_futur",
				(name) -> Game.LOGGER.info("Jeu terminé !!"),
				new Vector2f(7, 28)
			),
			futurPlayer, futureMedusa
		));
		this.futurTimeObjects.addAll(
			enemies.stream()
				.map(positions -> new Enemy(
					futurScene,
					positions.toArray(new Vector2f[positions.size()]),
					new Vector2f()
				))
				.toList()
		);

		// Adds to scenes
		this.commonObjects.forEach(timeObject ->{
			this.pastScene.addGameObject(timeObject.getGameObject());
			this.futurScene.addGameObject(timeObject.getGameObject());
		});

		this.pastScene.addGameObject(createMap("past"));
		this.pastScene.addGameObject(pastCamera);
		this.pastTimeObjects.forEach(timeObject ->
			this.pastScene.addGameObject(timeObject.getGameObject())
		);
		
		this.futurScene.addGameObject(createMap("future"));
		this.futurScene.addGameObject(futurCamera);
		this.futurTimeObjects.forEach(timeObject ->
			this.futurScene.addGameObject(timeObject.getGameObject())
		);
	}

	// GETTERS
	@Override
	public Scene getPastScene(){ return pastScene; }
	@Override
	public Scene getFuturScene(){ return futurScene; }

	// FUNCTIONS
	private static GameObject createMap(String textureName){
		GameObject map = new GameObject("map");
		map.addComponent(new MapRender(textureName, "zone1_lvl3"));
		return map;
	}
}
