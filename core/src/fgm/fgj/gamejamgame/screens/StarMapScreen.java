package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.StarMapStar;
import fgm.fgj.gamejamgame.StarType;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;

public class StarMapScreen implements Screen {
	private final GameJamGame game;
	private final PerspectiveCamera camera;
	private final Stage stage;

	private final ArrayList<AnimatedSprite> animatedStars = new ArrayList<>();
	private final Map<StarType, Texture> starTextures = new EnumMap<StarType, Texture>(StarType.class);
	private ArrayList<StarMapStar> nearbyStars;

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

	static int STAR_SPRITE_FRAMES = 15;
	static int STAR_SPRITE_FRAME_SIZE = 64;

	Random random = new Random();

	private void buildAnimatedStar(Texture texture, float x, float y, float size) {
		animatedStars.add(new AnimatedSprite(texture,
			x * SCREEN_WIDTH, y * SCREEN_HEIGHT, STAR_SPRITE_FRAME_SIZE, STAR_SPRITE_FRAME_SIZE, STAR_SPRITE_FRAMES, .1f, random.nextInt(STAR_SPRITE_FRAMES)));
		animatedStars.get(animatedStars.size() - 1).setScale(Math.max(size, 1));
	}

	@Override
	public void show() {
		starTextures.put(StarType.YELLOW, game.getAsset("data/stars/yellow.png"));
		starTextures.put(StarType.WHITE, game.getAsset("data/stars/white.png"));
		starTextures.put(StarType.BLUE, game.getAsset("data/stars/blue.png"));
		starTextures.put(StarType.ORANGE, game.getAsset("data/stars/orange.png"));
		starTextures.put(StarType.RED, game.getAsset("data/stars/red.png"));
		Gdx.input.setInputProcessor(stage);
		nearbyStars = game.getNearbyStars();
		animatedStars.clear();
		for (StarMapStar star : nearbyStars) {
			buildAnimatedStar(starTextures.get(star.solarSystem.starType), star.x, star.y, star.solarSystem.starSize);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.begin();
		for (AnimatedSprite animatedStar : animatedStars) {
			animatedStar.draw(spriteBatch, delta);
		}
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
		animatedStars.clear();
	}
}
