package com.mariobros.tools;

import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mariobros.SuperMarioBros;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.enemies.Goomba;
import com.mariobros.sprites.tileobjects.Brick;
import com.mariobros.sprites.tileobjects.ItemBlock;

/**
 * Renders the Box2D world and renders debug lines.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */

public class Box2DWorldCreator {

    private Box2DDebugRenderer debug;

    public Box2DWorldCreator(LevelScreen screen) {
        World world = screen.getWorld();
        Map map = screen.getMap();

        immobileBodyDefs(world, map, "Ground", SuperMarioBros.GROUND_BIT);
        immobileBodyDefs(world, map, "Coins", SuperMarioBros.ITEM_BIT);
        immobileBodyDefs(world, map, "Pipes", SuperMarioBros.OBJECT_BIT);
        immobileBodyDefs(world, map, "Checkpoints", SuperMarioBros.OBJECT_BIT);
        for(MapObject object : map.getLayers().get("Blocks").getObjects().getByType(RectangleMapObject.class)){
            new Brick(screen, object);
        }
        for(MapObject object : map.getLayers().get("ItemBlocks").getObjects().getByType(RectangleMapObject.class)){
            new ItemBlock(screen, object);
        }
        for(MapObject object : map.getLayers().get("Goombas").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Goomba(screen, rect.getX(), rect.getY());
        }
    }

    /**
     * Convienience method to build bodies.
     * @param world The world
     * @param map The map
     * @param name The name of the layer.
     * @param catagory The bitcatagory.
     */
    private void immobileBodyDefs(World world, Map map, String name, short catagory) {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Body body;

        for(MapObject object : map.getLayers().get(name).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / SuperMarioBros.PPM, (rect.getY() + rect.getHeight() / 2) / SuperMarioBros.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / SuperMarioBros.PPM, rect.getHeight() / 2 / SuperMarioBros.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = catagory;
            body.createFixture(fdef);
        }
    }
}
