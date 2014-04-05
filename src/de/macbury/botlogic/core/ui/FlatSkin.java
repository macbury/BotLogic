package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;


public class FlatSkin extends Skin {
  public static final float BUTTON_PADDING = 10;
  public Label.LabelStyle robotoThinLabelStyle;
  public BitmapFont robotoThinFont;
  public BitmapFont robotoBigFont;
  public Label.LabelStyle robotoBigLabelStyle;
  public ScrollPane.ScrollPaneStyle tileScrollPane;
  public ImageButton.ImageButtonStyle redImageButton;
  public BitmapFont defaultFont;
  public UIBuilder  builder;
  public TextButton.TextButtonStyle redTextButton;

  public FlatSkin() {
    super(new TextureAtlas(Gdx.files.internal("gui/skin.pack")));

    this.defaultFont   = new BitmapFont(Gdx.files.internal("fonts/default.fnt"));
    this.add("default", defaultFont);

    this.robotoBigFont  = new BitmapFont(Gdx.files.internal("fonts/roboto_big.fnt"));
    this.robotoThinFont = new BitmapFont(Gdx.files.internal("fonts/roboto_thin.fnt"));

    this.redTextButton   = new TextButton.TextButtonStyle();
    redTextButton.up     = this.getDrawable("button_red_normal");
    redTextButton.down   = this.getDrawable("button_red_pressed");
    redTextButton.font   = defaultFont;

    this.redImageButton  = new ImageButton.ImageButtonStyle();
    redImageButton.up    = this.getDrawable("button_red_normal");
    redImageButton.down  = this.getDrawable("button_red_pressed");

    this.tileScrollPane         = new ScrollPane.ScrollPaneStyle();
    tileScrollPane.vScroll      = this.getDrawable("white_scroll_background");
    tileScrollPane.hScroll      = this.getDrawable("white_scroll_background");
    tileScrollPane.hScrollKnob  = this.getDrawable("white_scroll_knob");
    tileScrollPane.vScrollKnob  = this.getDrawable("white_scroll_knob");

    this.robotoBigLabelStyle      = new Label.LabelStyle();
    robotoBigLabelStyle.font      = robotoBigFont;
    robotoBigLabelStyle.fontColor = Color.WHITE;

    this.robotoThinLabelStyle      = new Label.LabelStyle();
    robotoThinLabelStyle.font      = robotoThinFont;
    robotoThinLabelStyle.fontColor = Color.WHITE;

    this.builder = new UIBuilder(this);

    //Gdx.input.setCursorCatched(true);
    Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("gui/arrow.png"));
    Gdx.input.setCursorImage(cursorPixmap, 0, 0);
  }

}
