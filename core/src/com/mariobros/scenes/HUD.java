package com.mariobros.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mariobros.SuperMarioBros;
import com.mariobros.interfaces.Updateable;

/**
 * The HUD for the game.
 *
 * @version 1.0
 * @since 1.0
 *
 * @author stefan boodt
 */

public class HUD implements Disposable, Updateable {

    private SpriteBatch sb;

    /**
     * The stage to add the things to.
     */
    public Stage stage;

    /**
     * The view of this HUD.
     */
    private Viewport view;

    /**
     * Keeps track of the score in the game.
     */
    private static int score;

    /**
     * The current time left for the level.
     */
    private int time;

    /**
     * initialTime of the level.
     */
    private int initialTime;

    /**
     * Label for the time.
     */
    private Label countdownLabel;

    /**
     * Label for the score.
     */
    private static Label scoreLabel;

    /**
     * variable to improve counting.
     */
    private float countTime;

    public HUD(SpriteBatch sb, String currentLevel, int initialTime) {
        this.sb = sb;
        score = 0;
        setInitialTime(initialTime);
        countTime = 0;

        view = new FitViewport(SuperMarioBros.V_WIDTH, SuperMarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(view, sb);

        scoreLabel =new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countdownLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        resetTime();

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(scoreLabel).expandX().pad(10);
        table.add(countdownLabel).expandX().pad(10);
        stage.addActor(table);
    }

    /**
     * Sets the initial time to the given value.
     * @param time The new initial time.
     */
    public void setInitialTime(int time) {
        initialTime = time;
    }

    /**
     * Increases the score by added.
     * @param added The score to add.
     */
    public static void increaseScore(int added) {
        score += added;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isTimeLeft() {
        return time > 0;
    }

    @Override
    public void update(float dt) {
        countTime += dt;
        if (countTime > 1) {
            countTime--;
            time--;
            if (isTimeLeft()) {
                countdownLabel.setText(String.format("%03d", time));
            }
        }
    }

    /**
     * Resets the timer.
     */
    public void resetTime() {
        time = initialTime;
        countdownLabel.setText(String.format("%03d", time));
        countTime = 0;
    }

    /**
     * Resets the score.
     */
    public void resetScore() {
        increaseScore(-score);
    }
}
