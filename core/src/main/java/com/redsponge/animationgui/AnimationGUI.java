package com.redsponge.animationgui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.redsponge.betteranimations.AnimationManager;
import com.redsponge.betteranimations.AnimationParser;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class AnimationGUI extends Game {

    private AnimationManager animationManager;
    private TextureAtlas atlas;
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        atlas = new TextureAtlas(Gdx.files.internal("unpowered.atlas"));
        animationManager = AnimationParser.parse(Gdx.files.internal("player.ranim"), atlas);

        setScreen(new AnimationEditScreen(batch, animationManager.getAnimation("idle")));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}