package com.mariobros.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariobros.SuperMarioBros;
import com.mariobros.interfaces.Updateable;
import com.mariobros.scenes.HUD;
import com.mariobros.sprites.hero.Mario;
import com.mariobros.tools.Box2DWorldCreator;
import com.mariobros.tools.WorldCollisionListener;

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
     * General debug boolean.
     */
    public static final boolean debug = true;

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

    /**
     * The creator of the box2D debug lines.
     */
    private Box2DDebugRenderer debugRenderer;

    /**
     * Creates the box2D environment.
     */
    private Box2DWorldCreator creator;

    /**
     * The player.
     */
    private Mario player;

    /**
     * The texture atlas storing the mario sprites.
     */
    private TextureAtlas atlas;

    /**
     * Creates a new levelscreen.
     * @param game The game that is currently being played.
     */
    public LevelScreen(SuperMarioBros game) {
        this.game = game;
        atlas = new TextureAtlas("mario-sheet-32x32.txt");
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(SuperMarioBros.V_WIDTH / SuperMarioBros.PPM, SuperMarioBros.V_HEIGHT / SuperMarioBros.PPM, gamecam);
        hud = new HUD(game.batch, "1-1", 300);
        maploader = new TmxMapLoader();
        map = maploader.load("testworld.tmx");
        //map = maploader.load("Graphics-tests.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / SuperMarioBros.PPM, game.batch);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        creator = new Box2DWorldCreator(this);
        player = new Mario(this, 40.0f / SuperMarioBros.PPM, 40.0f / SuperMarioBros.PPM);
        world.setContactListener(new WorldCollisionListener());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        if (debug) {
            debugRenderer.render(world, gamecam.combined);
        }
        drawMario();
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    /**
     * Draws Mario to the screen.
     */
    private void drawMario() {
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
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
        map.dispose();
        hud.dispose();
        world.dispose();
        debugRenderer.dispose();
    }

    /**
     * Handles input.
     * @param dt The delta time.
     */
    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.jump();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            gamecam.position.y -= 100 * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.canSpeedUpRight()) {
            player.getBody().applyLinearImpulse(new Vector2(Mario.SPEED_UP_X, 0.0f), player.getBody().getWorldCenter(), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.canSpeedUpLeft()) {
            player.getBody().applyLinearImpulse(new Vector2(-Mario.SPEED_UP_X, 0.0f), player.getBody().getWorldCenter(), true);
        }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        hud.update(dt);
        world.step(1 / 60f, 6, 2);
        player.update(dt);
        gamecam.position.x = player.getBody().getPosition().x;
        gamecam.update();
        renderer.setView(gamecam);
    }

    /**
     * Get the world of this screen (for Box2D).
     * @return the world.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Return the map that is being used.
     * @return the tilemap in usage for this level.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Return the Hud.
     * @return the hud.
     */
    public HUD getHud() {
        return hud;
    }

    /**
     * Returns the atlas.
     * @return The atlas for the mario sprites.
     */
    public TextureAtlas getAtlas() {
        return atlas;
    }
}
