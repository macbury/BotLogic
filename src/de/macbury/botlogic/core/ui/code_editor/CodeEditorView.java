package de.macbury.botlogic.core.ui.code_editor;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.ui.code_editor.widget.CodeEditorTextArea;

/**
 * Created by macbury on 06.04.14.
 */
public class CodeEditorView extends ScrollPane {

  private CodeEditorTextArea textArea;
  private String text;

  public CodeEditorView() {
    super(null, BotLogic.skin.codeEditorScroll);

    this.setFadeScrollBars(false);
    this.setFlickScroll(false);

    this.textArea = new CodeEditorTextArea(BotLogic.skin.codeEditorArea, this);
    this.setWidget(textArea);
  }

  public CodeEditorTextArea getTextArea() {
    return textArea;
  }

  public void setText(String text) {
    this.textArea.setText(text);
  }

  public void focus() {
    getStage().setScrollFocus(this);
    getStage().setKeyboardFocus(textArea);
    textArea.resetBlink();
  }

  public void unfocus() {
    getStage().unfocus(this);
    getStage().unfocus(textArea);
    BotLogic.skin.setPointerCursor();
  }

  public String getText() {
    return textArea.getAllText();
  }
}
