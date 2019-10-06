package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.rustyrobot.GearTextButton;

import java.util.Random;

import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.IconType;
import fgm.fgj.gamejamgame.Icons;
import fgm.fgj.gamejamgame.ScreenNames;
import fgm.fgj.gamejamgame.Ship;
import fgm.fgj.gamejamgame.screens.shipPanels.CargoPanel;
import fgm.fgj.gamejamgame.screens.shipPanels.CrewPanel;
import fgm.fgj.gamejamgame.screens.shipPanels.ModulesPanel;
import fgm.fgj.gamejamgame.screens.shipPanels.SolarSystemPanel;

public class ShipScreen implements Screen {
	private final GameJamGame game;
	private final Camera camera;
	private final Stage stage;

	private GameDialog worldInfo;

	private Random random = new Random();
	private Viewport viewport;

	private Ship ship;

	Table shipTable = new Table();

	private final CrewPanel crewPanel;
	private final CargoPanel cargoPanel;
	private final ModulesPanel modulesPanel;
	private final SolarSystemPanel solarSystemPanel;

	private final Image centerCog;

	public ShipScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		//stage.setDebugAll(true);

		// Camera Setup
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(w, h, camera);
		camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
		camera.update();
		stage.setViewport(viewport);

		// Ship screen button

		// Info box

		// Planet overview box

		Skin skin = game.getSkin();
		final GearTextButton cancelButton = new GearTextButton("Cancel", skin);
		cancelButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				worldInfo.hide();
			}
		});
		final GearTextButton visitButton = new GearTextButton("Visit", skin);
		visitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.log("STAR_MAP", "Going to a star");
				worldInfo.hide();
			}
		});
		worldInfo = new GameDialog(game, "Solar System Info", visitButton, cancelButton);

		ship = game.getShip();
		shipTable.setBackground(skin.getDrawable("list"));
		shipTable.setFillParent(true);

		solarSystemPanel = new SolarSystemPanel(skin);
		shipTable.add(solarSystemPanel.getRoot()).fill().expand().uniform();

		shipTable.add(new Image(skin.getDrawable("splitpane-horizontal")))
			.expandY().fillY().top();

		modulesPanel = new ModulesPanel(skin);
		shipTable.add(modulesPanel.getRoot()).fill().expand().uniform();

		shipTable.row();

		shipTable.add(new Image(skin.getDrawable("splitpane-vertical")))
			.expandX().fillX().left();

		centerCog = new Image(skin.getDrawable("cog1"));
		centerCog.setOrigin(Align.center);
		shipTable.add(centerCog).pad(4);

		shipTable.add(new Image(skin.getDrawable("splitpane-vertical")))
			.expandX().fillX().right();

		shipTable.row();

		crewPanel = new CrewPanel(skin);
		shipTable.add(crewPanel.getRoot()).fill().expand().uniform();

		shipTable.add(new Image(skin.getDrawable("splitpane-horizontal")))
			.expandY().fillY().bottom();

		cargoPanel = new CargoPanel(skin);
		shipTable.add(cargoPanel.getRoot()).fill().expand().uniform();

		stage.addActor(shipTable);
		starMapButton.setSize(64, 64);
		starMapButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Gdx.app.log("STAR_MAP", "Going to current to ship info");
				game.showScreen(ScreenNames.StarMap);
			}
		});
		stage.addActor(starMapButton);
	}

	Image starMapButton = new Image();

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		crewPanel.displayCrew(ship.listCrewMembers());
		modulesPanel.displayModules(game, ship);
		TextureRegion textureRegion = Icons.getIcon(IconType.STARMAP_BUTTON);
		starMapButton.setDrawable(new TextureRegionDrawable(textureRegion));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		centerCog.setRotation(centerCog.getRotation() + 10.0f * delta);

		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		spriteBatch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		viewport.update(width, height);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
