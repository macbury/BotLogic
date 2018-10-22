package de.macbury.botlogic.core.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.BotLogicDebug;

/**
 * Created by macbury on 09.04.14.
 */
public class ErrorDialog extends Dialog {
  private static final String TAG = "ErrorDialog";
  private static final float PADDING_VERITICAL  = 10;
  private static final float PADDING_HORIZONTAL = 20;
  private static final float PADDING_BUTTONS_BOTTOM = 25;
  private TextButton okButton;
  public ErrorDialog(WindowStyle windowStyle, String content) {
    super("", windowStyle);

    this.okButton   = BotLogic.skin.builder.redTextButton("OK");

    okButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        hide();
        BotLogic.audio.click.play();
      }
    });

    this.row();
    this.add(BotLogic.skin.builder.titleLabel("BŁĄÐ GRY!")).fillX().expandX().pad(PADDING_VERITICAL, PADDING_HORIZONTAL, PADDING_HORIZONTAL, PADDING_VERITICAL).colspan(4);

    this.row();
    this.add(BotLogic.skin.builder.normalLabel(content)).colspan(4).fillX().padLeft(PADDING_BUTTONS_BOTTOM).padRight(PADDING_BUTTONS_BOTTOM);

    this.row();
    this.add().expandX().padLeft(PADDING_BUTTONS_BOTTOM);
    this.add(okButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20).colspan(2);
    this.add().expandX().padRight(PADDING_BUTTONS_BOTTOM);

    if (BotLogicDebug.TABLE)
      this.debug();
  }

}
