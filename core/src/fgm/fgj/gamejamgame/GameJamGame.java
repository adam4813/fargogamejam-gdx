package fgm.fgj.gamejamgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fgm.fgj.gamejamgame.screens.GameScreen;
import fgm.fgj.gamejamgame.screens.LoadingScreen;
import fgm.fgj.gamejamgame.screens.SolarSystemScreen;
import fgm.fgj.gamejamgame.screens.StarMapScreen;
import fgm.fgj.gamejamgame.screens.TitleScreen;

public class GameJamGame extends Game {
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	private Skin skin;
	private final Map<ScreenNames, Screen> screens = new HashMap<>();

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	public ShapeRenderer getShapeRender() {
		return shapeRenderer;
	}

	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private AssetManager assetManager;

	public <T> T getAsset(String filename) {
		return assetManager.get(filename);
	}

	public <T> void loadAsset(String filename, Class<T> classType) {
		assetManager.load(filename, classType);
	}

	public boolean assetsLoaded() {
		return assetManager.update();
	}

	public void loadTexture(String filename) {
		loadAsset(filename, Texture.class);
	}

	public void loadTextureAtlas(String filename) {
		loadAsset(filename, TextureAtlas.class);
	}

	public Skin getSkin() {
		return skin;
	}

	public static void quit() {
		Gdx.app.exit();
	}

	@Override
	public void create() {
		Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);

		assetManager = new AssetManager();

		// UI
		loadTextureAtlas("data/rustyrobotui/rusty-robot-ui.atlas");
		assetManager.load("data/rustyrobotui/rusty-robot-ui.json", Skin.class, new SkinLoader.SkinParameter("data/rustyrobotui/rusty-robot-ui.atlas"));
		assetManager.finishLoading();
		skin = getAsset("data/rustyrobotui/rusty-robot-ui.json");

		// Screens
		screens.put(ScreenNames.Title, new TitleScreen(this));
		screens.put(ScreenNames.Loading, new LoadingScreen(this));
		screens.put(ScreenNames.Game, new GameScreen(this));
		screens.put(ScreenNames.StarMap, new StarMapScreen(this));
		screens.put(ScreenNames.SolarSystem, new SolarSystemScreen(this));
		showScreen(ScreenNames.Loading);

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void dispose() {
		super.dispose();
		skin.dispose();
	}

	public void showScreen(ScreenNames screenName) {
		Screen screen = screens.get(screenName);
		if (screen != null) {
			this.setScreen(screen);
		}
	}

	public void loadAssets() {
		// load game assets here
		loadTexture("data/stars/yellow.png");
		loadTexture("data/stars/white.png");
		loadTexture("data/stars/red.png");
		loadTexture("data/stars/blue.png");
		loadTexture("data/stars/orange.png");
		loadTexture("data/stars/bg-star-blue.png");
		loadTexture("data/stars/bg-star-brown.png");
		loadTexture("data/stars/bg-star-green.png");
		loadTexture("data/stars/bg-star-red.png");
		loadTexture("data/stars/bg-star-yellow.png");
		loadTexture("data/planets/planetsheetBarren.png");
		loadTexture("data/planets/planetsheetEarthLike.png");
		loadTexture("data/planets/planetsheetGas01.png");
		loadTexture("data/planets/planetsheetGas02.png");
		loadTexture("data/planets/planetsheetGas03.png");
	}

	private final Galaxy galaxy = new Galaxy(130, null, new Ship(), 1, null);

	public ArrayList<StarMapStar> getNearbyStars() {
		ArrayList<StarMapStar> stars = new ArrayList<>();
		int starCount = galaxy.getShipLocation().linkedSolarSystems.size();
		double angle = Math.toRadians(360f / starCount);
		Gdx.app.log("GJG", "Count " + String.valueOf(starCount));
		int index = 0;
		for (SolarSystem solarSystem : galaxy.getShipLocation().linkedSolarSystems) {
			double itemAngle = angle * index++;
			stars.add(new StarMapStar(solarSystem, (float) Math.cos(itemAngle) * 0.5f, (float) Math.sin(itemAngle) * 0.75f));
		}
		return stars;
	}

	public StarMapStar getCurrentStar() {
		SolarSystem solarSystem = galaxy.getShipLocation();
		return new StarMapStar(solarSystem, 0f, 0f);
	}

	public void setCurrentSolarSystem(SolarSystem targetSolarSystem) {
		galaxy.setShipLocation(targetSolarSystem);
	}
}
