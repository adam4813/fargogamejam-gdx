package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ray3k.rustyrobot.GearTextButton;

import fgm.fgj.gamejamgame.GameJamGame;

public class GameDialog {
	private Dialog dialog;
	private Image dialogCog;
	Label titleLabel;
	Table bodyTable;
	private Array<com.ray3k.rustyrobot.GearTextButton> gearTextButtons = new Array<GearTextButton>();

	public GameDialog(GameJamGame game, String title, com.ray3k.rustyrobot.GearTextButton... buttons) {
		Skin skin = game.getSkin();

		dialog = new Dialog("", skin, "empty-bg") {
			@Override
			public void act(float delta) {
				super.act(delta);

				if (dialogCog != null) {
					dialogCog.setRotation(dialogCog.getRotation() + 10.0f * delta);
				}
				for (GearTextButton button : gearTextButtons) {
					button.act(delta);
				}
			}

			@Override
			public boolean remove() {
				gearTextButtons.clear();

				return super.remove();
			}

			@Override
			public void hide() {
				super.hide();
				bodyTable.clearChildren();
			}
		};
		Stack stack = new Stack();

		Container cont = new Container();
		cont.fill();
		cont.padLeft(159);
		cont.padTop(123.0f);
		cont.padRight(139);
		cont.padBottom(100);
		Container black = new Container();
		black.setBackground(skin.getDrawable("black"));
		cont.setActor(black);
		stack.add(cont);

		cont = new Container();
		dialogCog = new Image(skin, "cog3");
		dialogCog.setOrigin(Align.center);
		cont.setActor(dialogCog);
		cont.bottom();
		cont.padBottom(128);
		stack.add(cont);

		cont = new Container();
		cont.background(skin.getDrawable("window"));
		stack.add(cont);

		Table table = new Table();
		titleLabel = new Label(title, skin, "bg");
		table.add(titleLabel).top().padTop(30.0f).padLeft(24.0f).colspan(2);

		table.row();

		bodyTable = new Table();
		table.add(bodyTable).padTop(8).colspan(buttons.length).growX().row();
		for (GearTextButton button : buttons) {
			gearTextButtons.add(button);
			table.add(button).center().top().expandY();
		}
		stack.add(table);
		dialog.add(stack);
		dialog.key(Input.Keys.ESCAPE, false);
	}

	public void show(Stage stage, String title, Table body) {
		titleLabel.setText(title);
		bodyTable.add(body).growX();
		dialog.show(stage);
	}

	public void hide() {
		dialog.hide();
		dialog.remove();
	}
}
