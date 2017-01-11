package com.mariobros.sprites.hero;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mariobros.SuperMarioBros;
import com.mariobros.interfaces.Updateable;
import com.mariobros.screens.LevelScreen;

/**
 * This class represents Mario.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public class Mario extends Sprite implements Updateable {

    /**
     * The world mario lives in.
     */
    public World world;

    /**
     * Enum for different states.
     */
    public enum State { RUNNING, JUMPING, STANDING, DEAD, FALLING};

    /**
     * The current state.
     */
    private State currentState;

    /**
     * The previous state.
     */
    private State previousState;

    /**
     * The screen of the level.
     */
    private LevelScreen screen;

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

    /**
     * true if mario is facing right. Used to keep track of the direction.
     */
    private boolean facingRight;

    /**
     * The timer on the animations.
     */
    private float stateTimer;

    public Mario(final LevelScreen screen) {
        super();
        setScreen(screen);
        init();
        powerup.define();
    }

    public Mario(final LevelScreen screen, final float x, final float y) {
        super();
        setScreen(screen);
        init();
        powerup.define(new Vector2(x, y));
    }

    /**
     * Does some initialization.
     */
    private void init() {
        powerup.setScreen(screen);
        powerup = PowerUp.NORMAL;
        setRegion(powerup.getRegion());
        setBounds(0, 0, 16 / SuperMarioBros.PPM, 16 / SuperMarioBros.PPM);
        facingRight = true;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
    }

    /**
     * Sets the screen for the mario.
     * @param screen The new screen.
     */
    public void setScreen(final LevelScreen screen) {
        this.screen = screen;
        world = screen.getWorld();
    }

    /**
     * Changes the current power up to the new power up.
     * @param newPowerUp The new power up.
     */
    protected void setPowerup(PowerUp newPowerUp) {
        powerup = newPowerUp;
        powerup.redefine();
        setRegion(powerup.getRegion());
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

    @Override
    public void update(float dt) {
        powerup.update(dt);
        setPosition(powerup.getPosition().x - getWidth() / 2, powerup.getPosition().y - getHeight() / 2);
    }

    /**
     * Selects the current region of the spritesheet and sets it.
     */
    private void getRegion() {
        switch(currentState) {
            case STANDING:
                setRegion(powerup.getStanding());
                break;
            case JUMPING:
                setRegion(powerup.getJumping());
                break;
            case RUNNING:
                setRegion(powerup.getWalking());
                break;
            case DEAD:
                //setRegion();
                break;
            default:
                break;
        }
    }
}
