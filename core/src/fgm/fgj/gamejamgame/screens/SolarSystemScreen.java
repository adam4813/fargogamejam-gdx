package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ray3k.rustyrobot.GearTextButton;

import java.util.List;
import java.util.Random;

import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.Galaxy;
import fgm.fgj.gamejamgame.GameEvent;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.OrbitField;
import fgm.fgj.gamejamgame.Planet;
import fgm.fgj.gamejamgame.ScreenNames;
import fgm.fgj.gamejamgame.SolarSystem;
import fgm.fgj.gamejamgame.StarField;

import static fgm.fgj.gamejamgame.StarField.starTextures;

public class SolarSystemScreen implements Screen {
	private final GameJamGame game;
	private final Camera camera;
	private final Stage stage;

	private GameDialog worldInfo;
	private GameDialog eventDialog;

	private StarField backgroundStars;
	private OrbitField orbitField;
	private AnimatedSprite currentStar;

	private Array<Actor> starActors = new Array<>();

	private Table root;
	private Random random = new Random();
	private ExtendViewport viewport;
	private static final String TAG = "SolarSystemScreen";

	public SolarSystemScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);
		//stage.setDebugAll(true);

		// Camera Setup
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(w, h, camera);
		camera.position.set(viewport.getMinWorldWidth() / 2f, viewport.getMinWorldHeight() / 2f, 0);
		camera.update();
		stage.setViewport(viewport);

		// Ship screen button

		// Info box

		// Planet overview box

		Skin skin = game.getSkin();
		final GearTextButton cancelButton = new GearTextButton("Cancel", skin);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				targetPlantet = null;
				worldInfo.hide();
			}
		});
		final GearTextButton visitButton = new GearTextButton("Visit", skin);
		visitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log(TAG, "Doing and event on a planet!!");
				worldInfo.hide();
				gameEvent = Galaxy.generateGameEvent(game.getCurrentStar().solarSystem, 1, targetPlantet, game.getShip());
				Table eventBody = new Table();
				eventBody.align(Align.center);
				eventBody.setFillParent(true);
				Label eventTextLabel = new Label(gameEvent.getEventText(), skin);
				eventTextLabel.setWrap(true);
				eventBody.add(eventTextLabel).grow().padLeft(128);
				//eventBody.act(1);
				eventDialog.show(stage, gameEvent.isPositive() ? "Success" : "Uh-oh" ,eventBody );
				targetPlantet = null;
			}
		});

		final GearTextButton okButton = new GearTextButton("Ok", skin);
		okButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log(TAG, "Doing and event on a planet!!");
				eventDialog.hide();
				if (gameEvent.didLose()) {
					game.showScreen(ScreenNames.Lose);
				}
			}
		});
		worldInfo = new GameDialog(game, "Planet Info", visitButton, cancelButton);
		eventDialog = new GameDialog(game, "Event Result", okButton);
	}

	Planet targetPlantet;
	GameEvent gameEvent;

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		populatePlanetOrbits();
	}

	private static float STAR_SIZE = 10f;

	private void populatePlanetOrbits() {
		Skin skin = game.getSkin();
		int starCount = random.nextInt(40) + 20;
		backgroundStars = new StarField(starCount);

		float spritePadding = 8f;
		float spriteOffset = StarField.STAR_SPRITE_FRAME_SIZE / 4f + spritePadding;
		float spriteWidth = StarField.STAR_SPRITE_FRAME_SIZE / 2f + spritePadding * 2;
		SolarSystem currentSolarSystem = game.getCurrentStar().solarSystem;

		{
			currentStar = StarField.buildAnimatedStar(starTextures.get(currentSolarSystem.starType), -.078f * currentSolarSystem.starSize, .5f, STAR_SIZE * currentSolarSystem.starSize);
			Actor actor = new Actor();
			starActors.add(actor);
			float scale = currentStar.getScale();
			actor.setBounds(currentStar.getX() - spriteOffset * scale, currentStar.getY() - spriteOffset * scale, spriteWidth * scale, spriteWidth * scale);
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					Gdx.app.log("STAR_MAP", "Going to current to the star map");
					game.showScreen(ScreenNames.StarMap);
				}
			});
			stage.addActor(actor);
		}

		orbitField = new OrbitField();
		List<Planet> planets = currentSolarSystem.getPlanets();
		Gdx.app.log("GJG", "Count " + String.valueOf(planets.size()));
		float orbitGap = 1f / (planets.size() + 1);
		int index = 1;
		for (Planet planet : planets) {
			AnimatedSprite animatedSprite = orbitField.addPlanet(planet, orbitGap * index++);
			Actor actor = new Actor();
			starActors.add(actor);
			float scale = animatedSprite.getScale();
			actor.setBounds(animatedSprite.getX() - spriteOffset * scale, animatedSprite.getY() - spriteOffset * scale, spriteWidth * scale, spriteWidth * scale);
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);

					// Set the target to be used when the visit button is clicked, kind of a hackish way to handle it
					targetPlantet = planet;

					Table body = new Table();
					body.align(Align.center);
					body.act(1);
					worldInfo.show(stage, planet.getName(), body);
				}
			});
			stage.addActor(actor);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		camera.update();
		ShapeRenderer shapeRenderer = game.getShapeRender();
		shapeRenderer.setProjectionMatrix(camera.combined);
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);

		// Stars first to draw orbit lines over it
		spriteBatch.begin();
		backgroundStars.draw(spriteBatch, delta);
		currentStar.draw(spriteBatch, delta);
		spriteBatch.end();

		// Orbit lines
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		orbitField.drawOrbits(shapeRenderer);
		shapeRenderer.end();

		// Planets last
		spriteBatch.begin();
		orbitField.drawPlanets(spriteBatch, delta);
		spriteBatch.end();

		// UI
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		stage.getActors().removeAll(starActors, true);
	}

	@Override
	public void dispose() {

	}
}
