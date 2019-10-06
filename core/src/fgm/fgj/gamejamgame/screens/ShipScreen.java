package fgm.fgj.gamejamgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.rustyrobot.GearTextButton;

import java.util.Random;

import fgm.fgj.gamejamgame.GameJamGame;
import fgm.fgj.gamejamgame.Ship;
import fgm.fgj.gamejamgame.SolarSystem;

import static fgm.fgj.gamejamgame.GameJamGame.SCREEN_HEIGHT;

public class ShipScreen implements Screen {
	private final GameJamGame game;
	private final Camera camera;
	private final Stage stage;

	private GameDialog worldInfo;

	private Random random = new Random();
	private Viewport viewport;

	private Ship ship;

	Table shipTable = new Table();
	Table solarSystemTable = new Table();
	Table crewTable = new Table();
	Table equipmentTable = new Table();
	Table cargoTable = new Table();

	public ShipScreen(GameJamGame game) {
		this.game = game;
		stage = new Stage();
		//stage.setDebugAll(true);

		// Camera Setup
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		viewport = new FitViewport(w, h, camera);
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
		shipTable.pad(32f);
		shipTable.setFillParent(true);

		shipTable.add(solarSystemTable).fill().expand().uniform();
		solarSystemTable.pad(32f).padTop(0);
		solarSystemTable.add(new Label("Current Solar System", skin)).expand().top().left();

		shipTable.add(equipmentTable).fill().expand().uniform();
		equipmentTable.pad(32f).padTop(0);
		equipmentTable.add(new Label("Equipped Modules", skin)).expand().top().left();

		shipTable.row();

		shipTable.add(crewTable).fill().expand().uniform();
		crewTable.pad(32f).padTop(0);
		crewTable.add(new Label("Crew Roster", skin)).expand().top().left();

		shipTable.add(cargoTable).fill().expand().uniform();
		cargoTable.pad(32f).padTop(0);
		cargoTable.add(new Label("Cargo Hold", skin)).expand().top().left();

		stage.addActor(shipTable);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		SpriteBatch spriteBatch = game.getSpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();

		spriteBatch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().setScreenSize(width, height);
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
