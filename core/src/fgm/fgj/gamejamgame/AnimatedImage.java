package fgm.fgj.gamejamgame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
	Animation animation;
	private float stateTime = 0;
	private boolean looping = true;

	public AnimatedImage(Animation animation, boolean looping) {
		super((TextureRegion) animation.getKeyFrame(0));
		this.looping = looping;
		this.animation = animation;
	}

	@Override
	public void act(float delta) {
		((TextureRegionDrawable) getDrawable()).setRegion((TextureRegion) animation.getKeyFrame(stateTime += delta, looping));
		super.act(delta);
	}
}
