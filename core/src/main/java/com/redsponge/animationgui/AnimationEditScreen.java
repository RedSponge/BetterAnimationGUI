package com.redsponge.animationgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import com.redsponge.betteranimations.AnimationManager;
import com.redsponge.betteranimations.RAnimation;

public class AnimationEditScreen extends ScreenAdapter {

    private Skin uiSkin;
    private SpriteBatch batch;
    private RAnimation animation;
    private FitViewport viewport;

    private Stage stage;
    private TabbedPane tabbedPane;
    private Table contentTable;

    private MenuBar menuBar;
    private AnimationManager animationManager;

    public AnimationEditScreen(SpriteBatch batch, AnimationManager animationManager) {
        this.batch = batch;
        this.animationManager = animationManager;
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        uiSkin = VisUI.getSkin();
        stage = new Stage(viewport, batch);

        Table tabbedPaneTable = new Table(uiSkin);

        contentTable = new Table();
        tabbedPane = new TabbedPane();
        tabbedPane.add(new HomeTab());
        tabbedPane.add(new AnimationEditTab("idle", animationManager.getAnimation("idle")));
        tabbedPane.add(new AnimationEditTab("run", animationManager.getAnimation("run")));

        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                contentTable.clearChildren();
                contentTable.add(tab.getContentTable()).expand().fill();
            }
        });

        tabbedPane.switchTab(0);

        menuBar = new MenuBar();
        menuBar.addMenu(new FileMenu());

        tabbedPaneTable.setSize(stage.getWidth(), stage.getHeight());
        tabbedPaneTable.add(menuBar.getTable()).height(Value.percentHeight(0.05f, tabbedPaneTable)).fillX().expandX().row();
        tabbedPaneTable.add(tabbedPane.getTabsPane()).expandX().fillX().row();
        tabbedPaneTable.add(contentTable).expand().grow();

        stage.addActor(tabbedPaneTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        stage.act(delta);
        if(tabbedPane.getActiveTab() instanceof IUpdated) {
            ((IUpdated) tabbedPane.getActiveTab()).update(delta);
        }


        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
