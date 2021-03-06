package fgm.fgj.gamejamgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;
import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_WIDTH;

public class StarField {
	private static Random random = new Random();

	private final ArrayList<AnimatedSprite> animatedStars = new ArrayList<>();

	public StarField(int starCount) {
		for (int i = 0; i < starCount; i++) {
			buildAnimatedBGStar(starTextures.get(StarType.BG_YELLOW));
			buildAnimatedBGStar(starTextures.get(StarType.BG_BROWN));
			buildAnimatedBGStar(starTextures.get(StarType.BG_BLUE));
			buildAnimatedBGStar(starTextures.get(StarType.BG_GREEN));
			buildAnimatedBGStar(starTextures.get(StarType.BG_RED));
		}
	}

	public static final Map<StarType, Texture> starTextures = new EnumMap<StarType, Texture>(StarType.class);

	public static void initStarTextures(GameJamGame game) {
		starTextures.put(StarType.YELLOW, game.getAsset("data/stars/yellow.png"));
		starTextures.put(StarType.WHITE, game.getAsset("data/stars/white.png"));
		starTextures.put(StarType.BLUE, game.getAsset("data/stars/blue.png"));
		starTextures.put(StarType.ORANGE, game.getAsset("data/stars/orange.png"));
		starTextures.put(StarType.RED, game.getAsset("data/stars/red.png"));
		starTextures.put(StarType.BG_YELLOW, game.getAsset("data/stars/bg-star-yellow.png"));
		starTextures.put(StarType.BG_BROWN, game.getAsset("data/stars/bg-star-brown.png"));
		starTextures.put(StarType.BG_BLUE, game.getAsset("data/stars/bg-star-blue.png"));
		starTextures.put(StarType.BG_GREEN, game.getAsset("data/stars/bg-star-green.png"));
		starTextures.put(StarType.BG_RED, game.getAsset("data/stars/bg-star-red.png"));
	}

	public StarField() {
	}

	public StarField(List<StarMapStar> stars) {
		for (StarMapStar star : stars) {
			animatedStars.add(buildAnimatedStar(starTextures.get(star.solarSystem.starType), star.x, star.y, star.solarSystem.starSize));
		}
	}

	public AnimatedSprite addStar(StarMapStar star) {
		AnimatedSprite animatedSprite = buildAnimatedStar(starTextures.get(star.solarSystem.starType), star.x, star.y, star.solarSystem.starSize);
		animatedStars.add(animatedSprite);
		return animatedSprite;
	}

	static int STAR_SPRITE_FRAMES = 15;
	public static int STAR_SPRITE_FRAME_SIZE = 64;

	public static AnimatedSprite buildAnimatedStar(Texture texture, float x, float y, float size) {
		AnimatedSprite animatedSprite = new AnimatedSprite(texture,
			x * SCREEN_WIDTH, y * SCREEN_HEIGHT, STAR_SPRITE_FRAME_SIZE, STAR_SPRITE_FRAME_SIZE, STAR_SPRITE_FRAMES, .1f, random.nextInt(STAR_SPRITE_FRAMES));
		animatedSprite.setScale(Math.max(size, 1));
		return animatedSprite;
	}

	public static int BG_STAR_SPRITE_FRAMES = 5;

	private void buildAnimatedBGStar(Texture texture) {
		int weightTowardSmall = (int) random.nextInt(10);
		float max = 0.25f;
		if (weightTowardSmall >= 1) {
			max = 0.15f;
		}
		animatedStars.add(new AnimatedSprite(texture,
			random.nextFloat() * SCREEN_WIDTH, random.nextFloat() * SCREEN_HEIGHT, STAR_SPRITE_FRAME_SIZE, STAR_SPRITE_FRAME_SIZE, BG_STAR_SPRITE_FRAMES, .1f, random.nextInt(BG_STAR_SPRITE_FRAMES)));
		animatedStars.get(animatedStars.size() - 1).setScale(Math.max(random.nextFloat() * max, 0.05f));
	}

	public void draw(SpriteBatch spriteBatch, float delta) {
		for (AnimatedSprite animatedStar : animatedStars) {
			animatedStar.draw(spriteBatch, delta);
		}
	}

	public ArrayList<AnimatedSprite> getStars() {
		return animatedStars;
	}
}
