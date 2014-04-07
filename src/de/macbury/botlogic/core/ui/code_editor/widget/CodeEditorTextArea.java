package de.macbury.botlogic.core.ui.code_editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.ui.code_editor.CodeEditorView;
import de.macbury.botlogic.core.ui.code_editor.js.JavaScriptScanner;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by macbury on 07.04.14.
 */
public class CodeEditorTextArea extends Widget {
  private static final String TAG = "CodeEditorTextArea";
  private static final float  LINE_PADDING = 2;
  private static final float  PADDING_HORIZONTAL = 10;
  private static final float  PADDING_VERITICAL  = 15;
  private static final int    GUTTER_PADDING = 10;
  private static final float  GUTTER_WIDTTH  = 60;
  private static final char   BACKSPACE = '\b';
  private static final char   ENTER_DESKTOP = '\r';
  private static final char   ENTER_ANDROID = '\n';
  private static final char   TAB = '\t';
  private static final char   DELETE = 127;
  private static final char   BULLET = 149;
  private static final float CARET_WIDTH = 2;
  private CodeEditorView scroll;
  private Clipboard clipboard;
  private HashMap<JavaScriptScanner.Kind, Color> styles;
  private ArrayList<Line> lines;

  private CodeEditorTextAreaStyle style;
  private ShapeRenderer shape;

  private String text           = "";
  private Caret caret;
  private float blinkTime = 0.32f;
  private ClickListener inputListener;
  private int longestLineLength;
  private float width;
  private float height;
  private long lastBlink;
  private boolean cursorOn;

  public CodeEditorTextArea(CodeEditorTextAreaStyle style, CodeEditorView scrollContainer) {
    this.scroll = scrollContainer;
    this.style = style;
    lines      = new ArrayList<Line>();
    styles     = new HashMap<JavaScriptScanner.Kind, Color>();
    clipboard  = Gdx.app.getClipboard();
    caret      = new Caret(this.lines);

    styles.put(JavaScriptScanner.Kind.KEYWORD, style.syntaxKeywordColor);
    styles.put(JavaScriptScanner.Kind.NORMAL, style.textColor);
    styles.put(JavaScriptScanner.Kind.STRING, style.syntaxStringColor);
    styles.put(JavaScriptScanner.Kind.COMMENT, style.syntaxCommentColor);
    styles.put(JavaScriptScanner.Kind.NUMBER, style.syntaxNumberColor);
    styles.put(JavaScriptScanner.Kind.SPECIAL_KEYWORD, style.syntaxSpecialKeywordColor);

    setText("");

    initializeKeyboard();
    initializeMouse();

    resetBlink();
  }

  private void initializeMouse() {

  }

  @Override
  public float getPrefWidth() {
    return width;
  }

  @Override
  public float getPrefHeight() {
    return height;
  }

  private void initializeKeyboard() {
    inputListener = new ClickListener();
    addListener(inputListener);
  }

  public void parse(String text) {
    this.lines.clear();
    JavaScriptScanner js = new JavaScriptScanner(text);
    JavaScriptScanner.Kind kind;

    Line line = new Line();
    this.lines.add(line);
    while((kind=js.scan()) != JavaScriptScanner.Kind.EOF) {
      if(kind == JavaScriptScanner.Kind.NEWLINE) {
        line = new Line();
        this.lines.add(line);
      } else {
        line.add(new Element(kind, js.getString()));
      }
    }

    this.longestLineLength = 0;
    for (int i = 0; i < lines.size(); i++) {
      Line row = lines.get(i);
      row.setLineNumber(i+1);

      row.buildString();
      longestLineLength = Math.max(row.textLenght(), longestLineLength);
    }

    updateSize();
  }

  public void setText(String text) {
    this.text = text;
    parse(this.text);
    resetBlink();
    caret.setCursorPosition(1,1);
  }

  private void updateSize() {
    this.height = this.lines.size() * (this.style.font.getLineHeight() + LINE_PADDING) + PADDING_VERITICAL * 2;
    this.width  = longestLineLength * this.style.font.getSpaceWidth() + PADDING_HORIZONTAL * 2;

    this.invalidate();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Stage stage = getStage();
    boolean focused = stage != null && stage.getKeyboardFocus() == this;

    float x      = getX();
    float y      = getY();
    float width  = getWidth();
    float height = getHeight();

    float parentWidth  = getParent().getWidth();
    float parentHeight = getParent().getHeight();

    float totalHeight  = Math.max(parentHeight, height);
    float totalWidth   = Math.max(parentWidth, width);

    float lineHeight   = this.style.font.getLineHeight() + LINE_PADDING;

    style.lineNumberBackround.draw(batch, x,y, GUTTER_WIDTTH, totalHeight);

    float gy = totalHeight + y;
    float gx = x + GUTTER_WIDTTH;

    for(Line line : lines) {
      int ly              = line.getLineNumber() - 1;
      float lineElementX  = 0;
      float linePosY      = gy - (ly * lineHeight)  - PADDING_VERITICAL;

      style.font.setColor(style.lineNumberColor);
      style.font.draw(batch, line.getLineString(), gx - style.font.getBounds(line.getLineString()).width - GUTTER_PADDING, linePosY);

      for (int lx = 0; lx < line.size(); lx++) {
        Element elem                 = line.get(lx);
        BitmapFont.TextBounds bounds = style.font.getBounds(elem.text);

        style.font.setColor(styles.get(elem.kind));
        style.font.draw(batch, elem.text, gx + GUTTER_PADDING + lineElementX, linePosY);

        lineElementX += bounds.width;
      }

      if (line.getLineNumber() == caret.getRow()) {
        style.focusedLineBackround.draw(batch, 0, linePosY - PADDING_VERITICAL, totalWidth, lineHeight);
      }
    }
    
    if (focused) {
      blink();

      if (cursorOn) {
        float caretY = gy - ((caret.getRow()-1) * lineHeight) - PADDING_VERITICAL*2;
        style.cursor.draw(batch, gx + GUTTER_PADDING + (caret.getCol()-1) * style.font.getSpaceWidth(), caretY, CARET_WIDTH, lineHeight);
      }
    }
  }


  public void resetBlink() {
    lastBlink = 0;
    cursorOn  = false;
  }

  private void blink() {
    long time = TimeUtils.nanoTime();
    if ((time - lastBlink) / 1000000000.0f > blinkTime) {
      cursorOn = !cursorOn;
      lastBlink = time;
    }
  }

  public static class CodeEditorTextAreaStyle extends TextField.TextFieldStyle {
    public Drawable lineNumberBackround;
    public Drawable focusedLineBackround;
    public Color lineNumberColor;

    public Color syntaxKeywordColor;
    public Color textColor;
    public Color syntaxStringColor;
    public Color syntaxCommentColor;
    public Color syntaxNumberColor;
    public Color syntaxSpecialKeywordColor;
  }

  class KeyRepeatTask extends Timer.Task {
    int keycode;

    public void run () {
      inputListener.keyDown(null, keycode);
    }
  }
}
