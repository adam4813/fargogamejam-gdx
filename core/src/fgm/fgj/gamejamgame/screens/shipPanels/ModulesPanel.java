package fgm.fgj.gamejamgame.screens.shipPanels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ModulesPanel {
	public Table getRoot() {
		return root;
	}

	Table root = new Table();

	public ModulesPanel(Skin skin) {
		root.padRight(8).padTop(8);
		root.add(new Label("Equipped Modules", skin, "bg")).expand().top().left();
	}
}
