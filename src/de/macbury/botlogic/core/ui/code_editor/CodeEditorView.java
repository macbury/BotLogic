package de.macbury.botlogic.core.ui.code_editor;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.ui.code_editor.widget.CodeEditorTextArea;

/**
 * Created by macbury on 06.04.14.
 */
public class CodeEditorView extends ScrollPane {

  private CodeEditorTextArea textArea;

  public CodeEditorView() {
    super(null, BotLogic.skin.codeEditorScroll);

    this.setFadeScrollBars(false);
    this.setFlickScroll(false);

    this.textArea = new CodeEditorTextArea(BotLogic.skin.codeEditorArea, this);
    this.setWidget(textArea);
  }

  public void setText(String text) {
    this.textArea.setText(text);
  }

  public void focus() {
    getStage().setScrollFocus(this);
    getStage().setKeyboardFocus(textArea);
    textArea.resetBlink();
  }
}
