package com.mariobros.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.mariobros.SuperMarioBros;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.hero.Mario;

import java.util.logging.Level;

/**
 * Abstract class modelling interactive tile objects. These objects include different kinds of
 * blocks.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public abstract class InteractiveTileObject {

    /**
     * The boxworld to work in.
     */
    protected World world;

    /**
     * The map used.
     */
    protected TiledMap map;

    /**
     * The bounds of this InteractiveTileObject.
     */
    protected Rectangle bounds;

    /**
     * The body of this object.
     */
    protected Body body;

    /**
     * The screen used.
     */
    protected LevelScreen screen;

    /**
     * The object
     */
    protected MapObject object;

    /**
     * The fixture of this object.
     */
    protected Fixture fixture;

    /**
     * Creates a new tileobject.
     * @param screen The current screen.
     * @param object The object on this map.
     */
    public InteractiveTileObject(LevelScreen screen, MapObject object) {
        this.object = object;
        this.screen = screen;
        bounds = ((RectangleMapObject) object).getRectangle();
        this.world = screen.getWorld();
        map = screen.getMap();
        initBody();
    }

    /**
     * Does some required setup.
     */
    private void initBody() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / SuperMarioBros.PPM, (bounds.getY() + bounds.getHeight() / 2) / SuperMarioBros.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / SuperMarioBros.PPM, bounds.getHeight() / 2 / SuperMarioBros.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    /**
     * Code that specifies what should to the block happen if marios head collides with the object.
     * @param mario The mario that collides with this block.
     */
    public abstract void onHeadHit(Mario mario);

    /**
     * Sets the catagory to the specified bit.
     * @param filterBit The catagory to classify this object as.
     */
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }
}
