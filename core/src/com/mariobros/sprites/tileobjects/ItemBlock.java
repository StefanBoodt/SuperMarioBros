package com.mariobros.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.mariobros.screens.LevelScreen;

/**
 * This class represents a block with an item in it.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public class ItemBlock extends Brick {

    public ItemBlock(LevelScreen screen, MapObject object) {
        super(screen, object);
    }
}
