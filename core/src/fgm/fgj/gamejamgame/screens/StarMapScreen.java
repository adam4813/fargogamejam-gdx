package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ray3k.rustyrobot.GearTextButton;

import java.util.ArrayList;
import java.util.Random;

import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.Galaxy;
import fgm.fgj.gamejamgame.GameEvent;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.IconType;
import fgm.fgj.gamejamgame.Icons;
import fgm.fgj.gamejamgame.ScreenNames;
import fgm.fgj.gamejamgame.SolarSystem;
import fgm.fgj.gamejamgame.StarField;
import fgm.fgj.gamejamgame.StarMapStar;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;

public class StarMapScreen implements Screen {
	private final GameJamGame game;
	private final Camera camera;
	private final Stage stage;

	private GameDialog worldInfo;
	private GameDialog eventDialog;

	private StarField backgroundStars;
	private StarField nearbyStarField;

	private Array<Actor> starActors = new Array<>();

	private Table root;
	private Random random = new Random();
	private ExtendViewport viewport;

	public StarMapScreen(GameJamGame game) {
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
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				targetStarMapStar = null;
				worldInfo.hide();
			}
		});
		final GearTextButton visitButton = new GearTextButton("Visit", skin);
		visitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				Gdx.app.log("STAR_MAP", "Going to a star");
				worldInfo.hide();
				game.setCurrentSolarSystem(targetStarMapStar.solarSystem);
				populateStarField();

				Gdx.app.log("STAR_MAP", "Doing and event after arriving!!");
				gameEvent = Galaxy.generateGameEvent(game.getCurrentStar().solarSystem, targetStarMapStar.fuelCost, null, game.getShip());
				Table eventBody = new Table();
				eventBody.align(Align.center);
				eventBody.setFillParent(true);
				Label eventTextLabel = new Label(gameEvent.getEventText(), game.getSkin());
				eventTextLabel.setWrap(true);
				eventBody.add(eventTextLabel).grow().padLeft(128);
				eventDialog.show(stage, gameEvent.isPositive() ? "Success" : "Uh-oh" ,eventBody );
			}
		});
		worldInfo = new GameDialog(game, "Solar System Info", visitButton, cancelButton);

		shipInfoButton.setSize(64, 64);
		shipInfoButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log("STAR_MAP", "Going to current to ship info");
				game.showScreen(ScreenNames.Ship);
			}
		});

		final GearTextButton okButton = new GearTextButton("Ok", skin);
		okButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("STAR_MAP", "Doing and event on a planet!!");
				eventDialog.hide();
				if (gameEvent.didLose()) {
					game.showScreen(ScreenNames.Lose);
				}
			}
		});
		eventDialog = new GameDialog(game, "Event Result", okButton);
		stage.addActor(shipInfoButton);
	}

	private StarMapStar targetStarMapStar;
	private Image shipInfoButton = new Image();;
	private GameEvent gameEvent;

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		populateStarField();

		TextureRegion textureRegion = Icons.getIcon(IconType.SHIP_INFO_BUTTON);
		shipInfoButton.setDrawable(new TextureRegionDrawable(textureRegion));
	}
	ArrayList<StarMapStar> nearbyStars;

	private void populateStarField() {
		Skin skin = game.getSkin();
		int starCount = random.nextInt(40) + 20;
		backgroundStars = new StarField(starCount);
		nearbyStarField = new StarField();

		float spritePadding = 8f;
		float spriteOffset = StarField.STAR_SPRITE_FRAME_SIZE / 4f + spritePadding;
		float spriteWidth = StarField.STAR_SPRITE_FRAME_SIZE / 2f + spritePadding * 2;

		{
			StarMapStar currentStar = game.getCurrentStar();
			AnimatedSprite animatedSprite = nearbyStarField.addStar(currentStar);
			Actor actor = new Actor();
			starActors.add(actor);
			float scale = animatedSprite.getScale();
			actor.setBounds(animatedSprite.getX() - spriteOffset * scale, animatedSprite.getY() - spriteOffset * scale, spriteWidth * scale, spriteWidth * scale);
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					Gdx.app.log("STAR_MAP", "Going to current to star's solar system" + currentStar.solarSystem.getName());
					game.showScreen(ScreenNames.SolarSystem);
				}
			});
			stage.addActor(actor);
		}

		nearbyStars = game.getNearbyStars();
		for (StarMapStar star : nearbyStars) {
			AnimatedSprite animatedSprite = nearbyStarField.addStar(star);
			Actor actor = new Actor();
			starActors.add(actor);
			float scale = animatedSprite.getScale();
			actor.setBounds(animatedSprite.getX() - spriteOffset * scale, animatedSprite.getY() - spriteOffset * scale, spriteWidth * scale, spriteWidth * scale);
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);

					// Set the target to be used when the visit button is clicked, kind of a hackish way to handle it
					targetStarMapStar = star;

					Table body = new Table();
					//body.setFillParent(true);
					body.align(Align.center);
					body.add(new Label("Fuel cost to visit - " + star.fuelCost, skin)).padTop(20.0f).padBottom(10.0f).row();
					body.act(1);
					worldInfo.show(stage, star.solarSystem.getName(), body);
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
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		backgroundStars.draw(spriteBatch, delta);
		spriteBatch.end();
		ShapeRenderer shapeRenderer = game.getShapeRender();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		for (StarMapStar star : nearbyStars) {
			shapeRenderer.line(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, star.x * SCREEN_WIDTH, star.y *  SCREEN_HEIGHT);
		}
		shapeRenderer.end();
		spriteBatch.begin();
		nearbyStarField.draw(spriteBatch, delta);
		spriteBatch.end();
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
