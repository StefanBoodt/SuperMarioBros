package com.mariobros.sprites.hero;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mariobros.SuperMarioBros;
import com.mariobros.sprites.enemies.Enemy;

/**
 * Class to monitor the heros state.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public enum PowerUp {

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
            this.body = PowerUp.world.createBody(bdef);
            FixtureDef fdef = new FixtureDef();
            CircleShape shape = new CircleShape();
            shape.setRadius(6 / SuperMarioBros.PPM);
            fdef.shape = shape;
            this.body.createFixture(fdef);
        }

        @Override
        public void redefine() {
            Vector2 position = getPosition();
            world.destroyBody(body);
            define(position);
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
     * The world to live in.
     */
    protected static World world;

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
     * Sets the world to be affected by.
     * @param world The world to live in.
     */
    public static void setWorld(World world) {
        PowerUp.world = world;
    }

    /**
     * Jump the mario in the current state. It gives a default implementation for the jump.
     */
    public void jump() {
        body.applyLinearImpulse(new Vector2(0.0f, Mario.SPEED_UP_Y), body.getWorldCenter(), true);
    }
}
