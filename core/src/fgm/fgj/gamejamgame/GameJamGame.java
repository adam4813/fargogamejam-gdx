package fgm.fgj.gamejamgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fgm.fgj.gamejamgame.screens.GameScreen;
import fgm.fgj.gamejamgame.screens.LoadingScreen;
import fgm.fgj.gamejamgame.screens.TitleScreen;

import java.util.HashMap;
import java.util.Map;

public class GameJamGame extends Game {
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	private Skin skin;
	private final Map<ScreenNames, Screen> screens = new HashMap<>();

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
		loadTextureAtlas("data/default/uiskin.atlas");
		assetManager.load("data/default/uiskin.json", Skin.class, new SkinLoader.SkinParameter("data/default/uiskin.atlas"));
		assetManager.finishLoading();
		skin = getAsset("data/default/uiskin.json");
		skin.getFont("default-font").getData().setScale(3, 3);

		// Screens
		screens.put(ScreenNames.Title, new TitleScreen(this));
		screens.put(ScreenNames.Loading, new LoadingScreen(this));
		screens.put(ScreenNames.Game, new GameScreen(this));
		showScreen(ScreenNames.Loading);
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
	}
}
