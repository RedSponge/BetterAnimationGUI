package com.redsponge.animationgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.redsponge.betteranimations.Frame;
import com.redsponge.betteranimations.RAnimation;
import com.redsponge.betteranimations.RPlayMode;

public class AnimationEditTab extends Tab implements IUpdated {

    private String name;
    private Table wholeStage;

    private Table propertyPart;

    private Window frameProperties;
    private TextField duration;

    private Window frameImageHolder;
    private Image frameDisplay;
    private TextureRegionDrawable frameDrawable;

    private Window animationProperties;

    private Window timeline;
    private TextButton playButton;
    private Table framesTable;
    private ScrollPane framesPane;

    private Skin uiSkin;

    private RAnimation animation;
    private Frame[] frames;
    private Frame currentFrame;
    private ImageButton currentFrameButton;
    private ImageButton[] frameButtons;

    private boolean playing;

    private SelectBox<RPlayMode> playModeSelectBox;

    public AnimationEditTab(String name, RAnimation animation) {
        super(true);
        this.name = name;
        this.animation = animation;
        this.uiSkin = VisUI.getSkin();

        initStage();
        fillWithAnimationData();
    }

    private void fillWithAnimationData() {
        System.out.println(animation);
        frames = animation.getFrames();
        frameButtons = new ImageButton[frames.length];
        for (int i = 0; i < frames.length; i++) {
            TextureRegion texture = frames[i].getTextureRegion();
            final ImageButton image = new ImageButton(new TextureRegionDrawable(texture));
            final int finalI = i;

            image.getImageCell().expand().fill();
            image.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    beginDisplay(finalI);
                }
            });
            framesTable.add(image).size(100, 100).fill();
            frameButtons[i] = image;
        }
    }

    private void beginDisplay(int i) {
        currentFrame = frames[i];
        ImageButton selectedButton = frameButtons[i];
        duration.setText(String.valueOf(currentFrame.getDuration()));

        frameDisplay.setDrawable(new TextureRegionDrawable(currentFrame.getTextureRegion()));

        if(currentFrameButton != null) {
            currentFrameButton.getImage().setColor(Color.WHITE);
        }

        selectedButton.getImage().setColor(Color.GRAY);
        currentFrameButton = selectedButton;
    }

    private void initStage() {
        wholeStage = new Table(uiSkin);
        System.out.println(wholeStage.getHeight());

        propertyPart = new Table(uiSkin);
        setupProperties();
        setupFrame();
        setupAnimationProperties();

        wholeStage.add(propertyPart).height(Value.percentHeight(0.70f, wholeStage)).grow();
        initTimeline();

        wholeStage.row();
        wholeStage.add(timeline).height(Value.percentHeight(0.30f, wholeStage)).grow();
    }

    private void initTimeline() {
        timeline = new Window("Timeline", uiSkin);
        timeline.setMovable(false);
        framesTable = new Table(uiSkin);
        framesPane = new ScrollPane(framesTable, uiSkin);
        framesPane.setOverscroll(false, false);

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
        playModeSelectBox.setSelected(animation.getPlayMode());

        propertyPart.add(animationProperties).width(Value.percentWidth(0.25f, propertyPart)).grow();
    }

    private void setupFrame() {
        frameImageHolder = new Window("Frame", uiSkin);
        frameImageHolder.setMovable(false);

        frameDisplay = new Image();

        frameImageHolder.add(frameDisplay).grow();

        propertyPart.add(frameImageHolder).width(Value.percentWidth(0.5f, propertyPart)).grow();
    }

    private void setupProperties() {
        frameProperties = new Window("Properties", uiSkin);
        frameProperties.setMovable(false);
        Label durationLabel = new Label("Frame Duration", uiSkin);
        duration = new TextField("", uiSkin);
        duration.setTextFieldFilter(new DigitFilter());
        duration.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                final float dur;

                if(duration.getText().isEmpty()) {
                    dur = 0;
                } else {
                    dur = Float.parseFloat(duration.getText());
                }

                currentFrame.setDuration(dur);
            }
        });

        frameProperties.add(durationLabel);
        frameProperties.row();
        frameProperties.add(duration);
        propertyPart.add(frameProperties).width(Value.percentWidth(0.25f, propertyPart)).grow();
    }

    @Override
    public boolean save() {
        super.save();
        System.out.println("SAVING!");
        setDirty(false);
        return true;
    }

    @Override
    public void update(float delta) {
        if(playing) {
            animation.update(delta);
            frameDisplay.setDrawable(new TextureRegionDrawable(animation.getCurrentFrame().getTextureRegion()));
        }
    }

    @Override
    public String getTabTitle() {
        return name;
    }

    @Override
    public Table getContentTable() {
        return wholeStage;
    }
}
