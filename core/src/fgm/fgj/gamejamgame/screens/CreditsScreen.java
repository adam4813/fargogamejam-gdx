package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fgm.fgj.gamejamgame.AnimatedImage;
import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.StarField;
import fgm.fgj.gamejamgame.screens.menus.TitleMenu;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;
import static fgm.fgj.gamejamgame.GameJamGame.quit;

public class CreditsScreen extends ScreenAdapter {
	private static final float CREDITS_DISPLAY = 10.0f;
	float displayTime = 0;
	private final Stage stage;

	private GameJamGame game;
	private StarField backgroundStars;

	public CreditsScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		Table root = new Table();
		root.setFillParent(true);
		Animation loseAnimation = AnimatedSprite.buildAnimation(game.getAsset("data/scenes/credits.png"),1280, 720, 1, .2f );
		AnimatedImage image = new AnimatedImage(loseAnimation, true);
		image.setPosition(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
		root.add(image).center();
		stage.addActor(root);
		//stage.setDebugAll(true);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		backgroundStars = new StarField(40);
	}

	@Override
	public void render(float delta) {
		displayTime += delta;
		if (displayTime > CREDITS_DISPLAY) {
			quit();
		}
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
	public void hide() {
		super.hide();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
