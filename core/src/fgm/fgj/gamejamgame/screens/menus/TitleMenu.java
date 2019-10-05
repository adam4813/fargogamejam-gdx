package fgm.fgj.gamejamgame.screens.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.ScreenNames;

public class TitleMenu {
	private final Table table = new Table();

	public TitleMenu(GameJamGame game) {
		table.setFillParent(true);
		table.align(Align.center);

		TextButton startButton = new TextButton("New Game", game.getSkin());
		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Game);
			}
		});

		TextButton quitButton = new TextButton("Quit Game", game.getSkin());
		quitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				GameJamGame.quit();
			}
		});

		table.add(startButton).padBottom(20).row();
		table.add(quitButton);
	}

	public Table getTable() {
		return table;
	}
}
