package de.macbury.botlogic.core.ui.code_editor.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.ui.code_editor.CodeEditorView;
import de.macbury.botlogic.core.ui.code_editor.js.JavaScriptScanner;
import de.macbury.botlogic.core.ui.stage.FlatStage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by macbury on 07.04.14.
 */
public class CodeEditorTextArea extends WidgetGroup {
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
  private static final float EXCEPTION_ICON_LEFT_MARGIN = 10;
  private CodeEditorView scroll;
  private Clipboard clipboard;
  private HashMap<JavaScriptScanner.Kind, Color> styles;
  private ArrayList<Line> lines;

  private CodeEditorTextAreaStyle style;

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

  private int lineWithError = -1;
  private Vector2 tempVector = new Vector2();
  private String lineErrorMessage;

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

    initializeKeyboard();
    initializeCursorArea();
    resetBlink();
    setText("");
  }

  private void initializeCursorArea() {

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
    scroll.addListener(new ClickListener() {
      @Override
      public boolean mouseMoved(InputEvent event, float x, float y) {
        if (isFocused()) {
          if (x <= GUTTER_WIDTTH || (x >= scroll.getWidth() - scroll.getScrollBarWidth()) || (y <= scroll.getHeight() - scroll.getScrollHeight())) {
            BotLogic.skin.setPointerCursor();
          } else {
            BotLogic.skin.setTextCursor();
          }

          FlatStage stage = (FlatStage)getStage();

          if(lineWithError != -1 && (lineToY(lineWithError)+getLineHeight() >= y && lineToY(lineWithError) <= y)) {
            if (!stage.isTooltipVisible()) {
              stage.showTooltip(lineErrorMessage);
            }
          } else {
            stage.hideTooltip();
          }

          return true;
        } else {
          return false;
        }
      }
    });

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

      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (!super.touchDown(event, x, y, pointer, button)) return false;
        if (pointer == 0 && button != 0) return false;
        caret.clearSelection();
        caret.setCursorPosition(xToCol(x) + caret.getColScrollPosition(), yToRow(y) + caret.getRowScrollPosition());
        scroll.focus();
        return true;
      }

      @Override
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        onTouchDraged(event, x,y, pointer);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
      }

      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        if (toActor == null)
          BotLogic.skin.setPointerCursor();
      }
    };
    addListener(inputListener);
  }

  public int xToCol(float x) {
    int c = (int) Math.round((x - GUTTER_WIDTTH - GUTTER_PADDING) / style.font.getSpaceWidth());
    if (c < 0) {
      c = 0;
    }
    return c++;
  }

  public float getLineHeight() {
    return this.style.font.getLineHeight() + LINE_PADDING;
  }

  public int yToRow(float y) {
    float totalHeight  = Math.max(scroll.getHeight(), height);
    int r = (int) Math.round((totalHeight - y) / getLineHeight()) - 1;
    if (r < 0) {
      r = 0;
    }
    return r;
  }

  public void onTouchDraged(InputEvent event, float x, float y, int pointer) {
    lastBlink = 0;
    cursorOn = false;
    int col = xToCol(x) + caret.getColScrollPosition();
    int row = yToRow(y) + caret.getRowScrollPosition();

    int rd = row - caret.getRow();

    boolean moveLeft = col - caret.getCol() <= 0;
    caret.setCursorPosition(col,row);

    if (moveLeft) {
      //updateScrollInLeftDirectionForCol();
    } else {
      //updateScrollInRightDirectionForCol();
    }

    if (rd == 0) {

    } else if(rd < 0) {
      //updateScrollInDownDirectionForRow();
    } else {
      //updateScrollInUpDirectionForRow();
    }

    caret.startSelection();

    updateScroll();
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
      } else if (style.font.getData().hasGlyph(character)) {
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
    updateScroll();
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

      if (ctrl) {
        if (keycode == Input.Keys.V) {
          paste();
          return true;
        }
        if (keycode == Input.Keys.C || keycode == Input.Keys.INSERT) {
          copy();
          return true;
        }
        if (keycode == Input.Keys.X || keycode == Input.Keys.DEL) {
          cut();
          return true;
        }

        if (keycode == Input.Keys.A) {
          caret.clearSelection();
          caret.selectAll();
          return true;
        }

      }

      if (keycode == Input.Keys.PAGE_DOWN) {
        pageDown();
        //updateScrollInDownDirectionForRow();
        repeat = true;
      }

      if (keycode == Input.Keys.PAGE_UP) {
        pageUp();
        //updateScrollInUpDirectionForRow();
        repeat = true;
      }

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

      if (keycode == Input.Keys.HOME) {
        if (shift) {
          caret.startSelection();
        }
        caret.setColHome();
        //updateScrollInRightDirectionForCol();
      }

      if (keycode == Input.Keys.END) {
        if (shift) {
          caret.startSelection();
        }
        caret.setColEnd();
        //updateScrollInLeftDirectionForCol();
      }

      if (repeat && (!keyRepeatTask.isScheduled() || keyRepeatTask.keycode != keycode)) {
        keyRepeatTask.keycode = keycode;
        keyRepeatTask.cancel();
        Timer.schedule(keyRepeatTask, keyRepeatInitialTime, keyRepeatTime);
      }

      updateScroll();

      return true;
    } else {
      return false;
    }
  }

  private void updateScroll() {
    float width   = style.font.getSpaceWidth()+GUTTER_WIDTTH;
    float height  = getLineHeight();
    float x       = (caret.getCol() + 1) * style.font.getSpaceWidth() + GUTTER_WIDTTH;
    if (caret.getCol() <= 1) {
      x = 0;
    }
    scroll.scrollTo(x, getHeight() - PADDING_VERITICAL * 2 - (caret.getRow()) * getLineHeight(), width, height);
  }

  private void pageUp() {
    int mv = caret.getRow() - 1 - visibleLinesCount();
    if (mv < 0) {
      mv = 0;
    }
    caret.clearSelection();
    caret.setRow(mv);
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
    lineWithError = -1;
    parse(this.text);
    resetBlink();
    caret.setCursorPosition(0, 0);
  }

  private void updateSize() {
    this.height = this.lines.size() * (this.style.font.getLineHeight() + LINE_PADDING) + PADDING_VERITICAL * 2;
    this.width  = longestLineLength * this.style.font.getSpaceWidth() + PADDING_HORIZONTAL * 2;
    this.width += GUTTER_WIDTTH + GUTTER_PADDING;

    this.scroll.invalidate();
    this.invalidate();
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (!isFocused()) {
      BotLogic.skin.setPointerCursor();
    }
  }

  public float lineToY(int row) {
    float y      = getY();
    float height = getHeight();

    float parentHeight = getParent().getHeight();

    float totalHeight  = Math.max(parentHeight, height);

    float lineHeight   = this.style.font.getLineHeight() + LINE_PADDING;
    float gy = totalHeight + y;

    return gy - (row * lineHeight) - PADDING_VERITICAL;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
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

    if (caret.haveSelection()) {
      int cursorRowStart = Math.min(caret.getSelectionStartRow(), caret.getRow());
      int cursorRowEnd = Math.max(caret.getRow(), caret.getSelectionStartRow());

      int cursorColEnd    = caret.getCol();
      int cursorColStart  = caret.getSelectionStartCol();
      if (caret.getRow() < caret.getSelectionStartRow()) {
        cursorColEnd    = caret.getSelectionStartCol();
        cursorColStart  = caret.getCol();
      }

      if (cursorRowStart == cursorRowEnd) {
        style.selection.draw(batch, gx + GUTTER_PADDING + cursorColStart * style.font.getSpaceWidth(), lineToY(cursorRowStart) - PADDING_VERITICAL, (cursorColEnd - cursorColStart) * style.font.getSpaceWidth(), lineHeight);
      } else {
        style.selection.draw(batch, gx + GUTTER_PADDING + cursorColStart * style.font.getSpaceWidth(), lineToY(cursorRowStart) - PADDING_VERITICAL, totalWidth, lineHeight);

        int rowCount = Math.abs(cursorRowStart - cursorRowEnd) - 1;
        for (int i = 0; i < rowCount; i++) {
          style.selection.draw(batch, gx + GUTTER_PADDING, lineToY(cursorRowStart+i+1) - PADDING_VERITICAL, totalWidth, lineHeight);
        }

        style.selection.draw(batch, gx + GUTTER_PADDING, lineToY(cursorRowEnd) - PADDING_VERITICAL, cursorColEnd * style.font.getSpaceWidth(), lineHeight);
      }
    }

    for(Line line : lines) {
      float lineElementX  = 0;
      float linePosY      = lineToY(line.getLineNumber() - 1);

      style.font.setColor(style.lineNumberColor);
      GlyphLayout layout = new GlyphLayout(style.font, line.getLineString());
      style.font.draw(batch, line.getLineString(), gx - layout.width - GUTTER_PADDING, linePosY);

      if (line.getLineNumber() == lineWithError) {
        style.exceptionGutterIcon.draw(batch, EXCEPTION_ICON_LEFT_MARGIN, linePosY-style.exceptionGutterIcon.getMinHeight()+style.exceptionGutterIcon.getMinHeight()/4, style.exceptionGutterIcon.getMinWidth(), style.exceptionGutterIcon.getMinHeight());
        style.syntaxErrorLineBackground.draw(batch, GUTTER_WIDTTH, linePosY - PADDING_VERITICAL, totalWidth - GUTTER_WIDTTH, lineHeight);
      }

      for (int lx = 0; lx < line.size(); lx++) {
        Element elem                 = line.get(lx);
        layout = new GlyphLayout(style.font, elem.text);

        if (line.getLineNumber() == lineWithError) {
          style.font.setColor(style.syntaxErrorTextColor);
        } else {
          style.font.setColor(styles.get(elem.kind));
        }

        style.font.draw(batch, elem.text, gx + GUTTER_PADDING + lineElementX, linePosY);

        lineElementX += layout.width;
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

  public void setErrorLine(int line, int col, String message) {
    lineWithError = line+1;
    lineErrorMessage = message;
    caret.setCursorPosition(col, line);
  }

  public void clearError() {
    lineWithError = -1;
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

  public void pageDown() {
    int mv = caret.getRow() - 1 + visibleLinesCount();
    if (mv > this.lines.size() - 1) {
      mv = this.lines.size() - 1;
    }
    caret.clearSelection();
    caret.setRow(mv);
  }

  private void cut() {
    if (caret.haveSelection()) {
      copy();
      delete();
    }
  }

  private void copy() {
    String lineText = getAllText();
    int pos = caret.getCaretPosition();

    if (caret.haveSelection()) {
      int startPos = caret.getSelectionCaretPosition();

      int from = Math.min(pos, startPos);
      int to = Math.max(pos, startPos);

      String copyText = lineText.substring(from, to);
      clipboard.setContents(copyText);
    }
  }

  private void paste() {
    String content = clipboard.getContents();

    if (content != null) {
      insertText(content);
      caret.moveForwardByCharCount(content.length());
      caret.clearSelection();
    }
  }

  private int visibleLinesCount() {
    return (int) (this.getHeight() / getLineHeight());
  }

  public static class CodeEditorTextAreaStyle extends TextField.TextFieldStyle {
    public Drawable lineNumberBackround;
    public Drawable exceptionGutterIcon;
    public Drawable focusedLineBackround;
    public Drawable syntaxErrorLineBackground;

    public Color lineNumberColor;

    public Color syntaxKeywordColor;
    public Color textColor;
    public Color syntaxStringColor;
    public Color syntaxCommentColor;
    public Color syntaxNumberColor;
    public Color syntaxSpecialKeywordColor;

    public Color syntaxErrorTextColor;
  }

  class KeyRepeatTask extends Timer.Task {
    int keycode;

    public void run () {
      inputListener.keyDown(null, keycode);
    }
  }
}
