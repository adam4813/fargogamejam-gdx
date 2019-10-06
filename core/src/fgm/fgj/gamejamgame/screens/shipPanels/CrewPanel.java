package fgm.fgj.gamejamgame.screens.shipPanels;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.List;

import fgm.fgj.gamejamgame.CrewMember;
import fgm.fgj.gamejamgame.Species;

public class CrewPanel {
	private Skin skin;

	public Table getRoot() {
		return root;
	}

	Table root = new Table();
	Table crewTable = new Table();

	public CrewPanel(Skin skin) {
		this.skin = skin;
		root.padLeft(8).padBottom(8);
		root.add(new Label("Crew List", skin, "bg")).expandX().top().left();
		root.row();

		root.add(crewTable).top().left().grow();
	}

	private void addRow(CrewMember crewMember) {
		Species species = crewMember.getSpecies();
		Stack imageStack = new Stack();
		crewTable.add(imageStack).uniform().expand();
		crewTable.add(new Label(crewMember.getName(), skin)).uniform().expand();
		crewTable.add(new Label(species.getName(), skin)).uniform().expand();
		crewTable.add(new Label(String.valueOf(crewMember.getHP()), skin)).uniform().expand();
		crewTable.row();
	}

	public void displayCrew(List<CrewMember> crew) {
		crewTable.clearChildren();

		crewTable.add(new Container()).uniform().expandX().padBottom(4);
		crewTable.add(new Label("Name", skin)).uniform().expandX().padBottom(4);
		crewTable.add(new Label("Species", skin)).uniform().expandX().padBottom(4);
		crewTable.add(new Label("HP", skin)).uniform().expandX().padBottom(4);
		crewTable.row();
		for (CrewMember crewMember : crew) {
			addRow(crewMember);
		}
		//crewTable.add(new Container()).growY();
	}
}
