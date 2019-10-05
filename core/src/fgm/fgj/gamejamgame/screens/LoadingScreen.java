package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.ScreenNames;

public class LoadingScreen extends ScreenAdapter {
	private final SpriteBatch batch;
	private final BitmapFont font;
	private final GlyphLayout glyphLayout = new GlyphLayout();
	private final CharSequence loadingString = "Assets Loading";
	private GameJamGame game;

	public LoadingScreen(GameJamGame game) {
		this.game = game;
		game.loadAssets();
		batch = new SpriteBatch();
		font = game.getSkin().getFont("default-font");
		glyphLayout.setText(font, loadingString);
	}

	@Override
	public void render(float delta) {
		if (game.assetsLoaded()) {
			game.showScreen(ScreenNames.Title);
			return;
		}
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		font.draw(batch, loadingString, Gdx.graphics.getWidth() / 2f - glyphLayout.width / 2, Gdx.graphics.getHeight() / 2f + glyphLayout.height / 2);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
