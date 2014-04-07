package de.macbury.botlogic.core.ui.code_editor.widget;

import de.macbury.botlogic.core.ui.code_editor.js.JavaScriptScanner;

import java.util.ArrayList;

public class Line extends ArrayList<Element> {
  private String cachedFullText = "";
  private int lineNumber;
  private String lineString;

  public int textLenght() {
    return text().length();
  }

  public String text() {
    String s = "";
    for (Element e : this) {
      s += e.text;
    }
    return s;
  }

  public char charAt(int col) {
    try {
      return text().charAt(col);
    } catch(StringIndexOutOfBoundsException e) {
      return ' ';
    }
  }

  public void buildString() {
    setCachedFullText(text());
  }

  public String getCachedFullText() {
    return cachedFullText;
  }

  public void setCachedFullText(String cachedFullText) {
    this.cachedFullText = cachedFullText;
  }

  public int getPadding() {
    int sum = 0;
    for (int i = 0; i < this.size(); i++) {
      Element e = this.get(i);
      if (e.kind == JavaScriptScanner.Kind.NEWLINE) {
        continue;
      } else if (e.kind == JavaScriptScanner.Kind.NORMAL) {
        sum += e.countSpaces();
      } else {
        break;
      }
    }

    return sum;
  }

  public void setLineNumber(int lineNumber) {
    this.lineNumber = lineNumber;
    this.lineString = Integer.toString(lineNumber);
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getLineString() {
    return this.lineString;
  }
}
