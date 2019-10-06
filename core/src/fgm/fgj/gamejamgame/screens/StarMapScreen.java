package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.ray3k.rustyrobot.GearTextButton;

import java.util.ArrayList;
import java.util.Random;

import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.StarField;
import fgm.fgj.gamejamgame.StarMapStar;

public class StarMapScreen implements Screen {
	private final GameJamGame game;
	private final Camera camera;
	private final Stage stage;

	private GameDialog worldInfo;

	private StarField backgroundStars;
	private StarField nearbyStarField;

	private Array<Actor> starActors = new Array<>();

	private Table root;
	private Random random = new Random();

	public StarMapScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		//stage.setDebugAll(true);
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);

		// Camera Setup
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera( w, h);

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
		Skin skin = game.getSkin();
		int starCount = random.nextInt(40) + 20;
		backgroundStars = new StarField(starCount);
		nearbyStarField = new StarField();

		{
			StarMapStar currentStar = game.getCurrentStar();
			AnimatedSprite animatedSprite = nearbyStarField.addStar(currentStar);
			Actor actor = new Actor();
			starActors.add(actor);
			actor.setBounds(animatedSprite.getX(), animatedSprite.getY(), 64 * animatedSprite.getScale(), 64 * animatedSprite.getScale());
			actor.setOrigin(32 * animatedSprite.getScale(), 32 * animatedSprite.getScale());
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					Table body = new Table();
					//body.setFillParent(true);
					body.align(Align.center);
					body.add(new Label("Line 1", skin)).padTop(20.0f).padBottom(10.0f).row();
					body.add(new Label("Line 2", skin)).padTop(20.0f).padBottom(10.0f).row();
					body.add(new Label("Line 3", skin)).padTop(20.0f).padBottom(10.0f);
					body.act(1);
					worldInfo.show(stage, currentStar.solarSystem.getName(), body);
				}
			});
			stage.addActor(actor);
		}

		ArrayList<StarMapStar> nearbyStars = game.getNearbyStars();
		for (StarMapStar star : nearbyStars) {
			AnimatedSprite animatedSprite = nearbyStarField.addStar(star);
			Actor actor = new Actor();
			starActors.add(actor);
			actor.setBounds(animatedSprite.getX(), animatedSprite.getY(), 64 * animatedSprite.getScale(), 64 * animatedSprite.getScale());
			actor.setOrigin(32 * animatedSprite.getScale(), 32 * animatedSprite.getScale());
			actor.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					Table body = new Table();
					//body.setFillParent(true);
					body.align(Align.center);
					body.add(new Label("Line 1", skin)).padTop(20.0f).padBottom(10.0f).row();
					body.add(new Label("Line 2", skin)).padTop(20.0f).padBottom(10.0f).row();
					body.add(new Label("Line 3", skin)).padTop(20.0f).padBottom(10.0f);
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
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.begin();
		backgroundStars.draw(spriteBatch, delta);
		nearbyStarField.draw(spriteBatch, delta);

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
