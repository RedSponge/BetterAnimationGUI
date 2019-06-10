package com.redsponge.animationgui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.redsponge.betteranimations.AnimationManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AnimationGUI extends Game {

    private AnimationManager animationManager;
    private TextureAtlas atlas;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new AnimationEditScreen(batch));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}