package de.macbury.botlogic.ui;

import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;

import java.awt.*;

/**
 * Created by macbury on 29.03.14.
 */
public class JsParserNotice implements ParserNotice {

  private final String text;
  private final int line;

  public JsParserNotice(int line, String text) {
    this.line = line;
    this.text = text;
  }

  @Override
  public boolean containsPosition(int pos) {
    return true;
  }

  @Override
  public Color getColor() {
    return Color.red;
  }

  @Override
  public int getLength() {
    return 0;
  }

  @Override
  public int getLevel() {
    return 1;
  }

  @Override
  public int getLine() {
    return line;
  }

  @Override
  public String getMessage() {
    return text;
  }

  @Override
  public int getOffset() {
    return 0;
  }

  @Override
  public Parser getParser() {
    return null;
  }

  @Override
  public boolean getShowInEditor() {
    return true;
  }

  @Override
  public String getToolTipText() {
    return text;
  }

  @Override
  public int compareTo(ParserNotice o) {
    if (o instanceof JsParserNotice) {
      JsParserNotice other = (JsParserNotice) o;
      if (this.line == other.line) {
        return 0;
      } else {
        return this.line > other.line ? 1 : -1;
      }
    }
    return -1;
  }
}
