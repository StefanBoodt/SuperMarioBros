package com.mariobros.sprites.hero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mariobros.screens.LevelScreen;

/**
 * This class represents Mario.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public class Mario extends Sprite {

    /**
     * The world mario lives in.
     */
    public World world;

    /**
     * The powerup mario currently has.
     */
    private PowerUp powerup;

    public Mario(LevelScreen screen) {
        world = screen.getWorld();
        init();
        powerup.define();
    }

    public Mario(LevelScreen screen, float x, float y) {
        world = screen.getWorld();
        init();
        powerup.define(new Vector2(x, y));
    }

    /**
     * Does some initialization.
     */
    private void init() {
        powerup.setWorld(world);
        powerup = PowerUp.NORMAL;
    }

    /**
     * Changes the current power up to the new power up.
     * @param newPowerUp The new power up.
     */
    protected void setPowerup(PowerUp newPowerUp) {
        powerup = newPowerUp;
        powerup.redefine();
    }
}
