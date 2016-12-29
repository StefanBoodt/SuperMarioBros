package com.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariobros.SuperMarioBros;
import com.mariobros.interfaces.Updateable;
import com.mariobros.scenes.HUD;

/**
 * The Screen that paints the level.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */

public class LevelScreen implements Screen, Updateable {

    /**
     * The game that is being played.
     */
    private SuperMarioBros game;

    /**
     * The viewport for the game.
     */
    private Viewport gamePort;

    /**
     * The camera used for the game.
     */
    private OrthographicCamera gamecam;

    /**
     * The loader for the map.
     */
    private TmxMapLoader maploader;

    /**
     * The map used in the level.
     */
    private TiledMap map;

    /**
     * The renderer of the map.
     */
    private OrthogonalTiledMapRenderer renderer;

    /**
     * The world that is being played in.
     */
    private World world;

    /**
     * The hud.
     */
    private HUD hud;

    public LevelScreen(SuperMarioBros game) {
        this.game = game;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(SuperMarioBros.V_WIDTH / SuperMarioBros.PPM, SuperMarioBros.V_HEIGHT / SuperMarioBros.PPM);
        hud = new HUD(game.batch, "1-1", 300);
        maploader = new TmxMapLoader();
        //map = maploader.load("testworld.tmx");
        map = maploader.load("Graphics-tests.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / SuperMarioBros.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        renderer.dispose();
        hud.dispose();
        world.dispose();
    }

    /**
     * Handles input.
     * @param dt The delta time.
     */
    public void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            gamecam.position.x += 100 * dt;
            System.out.println("key touched.");
        } else if (Gdx.input.isTouched()) {
            gamecam.position.x += 100 * dt;
            System.out.println("Screen touched.");
        }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        hud.update(dt);
        world.step(1 / 60f, 6, 2);
        gamecam.update();
        renderer.setView(gamecam);
    }
}
