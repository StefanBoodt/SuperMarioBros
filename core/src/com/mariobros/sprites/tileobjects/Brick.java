package com.mariobros.sprites.tileobjects;

import com.badlogic.gdx.maps.MapObject;
import com.mariobros.SuperMarioBros;
import com.mariobros.screens.LevelScreen;
import com.mariobros.sprites.hero.Mario;

/**
 * This class models the brick blocks.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */
public class Brick extends InteractiveTileObject {

    public Brick(LevelScreen screen, MapObject object) {
        super(screen, object);
    }

    @Override
    public void onHeadHit(Mario mario) {
        if (mario.getPowerUp() != Mario.PowerUp.NORMAL) {
            setCategoryFilter(SuperMarioBros.NOTHING_BIT);
            getCell().setTile(null);
        }
    }

}
