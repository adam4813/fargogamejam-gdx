package fgm.fgj.gamejamgame.screens.shipPanels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CargoPanel {
	public Table getRoot() {
		return root;
	}

	Table root = new Table();

	public CargoPanel(Skin skin) {
		root.padRight(8).padBottom(8);
		root.add(new Label("Cargo Hold", skin, "bg")).expand().top().left();
	}
}
