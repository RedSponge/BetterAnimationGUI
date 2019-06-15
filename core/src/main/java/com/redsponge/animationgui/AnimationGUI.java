package com.redsponge.animationgui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisUI;
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
        VisUI.load(new Skin(Gdx.files.internal("tinted/tinted.json")));

        setScreen(new AnimationEditScreen(batch, animationManager));
    }

    @Override
    public void dispose() {
        super.dispose();
        VisUI.dispose();
    }
}