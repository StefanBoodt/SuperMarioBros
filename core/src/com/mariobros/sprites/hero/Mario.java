package com.mariobros.sprites.hero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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

    /**
     * The maximal speed of Mario.
     */
    protected static final float MAX_SPEED = 2.0f;

    /**
     * The difference in speed when the player wants to move to the right.
     */
    public static final float SPEED_UP_X = 0.1f;

    /**
     * The difference in speed to move up.
     */
    public static final float SPEED_UP_Y = 4.3f;

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

    /**
     * Makes mario jump.
     */
    public void jump() {
        powerup.jump();
    }

    /**
     * REturn the body of the player.
     * @return The body used.
     */
    public Body getBody() {
        return powerup.body;
    }

    /**
     * Checks if the player can still speed up to the left.
     * @return true if the player can get faster to the left.
     */
    public boolean canSpeedUpLeft() {
        return getBody().getLinearVelocity().x >= -MAX_SPEED;
    }

    /**
     * Checks if the player can still speed up to the right.
     * @return true if the player can get faster to the right.
     */
    public boolean canSpeedUpRight() {
        return getBody().getLinearVelocity().x <= MAX_SPEED;
    }
}
