package com.mariobros.sprites.hero;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mariobros.SuperMarioBros;
import com.mariobros.interfaces.Updateable;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.enemies.Enemy;

/**
 * Class to monitor the heros state.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public enum PowerUp implements Updateable {

    NORMAL() {
        @Override
        public void hit(Enemy enemy) {
            System.out.println("Mario is hit.");
        }

        @Override
        public void define() {
            Vector2 position = new Vector2(40.0f / SuperMarioBros.PPM, 60.0f / SuperMarioBros.PPM);
            define(position);
        }

        @Override
        public void define(Vector2 position) {
            BodyDef bdef = new BodyDef();
            bdef.position.set(position);
            bdef.type = BodyDef.BodyType.DynamicBody;
            this.body = screen.getWorld().createBody(bdef);
            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(7 / SuperMarioBros.PPM);
            setFilters(fdef);
            fdef.shape = shape;
            this.body.createFixture(fdef);

            EdgeShape head = new EdgeShape();
            head.set(new Vector2(-2 / SuperMarioBros.PPM, 7 / SuperMarioBros.PPM),
                    new Vector2(2 / SuperMarioBros.PPM, 7 / SuperMarioBros.PPM));
            fdef.isSensor = true;
            fdef.shape = head;
            body.createFixture(fdef).setUserData(this);
        }

        @Override
        public void redefine() {
            Vector2 position = getPosition();
            screen.getWorld().destroyBody(body);
            define(position);
        }

        @Override
        public TextureRegion getRegion() {
            return screen.getAtlas().findRegion("little_mario");
        }

        @Override
        public TextureRegion getStanding() {
            System.out.println("(" + getRegion().getRegionX() + ", " + getRegion().getRegionY() + ")");
            return new TextureRegion(getRegion().getTexture(), 0, 0, 16, 16);
        }

        @Override
        public TextureRegion getWalking(){
            return null;
        }

        @Override
        public TextureRegion getJumping() {
            return null;
        }

        @Override
        public void update(float dt) {

        }
    };//,

    //SUPER() {
//
 //   };

    /**
     * Marios body.
     */
    protected Body body;

    /**
     * The Screen used.
     */
    protected static LevelScreen screen;

    private PowerUp() {

    }

    /**
     * This method states what happens if mario is hit by an enemy.
     * @param enemy The enemy that hit mario.
     */
    public abstract void hit(Enemy enemy);

    /**
     * Returns the position.
     * @return The position.
     */
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    /**
     * Define marios figure in the boxworld.
     */
    public abstract void define();

    /**
     * Define marios figure in the boxworld.
     * @param position The starting position.
     */
    public abstract void define(Vector2 position);

    /**
     * Redefines marios figure in the boxworld.
     */
    public abstract void redefine();

    /**
     * Sets the screen to be affected by.
     * @param screen The screen to live in.
     */
    public static void setScreen(LevelScreen screen) {
        PowerUp.screen = screen;
    }

    /**
     * Jump the mario in the current state. It gives a default implementation for the jump.
     */
    public void jump() {
        body.applyLinearImpulse(new Vector2(0.0f, Mario.SPEED_UP_Y), body.getWorldCenter(), true);
    }

    protected void setFilters(FixtureDef fdef) {
        fdef.filter.categoryBits = SuperMarioBros.MARIO_BIT;
        fdef.filter.maskBits = SuperMarioBros.GROUND_BIT | SuperMarioBros.OBJECT_BIT | SuperMarioBros.ENEMY_BIT
                | SuperMarioBros.BLOCK_BIT;
    }

    /**
     *
     * @return
     */
    public abstract TextureRegion getStanding();

    /**
     *
     * @return
     */
    public abstract TextureRegion getWalking();

    /**
     *
     * @return
     */
    public abstract TextureRegion getJumping();

    /**
     * Return the entire region
     * @return
     */
    protected abstract TextureRegion getRegion();

    @Override
    public abstract void update(float dt);
}
