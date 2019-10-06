package fgm.fgj.gamejamgame.screens.menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.IconType;
import fgm.fgj.gamejamgame.Icons;
import fgm.fgj.gamejamgame.ScreenNames;

public class TitleMenu {
	private final Table table = new Table();

	public TitleMenu(GameJamGame game) {
		table.setFillParent(true);
		table.padTop(64);
		//table.align(Align.center);

		TextButton startButton = new TextButton("New Game", game.getSkin());
		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Game);
			}
		});

		TextButton starMapButton = new TextButton("Star Map", game.getSkin());
		starMapButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.StarMap);
			}
		});

		TextButton solarSystemButton = new TextButton("Solar System Map", game.getSkin());
		solarSystemButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.SolarSystem);
			}
		});

		TextButton shipButton = new TextButton("Ship", game.getSkin());
		shipButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Ship);
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

		TextButton loseButton = new TextButton("Quit Game", game.getSkin());
		loseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Win);

			}
		});

		//table.add(loseButton).padBottom(20).row();
		//table.add(startButton).padBottom(20).row();
		//table.add(starMapButton).padBottom(20).row();
		//table.add(solarSystemButton).padBottom(20).row();
		//table.add(shipButton).padBottom(20).row();
		//table.add(quitButton);

		Label titleLabel = new Label("Out Of Steam", game.getSkin(), "title");
		table.add(titleLabel).padBottom(64).center().colspan(2).row();

		Image playButton = new Image(Icons.getIcon(IconType.PLAY_BUTTON));
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Intro);
			}
		});
		playButton.getDrawable().setMinHeight(128);
		playButton.getDrawable().setMinWidth(128);

		Image exitButton = new Image(Icons.getIcon(IconType.EXIT_BUTTON));
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				game.showScreen(ScreenNames.Credits);
			}
		});
		exitButton.getDrawable().setMinHeight(128);
		exitButton.getDrawable().setMinWidth(128);

		table.add(playButton).right().pad(32).top();
		table.add(exitButton).left().pad(32).top();
		table.add(new Container());
	}

	public Table getTable() {
		return table;
	}
}
