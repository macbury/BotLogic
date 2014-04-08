package de.macbury.botlogic.core.ui.code_editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
  private static final int DELETE_RIGHT = 1;
  private static final int DELETE_LEFT = -1;
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

  KeyRepeatTask keyRepeatTask = new KeyRepeatTask();
  float keyRepeatInitialTime = 0.4f;
  float keyRepeatTime = 0.05f;

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
    inputListener = new ClickListener() {
      @Override
      public boolean keyTyped(InputEvent event, char character) {
        return CodeEditorTextArea.this.keyTyped(event, character);
      }

      @Override
      public boolean keyDown(InputEvent event, int keycode) {
        return CodeEditorTextArea.this.keyDown(event, keycode);
      }

      @Override
      public boolean keyUp(InputEvent event, int keycode) {
        keyRepeatTask.cancel();
        return true;
      }
    };
    addListener(inputListener);
  }

  public boolean isFocused() {
    return (getStage() != null && getStage().getKeyboardFocus() == this);
  }

  private boolean keyTyped(InputEvent event, char character) {
    if (isFocused()) {
      if (character == TAB) {
        caret.clearSelection();
        insertText("  ");
        caret.incCol(2);
      } else if (character == DELETE) {
        delete();
      } else if (character == BACKSPACE) {
        backspace();
      } else if (character == ENTER_DESKTOP) {
        insertText("\n");
        caret.incRow();
        int spaces = caret.getPrevPadding();
        Gdx.app.log(TAG, "Spaces in last line: "+ spaces);
        caret.setCol(0);
        for (int i = 0; i < spaces; i++) {
          insertText(" ");
        }
        caret.setCol(spaces);
      } else if (style.font.containsCharacter(character)) {
        insertText(String.valueOf(character));
        caret.incCol(1);
      } else {
        return false;
      }

      return true;
    } else {
      return false;
    }
  }

  private void insertText(String ins) {
    if (caret.haveSelection()) {
      delete();
    }

    String lineText = getAllText();
    int pos = caret.getCaretPosition();

    String finalText = lineText.substring(0, pos) + ins;
    if (pos < lineText.length()) {
      finalText += lineText.substring(pos, lineText.length());
    }

    parse(finalText);
  }

  private void delete() {
    remove(DELETE_RIGHT);
  }

  private void backspace() {
    remove(DELETE_LEFT);
  }

  private void remove(int i) {
    String lineText = getAllText();
    if (lineText.length() == 0) {
      return;
    }
    int pos = Math.max(0, caret.getCaretPosition());

    if (pos <= 0 && i == DELETE_LEFT) {
      return;
    }

    if (caret.haveSelection()) {
      int startPos = caret.getSelectionCaretPosition();

      int from = Math.min(pos, startPos);
      int to = Math.max(pos, startPos);

      String finalText = lineText.substring(0, from);
      if (pos < lineText.length()) {
        finalText += lineText.substring(to, lineText.length());
      }
      caret.moveToSelectionStart();
      caret.clearSelection();
      parse(finalText);
    } else {
      String finalText = null;
      if (i == -1 && pos > 0) {
        finalText = lineText.substring(0, pos + i);
        if (pos < lineText.length()) {
          finalText += lineText.substring(pos, lineText.length());
        }
        caret.moveOneCharLeft();
      } else {
        finalText = lineText.substring(0, pos);
        if (pos + 1 < lineText.length()) {
          finalText += lineText.substring(pos+ i, lineText.length());
        }
        //caret.moveOneCharRight();
      }

      parse(finalText);
    }

  }

  public String getAllText() {
    String out = "";
    for (int i = 0; i < this.lines.size(); i++) {
      Line line = this.lines.get(i);
      out += line.getCachedFullText();
      if (i != this.lines.size() - 1) {
        out += '\n';
      }
    }

    return out;
  }

  private boolean keyDown(InputEvent event, int keycode) {
    resetBlink();

    if (isFocused()) {
      boolean repeat = false;
      boolean ctrl = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT);
      boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

      if (keycode == Input.Keys.LEFT) {
        if (shift) {
          caret.startSelection();
        }
        if (ctrl) {
          caret.moveByWordInLeft();
        } else {
          if (caret.haveSelection() && !shift) {
            caret.clearSelection();
          } else {
            caret.moveOneCharLeft();
          }
        }

        repeat = true;
      }

      if (keycode == Input.Keys.RIGHT) {
        if (shift) {
          caret.startSelection();
        }
        if (ctrl) {
          caret.moveByWordInRight();
        } else {
          if (caret.haveSelection() && !shift) {
            caret.clearSelection();
          } else {
            caret.moveOneCharRight();
          }
        }

        repeat = true;
      }

      if (keycode == Input.Keys.UP && caret.getRow() > 0) {
        if (shift) {
          caret.startSelection();
        } else {
          caret.clearSelection();
        }
        caret.moveRowUp();
        repeat = true;
      }

      if (keycode == Input.Keys.DOWN && caret.getRow() < this.lines.size() - 1) {
        if (shift) {
          caret.startSelection();
        } else {
          caret.clearSelection();
        }
        caret.moveRowDown();
        repeat = true;
      }

      if (repeat && (!keyRepeatTask.isScheduled() || keyRepeatTask.keycode != keycode)) {
        keyRepeatTask.keycode = keycode;
        keyRepeatTask.cancel();
        Timer.schedule(keyRepeatTask, keyRepeatInitialTime, keyRepeatTime);
      }

      return true;
    } else {
      return false;
    }
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
    caret.setCursorPosition(0,0);
  }

  private void updateSize() {
    this.height = this.lines.size() * (this.style.font.getLineHeight() + LINE_PADDING) + PADDING_VERITICAL * 2;
    this.width  = longestLineLength * this.style.font.getSpaceWidth() + PADDING_HORIZONTAL * 2 + GUTTER_WIDTTH + GUTTER_PADDING;

    this.scroll.invalidate();
    this.invalidate();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Stage stage = getStage();

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

      if (isFocused() && line.getLineNumber() == caret.getRow()+1) {
        style.focusedLineBackround.draw(batch, 0, linePosY - PADDING_VERITICAL, totalWidth, lineHeight);
      }
    }
    
    if (isFocused()) {
      blink();

      if (cursorOn) {
        float caretY = gy - ((caret.getRow()) * lineHeight) - PADDING_VERITICAL*2;
        style.cursor.draw(batch, gx + GUTTER_PADDING + (caret.getCol()) * style.font.getSpaceWidth(), caretY, CARET_WIDTH, lineHeight);
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
