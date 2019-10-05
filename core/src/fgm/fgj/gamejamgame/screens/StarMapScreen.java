package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.StarField;

public class StarMapScreen implements Screen {
	private final GameJamGame game;
	private final PerspectiveCamera camera;
	private final Stage stage;

	private StarField backgroundStars;
	private StarField nearbyStars;

	public StarMapScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();

		// Camera Setup
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new PerspectiveCamera(75f, w, h);
		camera.near = .1f;
		camera.far = 300f;
		camera.update();

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		backgroundStars = new StarField();
		nearbyStars = new StarField(game.getNearbyStars());
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

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
