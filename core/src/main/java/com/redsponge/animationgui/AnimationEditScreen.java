package com.redsponge.animationgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.redsponge.betteranimations.Frame;
import com.redsponge.betteranimations.RAnimation;
import com.redsponge.betteranimations.RPlayMode;

public class AnimationEditScreen extends ScreenAdapter {

    private Stage stage;
    private Table wholeStage;

    private Table propertyPart;

    private Window frameProperties;
    private TextField duration;

    private Window frameImageHolder;
    private Image frameDisplay;

    private Window animationProperties;
    private List<String> animationList;

    private Window timeline;
    private TextButton playButton;
    private Table framesTable;
    private ScrollPane framesPane;

    private SpriteBatch batch;
    private FitViewport viewport;

    private Skin uiSkin;

    private RAnimation animation;
    private Frame[] frames;
    private Frame currentFrame;
    private int currentFrameI;
    private ImageButton currentFrameButton;

    private boolean playing;

    private SelectBox<RPlayMode> playModeSelectBox;

    public AnimationEditScreen(SpriteBatch batch, RAnimation animation) {
        this.batch = batch;
        this.animation = animation;
    }

    @Override
    public void show() {
        viewport = new FitViewport(Constants.GUI_WIDTH, Constants.GUI_HEIGHT);
        uiSkin = new Skin(Gdx.files.internal("neutralizerui/neutralizer-ui.json"));
        initStage();
        fillWithAnimationData();
    }

    private void fillWithAnimationData() {
        frames = animation.getFrames();
        for (int i = 0; i < frames.length; i++) {
            TextureRegion texture = frames[i].getTextureRegion();
            final ImageButton image = new ImageButton(new TextureRegionDrawable(texture));
            image.getImageCell().expand().fill();
            final int finalI = i;
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    beginDisplay(image, finalI);
                }
            });
            framesTable.add(image).size(100, 100).fill();
        }
    }

    private void beginDisplay(ImageButton selectedButton, int i) {
        currentFrame = frames[i];
        currentFrameI = i;
        duration.setText(String.valueOf(currentFrame.getDuration()));
        frameDisplay.setDrawable(new TextureRegionDrawable(currentFrame.getTextureRegion()));
        if(currentFrameButton != null) {
            currentFrameButton.getImage().setColor(Color.WHITE);
        }
        selectedButton.getImage().setColor(Color.GRAY);
        currentFrameButton = selectedButton;
    }

    private void initStage() {
        stage = new Stage(viewport, batch);
        stage.setDebugAll(true);
        wholeStage = new Table(uiSkin);
        wholeStage.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(wholeStage);
        System.out.println(wholeStage.getHeight());

        propertyPart = new Table(uiSkin);
        setupProperties();
        setupFrame();
        setupAnimationProperties();

        wholeStage.add(propertyPart).height(Value.percentHeight(0.75f, wholeStage)).grow();
        initTimeline();

        wholeStage.row();
        wholeStage.add(timeline).height(Value.percentHeight(0.25f, wholeStage)).grow();
        wholeStage.setHeight(stage.getHeight());

        Gdx.input.setInputProcessor(stage);
    }

    private void initTimeline() {
        timeline = new Window("Timeline", uiSkin);
        timeline.setMovable(false);
        framesTable = new Table(uiSkin);
        framesPane = new ScrollPane(framesTable, uiSkin);

        playButton = new TextButton("|>", uiSkin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePlay();
            }
        });
        timeline.add(framesPane).grow();
        timeline.row();
        timeline.add(playButton);
    }

    private void togglePlay() {
        playing = !playing;

        if(playing) {
            playButton.setText("[]");
            animation.setCurrentFrame(0);
        }
        else {
            playButton.setText("|>");
        }
    }

    private void setupAnimationProperties() {
        animationProperties = new Window("Animations", uiSkin);
        animationProperties.setMovable(false);
        Label playModeLabel = new Label("Play Mode:", uiSkin);
        animationProperties.add(playModeLabel);
        animationProperties.row();
        playModeSelectBox = new SelectBox<RPlayMode>(uiSkin);
        playModeSelectBox.setItems(RPlayMode.values());
        animationProperties.add(playModeSelectBox);
        playModeSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                animation.setPlayMode(playModeSelectBox.getSelected());
            }
        });

        propertyPart.add(animationProperties).width(Value.percentWidth(0.25f, propertyPart)).grow();
    }

    private void setupFrame() {
        frameImageHolder = new Window("Frame", uiSkin);
        frameImageHolder.setMovable(false);
        frameDisplay = new Image(new Texture("unpowered.png"));
        frameImageHolder.add(frameDisplay).grow();

        propertyPart.add(frameImageHolder).width(Value.percentWidth(0.5f, propertyPart)).grow();
    }

    private void setupProperties() {
        frameProperties = new Window("Properties", uiSkin);
        frameProperties.setMovable(false);
        Label dur = new Label("Frame Duration (ms)", uiSkin);
        duration = new TextField("", uiSkin);
        frameProperties.add(dur);
        frameProperties.row();
        frameProperties.add(duration);
        frameProperties.row();

        TextButton saveButton = new TextButton("Save", uiSkin);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveFrame();
            }
        });
        frameProperties.add(saveButton);

        propertyPart.add(frameProperties).width(Value.percentWidth(0.25f, propertyPart)).grow();
    }

    private void saveFrame() {
        float dur = Float.parseFloat(duration.getText());
        currentFrame.setDuration(dur);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(playing) {
            animation.update(delta);
            ((TextureRegionDrawable) frameDisplay.getDrawable()).setRegion(animation.getCurrentFrame().getTextureRegion());
        }

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
