package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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

public class LoseScreen extends ScreenAdapter {
	private static final float BUTTON_DELAY = 2.0f;
	private final Stage stage;

	private float displayTime = 0f;
	private boolean buttonsAdded = false;

	private GameJamGame game;
	private StarField backgroundStars;

	public LoseScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		Table root = new Table();
		root.setFillParent(true);
		Animation loseAnimation = AnimatedSprite.buildAnimation(game.getAsset("data/scenes/sceneLose.png"),1280, 720, 9, .2f );
		AnimatedImage image = new AnimatedImage(loseAnimation, false);
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

	private void addButtons() {
		Table table = new Table();
		table.setFillParent(true);
		Image playButton = new Image(Icons.getIcon(IconType.PLAY_BUTTON));
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Title);
			}
		});
		playButton.getDrawable().setMinHeight(128);
		playButton.getDrawable().setMinWidth(128);

		Image exitButton = new Image(Icons.getIcon(IconType.EXIT_BUTTON));
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameJamGame.quit();
			}
		});
		exitButton.getDrawable().setMinHeight(128);
		exitButton.getDrawable().setMinWidth(128);

		table.add(playButton).right().pad(32).top();
		table.add(exitButton).left().pad(32).top();
		table.add(new Container());
		stage.addActor(table);
		buttonsAdded = true;
	}

	@Override
	public void render(float delta) {
		displayTime += delta;
		if (displayTime > BUTTON_DELAY && !buttonsAdded) {
			addButtons();
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
	}

	@Override
	public void hide() {
		super.hide();

		displayTime = 0f;
		buttonsAdded = false;
	}
}
