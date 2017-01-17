package com.mariobros.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mariobros.SuperMarioBros;
import com.mariobros.sprites.enemies.Enemy;
import com.mariobros.sprites.hero.Mario;
import com.mariobros.sprites.tileobjects.InteractiveTileObject;

/**
 * Created by stefanboodt on 30-12-16.
 */

public class WorldCollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        final int collision = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (collision) {
            case SuperMarioBros.MARIO_HEAD_BIT | SuperMarioBros.BLOCK_BIT: {
                Mario mario;
                InteractiveTileObject block;
                if (fixA.getUserData() instanceof Mario) {
                    mario = (Mario) fixA.getUserData();
                    block = (InteractiveTileObject) fixB.getUserData();
                } else {
                    mario = (Mario) fixB.getUserData();
                    block = (InteractiveTileObject) fixB.getUserData();
                }
                block.onHeadHit(mario);
                break;
            }
            case SuperMarioBros.MARIO_BIT | SuperMarioBros.ENEMY_WEAKNESS_BIT: {
                Mario mario = fixA.getUserData() instanceof Mario ? (Mario) fixA.getUserData() : (Mario) fixB.getUserData();
                Enemy enemy = fixA.getFilterData().categoryBits == SuperMarioBros.ENEMY_WEAKNESS_BIT ? (Enemy) fixB.getUserData() : (Enemy) fixA.getUserData();
                enemy.onHit(mario);
                break;
            }
            case SuperMarioBros.MARIO_BIT | SuperMarioBros.ENEMY_BIT: {
                Mario mario = fixA.getUserData() instanceof Mario ? (Mario) fixA.getUserData() : (Mario) fixB.getUserData();
                Enemy enemy = fixA.getFilterData().categoryBits == SuperMarioBros.ENEMY_WEAKNESS_BIT ? (Enemy) fixB.getUserData() : (Enemy) fixA.getUserData();
                mario.hit(enemy);
                break;
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
