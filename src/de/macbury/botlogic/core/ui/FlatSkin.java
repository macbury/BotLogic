package de.macbury.botlogic.core.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import de.macbury.botlogic.core.ui.code_editor.widget.CodeEditorTextArea;
import de.macbury.botlogic.core.ui.colors.FlatColors;
import de.macbury.botlogic.core.ui.colors.SolarizedDarkColors;
import de.macbury.botlogic.core.ui.tooltip.TooltipWidget;


public class FlatSkin extends Skin {
  public static final float BUTTON_PADDING = 10;
  public TooltipWidget.TooltipStyle tooltipStyle;
  private Cursor pointer;
  private Cursor text;
  public Label.LabelStyle codeLabelStyle;
  public ScrollPane.ScrollPaneStyle codeEditorScroll;
  public BitmapFont codeFont;
  public CodeEditorTextArea.CodeEditorTextAreaStyle codeEditorArea;
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
    this.codeFont       = new BitmapFont(Gdx.files.internal("fonts/code.fnt"));

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

    this.codeLabelStyle           = new Label.LabelStyle();
    codeLabelStyle.font      = codeFont;
    codeLabelStyle.fontColor = SolarizedDarkColors.TEXT;


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


    this.codeEditorScroll        =  new ScrollPane.ScrollPaneStyle();
    codeEditorScroll.vScroll      = this.getDrawable("white_scroll_background");
    codeEditorScroll.hScroll      = this.getDrawable("white_scroll_background");
    codeEditorScroll.hScrollKnob  = this.getDrawable("white_scroll_knob");
    codeEditorScroll.vScrollKnob  = this.getDrawable("white_scroll_knob");
    codeEditorScroll.background   = this.getDrawable("text_area_background");

    this.codeEditorArea                           = new CodeEditorTextArea.CodeEditorTextAreaStyle();
    this.codeEditorArea.background                = this.getDrawable("text_area_background");
    this.codeEditorArea.focusedBackground         = this.getDrawable("text_area_background");
    this.codeEditorArea.focusedFontColor          = this.codeEditorArea.fontColor = SolarizedDarkColors.TEXT;
    this.codeEditorArea.font                      = codeFont;
    this.codeEditorArea.selection                 = this.getDrawable("text_area_selection");
    this.codeEditorArea.cursor                    = this.getDrawable("text_area_cursor");
    this.codeEditorArea.lineNumberBackround       = this.getDrawable("text_area_line_number_background");
    this.codeEditorArea.focusedLineBackround      = this.getDrawable("text_area_current_line_background");
    this.codeEditorArea.lineNumberColor           = SolarizedDarkColors.LINE_NUMBER;
    this.codeEditorArea.textColor                 = SolarizedDarkColors.TEXT;
    this.codeEditorArea.syntaxCommentColor        = SolarizedDarkColors.COMMENT;
    this.codeEditorArea.syntaxNumberColor         = SolarizedDarkColors.NUMBER;
    this.codeEditorArea.syntaxStringColor         = SolarizedDarkColors.STRING;
    this.codeEditorArea.syntaxKeywordColor        = SolarizedDarkColors.KEYWORD;
    this.codeEditorArea.syntaxSpecialKeywordColor = SolarizedDarkColors.SPECIAL_KEYWORD;
    this.codeEditorArea.syntaxErrorLineBackground = this.getDrawable("textarea_syntax_error_line_number");
    this.codeEditorArea.syntaxErrorTextColor      = Color.WHITE;
    this.codeEditorArea.exceptionGutterIcon       = this.getDrawable("exception");

    this.pointer = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("gui/arrow.png")), 0, 0);
    this.text = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("gui/text_cursor.png")), 16, 16);

    this.tooltipStyle           = new TooltipWidget.TooltipStyle();
    tooltipStyle.font           = robotoThinFont;
    tooltipStyle.fontColor      = Color.WHITE;
    tooltipStyle.background     = this.getDrawable("tooltip_background");

    this.builder = new UIBuilder(this);
    setPointerCursor();
  }

  public void setPointerCursor() {
    Gdx.graphics.setCursor(pointer);
  }

  public void setTextCursor() {
    Gdx.graphics.setCursor(pointer);
  }
}
