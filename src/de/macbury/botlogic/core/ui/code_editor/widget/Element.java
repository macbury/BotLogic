package de.macbury.botlogic.core.ui.code_editor.widget;

import de.macbury.botlogic.core.ui.code_editor.js.JavaScriptScanner;

public class Element {
  public JavaScriptScanner.Kind kind;
  public String text;

  public Element(JavaScriptScanner.Kind k, String t) {
    text = t;
    kind = k;
  }

  public int countSpaces() {
    int sum = 0;

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);
      if (c == ' ') {
        sum += 1;
      } else {
        break;
      }
    }

    return sum;
  }
}