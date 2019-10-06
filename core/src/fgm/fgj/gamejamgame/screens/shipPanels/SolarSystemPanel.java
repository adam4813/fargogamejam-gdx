package fgm.fgj.gamejamgame.screens.shipPanels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SolarSystemPanel {
	public Table getRoot() {
		return root;
	}

	Table root = new Table();

	public SolarSystemPanel(Skin skin) {
		root.padLeft(8).padTop(8);
		root.add(new Label("Current Solar System", skin, "bg")).expand().top().left();
	}
}
