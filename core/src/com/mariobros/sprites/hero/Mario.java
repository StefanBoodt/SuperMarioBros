package com.mariobros.sprites.hero;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mariobros.SuperMarioBros;
import com.mariobros.interfaces.Updateable;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.enemies.Enemy;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    public enum State {RUNNING, JUMPING, STANDING, DEAD};

    /**
     * Enum for powerups.
     */
    public enum PowerUp {NORMAL, SUPER};

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

    /**
     * The standing frame.
     */
    private Array<TextureRegion> marioStand;

    /**
     * The run animations. The animations should stay in the same order as the PowerUp enum.
     */
    private Array<Animation<TextureRegion>> marioRun;

    /**
     * The jumping frame.
     */
    private Array<TextureRegion> marioJump;

    /**
     * The region for when mario dies.
     */
    private TextureRegion marioDead;

    /**
     * The body for mario.
     */
    private Body body;

    public Mario(final LevelScreen screen) {
        super();
        setScreen(screen);
        init();
        define();
    }

    public Mario(final LevelScreen screen, final float x, final float y) {
        super();
        setScreen(screen);
        init();
        define(new Vector2(x, y));
    }

    /**
     * Does some initialization.
     */
    private void init() {
        powerup = PowerUp.NORMAL;
        facingRight = true;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        load_animations();
        setBounds(0, 0, 16 / SuperMarioBros.PPM, 16 / SuperMarioBros.PPM);
        setRegion(getStanding());
    }

    /**
     * Loads all the animations and texture regions and puts them into variables.
     */
    private void load_animations() {
        marioStand = new Array<TextureRegion>();
        marioJump = new Array<TextureRegion>();
        marioRun = new Array<Animation<TextureRegion>>();
        load_normal_and_super_animations();
    }

    private void load_normal_and_super_animations() {
        TextureRegion region = screen.getAtlas().findRegion("little_mario");
        marioStand.add(new TextureRegion(region, 32, 0, 16, 16));
        marioJump.add(new TextureRegion(region, 80, 0, 16, 16));
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 2; i <= 3; i++) {
            frames.add(new TextureRegion(region, i * 16, 0, 16, 16));
        }
        marioRun.add(new Animation(0.1f, frames));
        marioDead = new TextureRegion(region, 160, 0, 16, 16);
        frames.clear();
        region = screen.getAtlas().findRegion("super_mario");
        marioStand.add(new TextureRegion(region, 64, 0, 32, 32));
        marioJump.add(new TextureRegion(region, 192, 0, 32, 32));
        for (int i = 2; i < 5; i++) {
            frames.add(new TextureRegion(region, i * 32, 0, 32, 32));
        }
        marioRun.add(new Animation(0.1f, frames));
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
        redefine();
    }

    /**
     * REturn the body of the player.
     * @return The body used.
     */
    public Body getBody() {
        return body;
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
        setPosition(getPosition().x - getWidth() / 2, getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    /**
     * This method states what happens if mario is hit by an enemy.
     * @param enemy The enemy that hit mario.
     */
    public void hit(Enemy enemy) {
        switch (powerup) {
            case NORMAL:
                die();
                break;
            case SUPER:
                setPowerup(PowerUp.NORMAL);
                break;
            default:
                setPowerup(PowerUp.SUPER);
                break;
        }
    }

    /**
     * Define marios figure in the boxworld.
     */
    protected void define() {
        Vector2 position = new Vector2(40.0f / SuperMarioBros.PPM, 60.0f / SuperMarioBros.PPM);
        define(position);
    }

    /**
     * Define marios figure in the boxworld.
     * @param position The starting position.
     */
    protected void define(Vector2 position) {
        switch (powerup) {
            case NORMAL:
                BodyDef bdef = new BodyDef();
                bdef.position.set(position);
                bdef.type = BodyDef.BodyType.DynamicBody;
                this.body = screen.getWorld().createBody(bdef);
                FixtureDef fdef = new FixtureDef();
                CircleShape shape = new CircleShape();
                shape.setRadius(7 / SuperMarioBros.PPM);
                setFilters(fdef);
                fdef.shape = shape;
                this.body.createFixture(fdef).setUserData(this);

                EdgeShape head = new EdgeShape();
                head.set(new Vector2(-2 / SuperMarioBros.PPM, 7 / SuperMarioBros.PPM),
                        new Vector2(2 / SuperMarioBros.PPM, 7 / SuperMarioBros.PPM));
                fdef.isSensor = true;
                fdef.shape = head;
                fdef.filter.categoryBits = SuperMarioBros.MARIO_HEAD_BIT;
                body.createFixture(fdef).setUserData(this);
                break;
        }
    }

    /**
     * Redefines marios figure in the boxworld.
     */
    protected void redefine() {
        Vector2 position = getPosition();
        screen.getWorld().destroyBody(body);
        define(position);
    }

    /**
     * Makes mario jump.
     */
    public void jump() {
        body.applyLinearImpulse(new Vector2(0.0f, Mario.SPEED_UP_Y), body.getWorldCenter(), true);
    }

    /**
     * Gets the state the player should be in.
     * @return The correct state.
     */
    protected State getState() {
        if (currentState == State.DEAD) {
            return State.DEAD;
        } else if (body.getLinearVelocity().y > Mario.SPEED_UP_Y / 10 || (previousState  == State.JUMPING && body.getLinearVelocity().y < 0)) {
            return State.JUMPING;
        } else if (body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    /**
     * Makes the player die.
     */
    public void die() {
        setState(State.DEAD);
        if (previousState != State.DEAD) {
            Filter filter = new Filter();
            filter.maskBits = SuperMarioBros.NOTHING_BIT;
            for (Fixture fixture: body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
        }
    }

    /**
     * Gets the frame to draw.
     * @param dt The delta time.
     * @return The frame of mario right now.
     */
    public TextureRegion getFrame(float dt) {
        setState(getState());
        TextureRegion frame = null;
        switch (currentState) {
            case STANDING:
                frame = getStanding();
                break;
            case JUMPING:
                frame = getJumping();
                break;
            case RUNNING:
                frame = getWalking().getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                frame = marioDead;
                break;
        }

        if((body.getLinearVelocity().x < 0 || !facingRight) && !frame.isFlipX()){
            frame.flip(true, false);
            facingRight = false;
        }
        else if((body.getLinearVelocity().x > 0 || facingRight) && frame.isFlipX()){
            frame.flip(true, false);
            facingRight = true;
        }
        stateTimer += dt;
        previousState = currentState;
        return frame;
    }

    /**
     * Returns the position.
     * @return The position.
     */
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    /**
     * Retrurns the standing region.
     * @return The standing frame.
     */
    public TextureRegion getStanding() {
        TextureRegion standing = null;
        switch (powerup) {
            case NORMAL:
                standing = marioStand.get(0);
                break;
            case SUPER:
                standing = marioStand.get(1);
                break;
            default:
                throw new IllegalArgumentException("Mario powerup is " + powerup);
        }
        return standing;
    }

    /**
     * Return the walking animation.
     * @return The walking animation.
     */
    public Animation<TextureRegion> getWalking() {
        Animation<TextureRegion> walking = null;
        switch (powerup) {
            case NORMAL:
                walking = marioRun.get(0);
                break;
            case SUPER:
                walking = marioRun.get(1);
                break;
            default:
                throw new IllegalArgumentException("Mario powerup is " + powerup);
        }
        return walking;
    }

    /**
     * Returns the jumping texture.
     * @return The jumping texture.
     */
    public TextureRegion getJumping() {
        TextureRegion jumping = null;
        switch (powerup) {
            case NORMAL:
                jumping = marioJump.get(0);
                break;
            case SUPER:
                jumping = marioJump.get(1);
                break;
        }
        return jumping;
    }

    /**
     * Sets the state to the new state.
     * @param state The state to set to.
     */
    protected void setState(State state) {
        if (!currentState.equals(state)) {
            currentState = state;
            stateTimer = 0;
        }
    }

    /**
     * Sets the filters for mario.
     * @param fdef The fixturedefinition of mario.
     */
    protected void setFilters(FixtureDef fdef) {
        fdef.filter.categoryBits = SuperMarioBros.MARIO_BIT;
        fdef.filter.maskBits = SuperMarioBros.GROUND_BIT | SuperMarioBros.OBJECT_BIT | SuperMarioBros.ENEMY_BIT
                | SuperMarioBros.BLOCK_BIT | SuperMarioBros.ENEMY_WEAKNESS_BIT;
    }

    /**
     * Return the entire region for the current powerup.
     * @return The texture region for the powerup.
     */
    protected TextureRegion getRegion() {
        String regionname = "";
        switch (powerup) {
            case NORMAL:
                regionname = "little_mario";
                break;
            case SUPER:
                regionname = "super_mario";
                break;
        }
        return screen.getAtlas().findRegion(regionname);
    }

    /**
     * Returns the powerup mario currently has.
     */
    public PowerUp getPowerUp() {
        return powerup;
    }
}
