package fgm.fgj.gamejamgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite {
	private Animation<TextureRegion> animation;
	private float x, y;
	public AnimatedSprite(Texture texture, float x, float y, int frameWidth, int frameHeight, int frameNum, float frameDuration) {
		buildAnimation(texture, frameWidth, frameHeight, frameNum, frameDuration);
		this.x = x;
		this.y = y;
	}

	private float elapsedTime;

	private void buildAnimation(Texture texture, int frameWidth, int frameHeight, int frameNum, float frameDuration) {

		TextureRegion[][] tmpFrames = TextureRegion.split(texture, frameWidth, frameHeight);
		TextureRegion[] animationFrames = new TextureRegion[frameNum];
		int index = 0;

		for (int i = 0; i < frameNum; i++) {
			animationFrames[index++] = tmpFrames[0][i];
		}

		animation = new Animation(frameDuration, animationFrames);
	}

	public void draw(SpriteBatch spriteBatch, float delta) {
		elapsedTime += delta;
		spriteBatch.draw(animation.getKeyFrame(elapsedTime, true), x, y);
	}
}
