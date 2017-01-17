package com.mariobros.sprites.tileobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.mariobros.SuperMarioBros;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.hero.Mario;

/**
 * This class represents a block with an item in it.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public class ItemBlock extends InteractiveTileObject {

    public ItemBlock(LevelScreen screen, MapObject object) {
        super(screen, object);
    }

    @Override
    public void onHeadHit(Mario mario) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("ItemBlocks");
        final int w = Math.round(layer.getTileWidth());
        final int h = Math.round(layer.getTileHeight());
        String name = "used_block_" +  w + "x" + h + ".png";
        TextureRegion region = new TextureRegion(new Texture(name));
        getCell().setTile(new StaticTiledMapTile(region));
    }
}
