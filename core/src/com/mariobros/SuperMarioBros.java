package com.mariobros;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mariobros.interfaces.Updateable;
import com.mariobros.screens.LevelScreen;

/**
 * The Super Mario Brothers game.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public class SuperMarioBros extends Game {

	/**
	 * The spritebatch for the game.
	 */
	public SpriteBatch batch;

	/**
	 * The asset manager for the game.
	 */
	public AssetManager manager;

	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	/**
	 * Virtual width of the game.
	 */
	public static final int V_WIDTH = 400;

	/**
	 * Virtual height of the game.
	 */
	public static final int V_HEIGHT = 208;

	/**
	 * Scale for the game.
	 */
	public static final float PPM = 100f;

	/**
	 * The bit that states that nothing collides.
	 */
	public static final short NOTHING_BIT = 1;

	/**
	 * The bit that identifies the ground.
	 */
	public static final short GROUND_BIT = 2;

	/**
	 * The bit that identifies mario.
	 */
	public static final short MARIO_BIT = 4;

	/**
	 * The bit that identifies objects (pipes, cannons, ... etc.).
	 */
	public static final short OBJECT_BIT = 8;

	/**
	 * The bit that identifies enemies.
	 */
	public static final short ENEMY_BIT = 16;

	/**
	 * The bit that identifies items.
	 */
	public static final short ITEM_BIT = 32;

	/**
	 * The bit that defines blocks.
	 */
	public static final short BLOCK_BIT = 64;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.finishLoading();
		setScreen(new LevelScreen(this));
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}
}
