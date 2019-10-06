package fgm.fgj.gamejamgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite {
	private Animation<TextureRegion> animation;

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	private float x, y;
	private float scale = 1;
	private int frameWidth;
	private int frameHeight;

	public AnimatedSprite(Texture texture, float x, float y, int frameWidth, int frameHeight, int frameNum, float frameDuration) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		buildAnimation(texture, frameWidth, frameHeight, frameNum, frameDuration);
		this.x = x;
		this.y = y;
	}

	public AnimatedSprite(Texture texture, float x, float y, int frameWidth, int frameHeight, int frameNum, float frameDuration, int startFrame) {
		this(texture, x, y, frameWidth, frameHeight, frameNum, frameDuration);
		elapsedTime += frameDuration * startFrame;
	}

	public void setScale(float scale) {
		this.scale = scale;
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
		spriteBatch.draw(animation.getKeyFrame(elapsedTime, true), x - frameWidth / 2f * scale, y - frameHeight / 2f * scale, frameWidth * scale, frameHeight * scale);
	}

	public float getScale() {
		return scale;
	}
}
