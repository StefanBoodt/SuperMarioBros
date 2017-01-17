package com.mariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mariobros.interfaces.Updateable;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.hero.Mario;

/**
 * Class to overlook all enemies. A typical mario enemy is the goomba or the turtle. This
 * class provides code that all enemies have in common.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */

public abstract class Enemy extends Sprite implements Updateable {
	
	/**
	 * Screen to use.
	 */
	protected LevelScreen screen;

    public Enemy(LevelScreen screen, float x, float y) {
    	this.screen = screen;
    	setPosition(x,y);
    }

    /**
     * Define the enemy so it can be used in the world.
     */
    public abstract void defineEnemy();

    /**
     * Code for what happens if the enemy is stomped.
     * @param mario The mario that stomped this enemy.
     */
    public abstract void onHit(Mario mario);

}
