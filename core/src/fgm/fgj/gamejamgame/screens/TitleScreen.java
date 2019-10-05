package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Random;

import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.screens.menus.TitleMenu;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;

public class TitleScreen extends ScreenAdapter {
	private final Stage stage;
	private final TitleMenu titleMenu;

	private final ArrayList<AnimatedSprite> animatedStars = new ArrayList<>();
	private GameJamGame game;

	public TitleScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		titleMenu = new TitleMenu(game);
		stage.addActor(titleMenu.getTable());
	}

	private Sprite buildStarSprite(Texture texture, float x, float y) {
		Sprite sprite = new Sprite(texture, 64, 64);
		sprite.setPosition(x, y);
		return sprite;
	}

	Random random = new Random();

	private void buildAnimatedStar(String filename) {
		animatedStars.add(new AnimatedSprite(game.getAsset(filename),
			random.nextFloat() * SCREEN_WIDTH, random.nextFloat() * SCREEN_HEIGHT, 64, 64, 15, .1f, random.nextInt() % 15 + 15));
	}

	private void buildAnimatedBGStar(String filename) {
		int weightTowardSmall = (int) random.nextInt(10);
		float max = 0.25f;
		if (weightTowardSmall >= 1) {
			max = 0.15f;
		}
		animatedStars.add(new AnimatedSprite(game.getAsset(filename),
			random.nextFloat() * SCREEN_WIDTH, random.nextFloat() * SCREEN_HEIGHT, 64, 64, 5, .1f, random.nextInt(5)));
		animatedStars.get(animatedStars.size() - 1).setScale(Math.max(random.nextFloat() * max, 0.05f));
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		for (int i = 0; i < 40; i++) {
			buildAnimatedBGStar("data/stars/bg-star-blue.png");
			buildAnimatedBGStar("data/stars/bg-star-brown.png");
			buildAnimatedBGStar("data/stars/bg-star-green.png");
			buildAnimatedBGStar("data/stars/bg-star-red.png");
			buildAnimatedBGStar("data/stars/bg-star-yellow.png");
		}
		buildAnimatedStar("data/stars/yellow.png");
		buildAnimatedStar("data/stars/white.png");
		buildAnimatedStar("data/stars/blue.png");
		buildAnimatedStar("data/stars/orange.png");
		buildAnimatedStar("data/stars/red.png");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.begin();
		for (AnimatedSprite animatedStar : animatedStars) {
			animatedStar.draw(spriteBatch, delta);
		}
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
