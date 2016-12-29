package com.mariobros.interfaces;

/**
 * A small interface for all updateable things in the game.
 *
 * @since 1.0
 * @version 1.0
 *
 * @author stefan boodt
 */

public interface Updateable {

    /**
     * Updates the game with dt time. This should do all the calculations and physics so the
     * world is working correctly.
     * @param dt The time since the last update.
     */
    public void update(float dt);
}
