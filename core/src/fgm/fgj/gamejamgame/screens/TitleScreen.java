package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.StarField;
import fgm.fgj.gamejamgame.screens.menus.TitleMenu;

public class TitleScreen extends ScreenAdapter {
	private final Stage stage;
	private final TitleMenu titleMenu;

	private GameJamGame game;
	private StarField backgroundStars;

	public TitleScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		titleMenu = new TitleMenu(game);
		stage.addActor(titleMenu.getTable());
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		backgroundStars = new StarField();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.begin();
		backgroundStars.draw(spriteBatch, delta);
		spriteBatch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
