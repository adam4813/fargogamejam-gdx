package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ray3k.rustyrobot.GearTextButton;

import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.StarField;

public class StarMapScreen implements Screen {
	private final GameJamGame game;
	private final PerspectiveCamera camera;
	private final Stage stage;

	private GameDialog worldInfo;

	private StarField backgroundStars;
	private StarField nearbyStars;

	private Array<Actor> starActors = new Array<>();

	Table root;

	public StarMapScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		// Camera Setup
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new PerspectiveCamera(75f, w, h);
		camera.near = .1f;
		camera.far = 300f;
		camera.update();


		// Ship screen button

		// Info box

		// Planet overview box

		Skin skin = game.getSkin();
		final GearTextButton cancelButton = new GearTextButton("Cancel", skin);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				worldInfo.hide();
			}
		});
		final GearTextButton visitButton = new GearTextButton("Visit", skin);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeListener.ChangeEvent event, Actor actor) {
				Gdx.app.log("STAR_MAP", "Visitng a star");
			}
		});
		worldInfo = new GameDialog(game, "World Info", visitButton, cancelButton);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		backgroundStars = new StarField();
		nearbyStars = new StarField(game.getNearbyStars());
		for (AnimatedSprite star : nearbyStars.getStars()) {
			Actor actor = new Actor();
			starActors.add(actor);
			actor.setBounds(star.getX(), star.getY(), 64 * star.getScale(), 64 * star.getScale());
			actor.setOrigin(32 * star.getScale(), 32 * star.getScale());
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					worldInfo.show(stage, "SOLAR SYSTEM NAME");
				}
			});
			stage.addActor(actor);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.begin();
		backgroundStars.draw(spriteBatch, delta);
		nearbyStars.draw(spriteBatch, delta);

		spriteBatch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
		camera.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		for (Actor actor : starActors) {
			stage.getActors().removeAll(starActors, true);
		}
	}

	@Override
	public void dispose() {

	}
}
