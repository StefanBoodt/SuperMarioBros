package com.mariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mariobros.SuperMarioBros;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.hero.Mario;

/**
 * Created by stefanboodt on 17-01-17.
 */
public class Goomba extends Enemy {

    /**
     * The goombas body.
     */
    private Body body;

    /**
     * The state timer.
     */
    private float stateTimer;

    /**
     * The walking animation.
     */
    private Animation<TextureRegion> walking;

    /**
     * Create a new goomba on the given screen with the given (x, y) coordinates.
     * @param screen The level screen.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Goomba(LevelScreen screen, float x, float y) {
        super(screen, x, y);
        loadAnimation();
        stateTimer = 0.0f;
    }

    @Override
    public void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        this.body = screen.getWorld().createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(7 / SuperMarioBros.PPM);
        setFilters(fdef);
        fdef.shape = shape;
        this.body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertices = new Vector2[4];
        vertices[0] = new Vector2(-6, 9).scl(1 / SuperMarioBros.PPM);
        vertices[1] = new Vector2(6, 9).scl(1 / SuperMarioBros.PPM);
        vertices[2] = new Vector2(3, 3).scl(1 / SuperMarioBros.PPM);
        vertices[3] = new Vector2(-3, 3).scl(1 / SuperMarioBros.PPM);
        head.set(vertices);
        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = SuperMarioBros.ENEMY_WEAKNESS_BIT;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void onHit(Mario mario) {

    }

    /**
     * Sets the filters for the goomba.
     * @param fdef The fixturedef to add the bits to.
     */
    private void setFilters(FixtureDef fdef) {
        fdef.filter.categoryBits = SuperMarioBros.ENEMY_BIT;
        fdef.filter.maskBits = SuperMarioBros.GROUND_BIT | SuperMarioBros.ENEMY_BIT | SuperMarioBros.MARIO_BIT
                | SuperMarioBros.OBJECT_BIT | SuperMarioBros.BLOCK_BIT;
    }

    @Override
    public void update(float dt) {
        stateTimer += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(walking.getKeyFrame(stateTimer, true));
    }

    /**
     * Loads the necessary animations.
     */
    private void loadAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        TextureAtlas atlas = screen.getGame().manager.get("tileset.txt");
        TextureRegion region = atlas.findRegion("goomba");
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(region, 16 * i, 0, 16, 16));
        }
        walking = new Animation<TextureRegion>(0.4f, frames);
    }
}
