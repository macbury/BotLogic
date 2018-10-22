package de.macbury.botlogic.core.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.BotLogicDebug;

/**
 * Created by macbury on 06.04.14.
 */
public class EndGameDialog extends Dialog {
  private static final String TAG = "EndGameDialog";
  private static final float PADDING_VERITICAL  = 10;
  private static final float PADDING_HORIZONTAL = 20;
  private static final float PADDING_BUTTONS_BOTTOM = 25;
  private TextButton cancelButton;
  private TextButton okButton;
  private EndGameListener listener;
  public EndGameDialog(WindowStyle windowStyle) {
    super("", windowStyle);

    this.okButton   = BotLogic.skin.builder.redTextButton("Tak");
    this.cancelButton = BotLogic.skin.builder.redTextButton("Nie");

    okButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        hide();
        BotLogic.audio.click.play();
        listener.onEndGameDialogOk(EndGameDialog.this);
      }
    });

    cancelButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        hide();
        BotLogic.audio.click.play();
        listener.onEndGameDialogCancel(EndGameDialog.this);
      }
    });

    this.row();
      this.add(BotLogic.skin.builder.titleLabel("Uwaga!")).fillX().expandX().pad(PADDING_VERITICAL, PADDING_HORIZONTAL, PADDING_HORIZONTAL, PADDING_VERITICAL).colspan(4);

    this.row();
      this.add(BotLogic.skin.builder.normalLabel("Czy na pewno chcesz zakończyć tą pasjonującą rozgrywkę?")).colspan(4).fillX().padLeft(PADDING_BUTTONS_BOTTOM).padRight(PADDING_BUTTONS_BOTTOM);

    this.row();
      this.add().expandX().padLeft(PADDING_BUTTONS_BOTTOM);
      this.add(okButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20);
      this.add(cancelButton).width(180).height(52).padTop(PADDING_BUTTONS_BOTTOM).padBottom(20).padLeft(20);
      this.add().expandX().padRight(PADDING_BUTTONS_BOTTOM);

    if (BotLogicDebug.TABLE)
      this.debug();
  }

  public void setListener(EndGameListener listener) {
    this.listener = listener;
  }
}
