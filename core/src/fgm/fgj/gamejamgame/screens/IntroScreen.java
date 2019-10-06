package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fgm.fgj.gamejamgame.AnimatedImage;
import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.IconType;
import fgm.fgj.gamejamgame.Icons;
import fgm.fgj.gamejamgame.ScreenNames;
import fgm.fgj.gamejamgame.StarField;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;

public class IntroScreen extends ScreenAdapter {
	private static final float INTRO_LENGTH = 80f;
	private final Stage stage;

	private float displayTime = 0f;

	private GameJamGame game;
	private StarField backgroundStars;
	Music music;

	public IntroScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		Table root = new Table();
		root.setFillParent(true);
		Animation loseAnimation = AnimatedSprite.buildAnimation(game.getAsset("data/scenes/sceneIntro.png"),1280, 720, 3, 26 );
		AnimatedImage image = new AnimatedImage(loseAnimation, false);
		image.setPosition(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2);
		root.add(image).center();
		stage.addActor(root);
		//stage.setDebugAll(true);
		music = Gdx.audio.newMusic(Gdx.files.internal("data/introVoiceOver.wav"));
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		backgroundStars = new StarField(40);
		music.play();
	}

	private void showStarMap() {
		game.showScreen(ScreenNames.StarMap);
	}

	@Override
	public void render(float delta) {
		displayTime += delta;
		if (displayTime > INTRO_LENGTH) {
			showStarMap();
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
	public void dispose() {
		stage.dispose();
		music.stop();
		music.dispose();
	}

	@Override
	public void hide() {
		super.hide();

		displayTime = 0f;
	}
}
