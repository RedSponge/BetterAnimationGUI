package com.redsponge.animationgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class AnimationEditScreen extends ScreenAdapter {

    private Stage stage;
    private Table wholeStage;

    private Table propertyPart;

    private Window animationInfo;
    private TextField duration;

    private Window frameImageHolder;
    private Image frameDisplay;

    private Window animationChoice;
    private List<String> animationList;

    private Window timeline;
    private Button playButton;
    private Table framesTable;
    private ScrollPane framesPane;

    private SpriteBatch batch;
    private FitViewport viewport;

    private Skin uiSkin;

    public AnimationEditScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        uiSkin = new Skin(Gdx.files.internal("neutralizerui/neutralizer-ui.json"));
        initStage();
    }

    private void initStage() {
        stage = new Stage(viewport, batch);
        stage.setDebugAll(true);
        wholeStage = new Table(uiSkin);
        wholeStage.setSize(stage.getWidth(), stage.getHeight());

        propertyPart = new Table(uiSkin);
        animationInfo = new Window("Properties", uiSkin);
        animationInfo.setMovable(false);
        duration = new TextField("", uiSkin);
        animationInfo.add(duration);

        frameImageHolder = new Window("Frame", uiSkin);
        frameImageHolder.setMovable(false);

        animationChoice = new Window("Animations", uiSkin);
        animationChoice.setMovable(false);
        animationList = new List<String>(uiSkin);
        animationList.setItems("A", "B", "C");
        animationChoice.add(animationList);


        frameDisplay = new Image(new Texture("unpowered.png"));
        frameDisplay.setScaling(Scaling.fillX);

        frameImageHolder.add(frameDisplay).grow();

        wholeStage.add(propertyPart).height(Value.percentHeight(0.75f, wholeStage)).grow();

        propertyPart.add(animationInfo).width(Value.percentWidth(0.25f, propertyPart)).grow();
        propertyPart.add(frameImageHolder).width(Value.percentWidth(0.5f, propertyPart)).grow();
        propertyPart.add(animationChoice).width(Value.percentWidth(0.25f, propertyPart)).grow();


        timeline = new Window("Timeline", uiSkin);
        timeline.setMovable(false);
        framesTable = new Table(uiSkin);
        framesPane = new ScrollPane(framesTable, uiSkin);
        timeline.add(framesPane).grow();

        wholeStage.row();
        wholeStage.add(timeline).height(Value.percentHeight(0.25f, wholeStage)).grow();

        stage.addActor(wholeStage);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
