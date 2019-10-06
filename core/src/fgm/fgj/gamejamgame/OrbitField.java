package fgm.fgj.gamejamgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;

public class OrbitField {
	private static final float PLANET_SIZE = 1.5f;
	private static Random random = new Random();

	private final ArrayList<AnimatedSprite> animatedPlanets = new ArrayList<>();

	public static final Map<PlanetType, Texture> planetTextures = new EnumMap<PlanetType, Texture>(PlanetType.class);

	private List<Float> orbitLines = new ArrayList<>();

	public static void initPlanetTextures(GameJamGame game) {
		planetTextures.put(PlanetType.BARREN, game.getAsset("data/planets/planetsheetBarren.png"));
		planetTextures.put(PlanetType.EARTH_LIKE, game.getAsset("data/planets/planetsheetEarthLike.png"));
		planetTextures.put(PlanetType.GAS1, game.getAsset("data/planets/planetsheetGas01.png"));
		planetTextures.put(PlanetType.GAS2, game.getAsset("data/planets/planetsheetGas02.png"));
		planetTextures.put(PlanetType.GAS3, game.getAsset("data/planets/planetsheetGas03.png"));
	}

	public AnimatedSprite addPlanet(Planet planet, float orbit) {
		AnimatedSprite animatedSprite = buildAnimatedStar(planetTextures.get(planet.planetType), orbit, .5f, PLANET_SIZE);
		animatedPlanets.add(animatedSprite);
		this.orbitLines.add(new Float(orbit));
		return animatedSprite;
	}

	public static int PLANET_SPRITE_FRAME_SIZE = 64;

	public static AnimatedSprite buildAnimatedStar(Texture texture, float x, float y, float size) {
		int frames = texture.getWidth() / PLANET_SPRITE_FRAME_SIZE;
		AnimatedSprite animatedSprite = new AnimatedSprite(texture,
			x * SCREEN_WIDTH, y * SCREEN_HEIGHT, PLANET_SPRITE_FRAME_SIZE, PLANET_SPRITE_FRAME_SIZE, frames, .1f, random.nextInt(frames));
		animatedSprite.setScale(Math.max(size, 1));
		return animatedSprite;
	}

	public void drawPlanets(SpriteBatch spriteBatch, float delta) {
		for (AnimatedSprite animatedStar : animatedPlanets) {
			animatedStar.draw(spriteBatch, delta);
		}
	}

	public void drawOrbits(ShapeRenderer shapeRenderer) {
		for (Float orbit: orbitLines) {
			shapeRenderer.circle(-SCREEN_WIDTH / 2f, .5f * SCREEN_HEIGHT, orbit * SCREEN_WIDTH + SCREEN_WIDTH / 2f);
		}
	}

	public ArrayList<AnimatedSprite> getPLanets() {
		return animatedPlanets;
	}
}
