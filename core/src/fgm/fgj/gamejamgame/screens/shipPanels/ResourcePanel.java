package fgm.fgj.gamejamgame.screens.shipPanels;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import fgm.fgj.gamejamgame.AnimatedImage;
import fgm.fgj.gamejamgame.AnimatedSprite;
import fgm.fgj.gamejamgame.CargoBay;
import fgm.fgj.gamejamgame.Engine;
import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.IconType;
import fgm.fgj.gamejamgame.Icons;
import fgm.fgj.gamejamgame.Ship;

public class ResourcePanel {
	private Skin skin;

	public Table getRoot() {
		return root;
	}

	Table root = new Table();

	Table fuelTabla = new Table();
	Table waterTable = new Table();
	Table foodTable = new Table();
	Table metalTable = new Table();
	Table gasTable = new Table();
	Table ammoTable = new Table();

	public ResourcePanel(Skin skin) {
		this.skin = skin;
		root.padRight(8).padBottom(8);
		root.add(new Label("Resources", skin, "bg")).expandX().top().left();

		root.row();

		Table resourcesTable = new Table();
		resourcesTable.add(fuelTabla).uniform();
		resourcesTable.add(waterTable).uniform();

		resourcesTable.row();

		resourcesTable.add(foodTable).uniform();
		resourcesTable.add(metalTable).uniform();

		resourcesTable.row();

		resourcesTable.add(gasTable).uniform();
		resourcesTable.add(ammoTable).uniform();
		root.add(resourcesTable).top().left().growY();
	}

	public void displayResources(CargoBay cargoBay) {
		fuelTabla.clearChildren();
		fuelTabla.add(new Image(Icons.getIcon(IconType.RESOURCE_YELLOW))).uniform().expand();
		fuelTabla.add(new Label(new StringBuilder().append("Fuel Level: ")
			.append(cargoBay.checkFuel()).toString(), skin)).uniform().expand();

		foodTable.clearChildren();
		foodTable.add(new Image(Icons.getIcon(IconType.RESOURCE_BROWN))).uniform().expand();
		foodTable.add(new Label(new StringBuilder().append("Food Level: ")
			.append(cargoBay.checkFood()).toString(), skin)).uniform().expand();

		waterTable.clearChildren();
		waterTable.add(new Image(Icons.getIcon(IconType.RESOURCE_BLUE))).uniform().expand();
		waterTable.add(new Label(new StringBuilder().append("Water Level: ")
			.append(cargoBay.checkWater()).toString(), skin)).uniform().expand();

		gasTable.clearChildren();
		gasTable.add(new Image(Icons.getIcon(IconType.RESOURCE_GREEN))).uniform().expand();
		gasTable.add(new Label(new StringBuilder().append("Gas Level: ")
			.append(cargoBay.checkGas()).toString(), skin)).uniform().expand();

		metalTable.clearChildren();
		metalTable.add(new Image(Icons.getIcon(IconType.RESOURCE_GRAY))).uniform().expand();
		metalTable.add(new Label(new StringBuilder().append("Metal Level: ")
			.append(cargoBay.checkMetal()).toString(), skin)).uniform().expand();

		ammoTable.clearChildren();
		ammoTable.add(new Image(Icons.getIcon(IconType.RESOURCE_PURPLE))).uniform().expand();
		ammoTable.add(new Label(new StringBuilder().append("Ammo Level: ")
			.append(cargoBay.checkAmmo()).toString(), skin)).uniform().expand();
	}
}
