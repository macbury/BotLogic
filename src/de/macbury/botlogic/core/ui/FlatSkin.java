package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;


public class FlatSkin extends Skin {
  public static final float BUTTON_PADDING = 10;
  public Slider.SliderStyle sliderStyle;
  public ImageButton.ImageButtonStyle blueImageButton;
  public CheckBox.CheckBoxStyle checkBoxStyle;
  public List.ListStyle listStyle;
  public ScrollPane.ScrollPaneStyle scrollPaneStyle;
  public SelectBox.SelectBoxStyle selectBoxStyle;
  public Window.WindowStyle dialogStyle;
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

    this.blueImageButton  = new ImageButton.ImageButtonStyle();
    blueImageButton.up    = this.getDrawable("button_blue_norma");
    blueImageButton.down  = this.getDrawable("button_blue_pressed");

    this.tileScrollPane         = new ScrollPane.ScrollPaneStyle();
    tileScrollPane.vScroll      = this.getDrawable("white_scroll_background");
    tileScrollPane.hScroll      = this.getDrawable("white_scroll_background");
    tileScrollPane.hScrollKnob  = this.getDrawable("white_scroll_knob");
    tileScrollPane.vScrollKnob  = this.getDrawable("white_scroll_knob");

    this.scrollPaneStyle         = new ScrollPane.ScrollPaneStyle();
    scrollPaneStyle.vScroll      = this.getDrawable("white_scroll_background");
    scrollPaneStyle.hScroll      = this.getDrawable("white_scroll_background");
    scrollPaneStyle.hScrollKnob  = this.getDrawable("white_scroll_knob");
    scrollPaneStyle.vScrollKnob  = this.getDrawable("white_scroll_knob");
    scrollPaneStyle.background   = this.getDrawable("background_dark");

    this.robotoBigLabelStyle      = new Label.LabelStyle();
    robotoBigLabelStyle.font      = robotoBigFont;
    robotoBigLabelStyle.fontColor = Color.WHITE;

    this.robotoThinLabelStyle      = new Label.LabelStyle();
    robotoThinLabelStyle.font      = robotoThinFont;
    robotoThinLabelStyle.fontColor = Color.WHITE;

    this.dialogStyle               = new Dialog.WindowStyle();
    dialogStyle.stageBackground    = this.getDrawable("background_dark_transparent");
    dialogStyle.background         = this.getDrawable("background_light");
    dialogStyle.titleFont          = defaultFont;
    dialogStyle.titleFontColor     = Color.WHITE;

    this.listStyle                 = new List.ListStyle();
    listStyle.background           = this.getDrawable("list_box_background");
    listStyle.font                 = robotoThinFont;
    listStyle.fontColorSelected    = Color.WHITE;
    listStyle.fontColorUnselected  = FlatColors.CLOUDS;
    listStyle.selection            = this.getDrawable("white_scroll_background");

    this.selectBoxStyle             = new SelectBox.SelectBoxStyle();
    selectBoxStyle.scrollStyle      = scrollPaneStyle;
    selectBoxStyle.listStyle        = listStyle;
    selectBoxStyle.fontColor        = FlatColors.CLOUDS;
    selectBoxStyle.background       = this.getDrawable("select_box_background");
    selectBoxStyle.backgroundOpen   = this.getDrawable("select_box_active_background");
    selectBoxStyle.backgroundOver   = this.getDrawable("select_box_hover_background");
    selectBoxStyle.font             = robotoThinFont;
    selectBoxStyle.fontColor        = FlatColors.CLOUDS;

    this.checkBoxStyle              = new CheckBox.CheckBoxStyle();
    this.checkBoxStyle.checkboxOn   = this.getDrawable("checkbox_on");
    this.checkBoxStyle.checkboxOff  = this.getDrawable("checkbox_off");
    this.checkBoxStyle.font         = robotoThinFont;

    this.sliderStyle                = new Slider.SliderStyle();
    sliderStyle.background          = this.getDrawable("white_scroll_background");
    sliderStyle.knob                = this.getDrawable("white_scroll_knob");
    this.builder = new UIBuilder(this);

    //Gdx.input.setCursorCatched(true);
    Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("gui/arrow.png"));
    Gdx.input.setCursorImage(cursorPixmap, 0, 0);

  }

}
