package de.macbury.botlogic.core.ui.button;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by macbury on 08.04.14.
 */
public class StateImageButton extends ImageButton {
  private Drawable second;
  private Drawable first;

  public StateImageButton(ImageButtonStyle style, Drawable first, Drawable second) {
    super(style);
    this.first  = first;
    this.second = second;
    setOn(false);
  }

  public void setOn(boolean on) {

    if(on) {
      this.getStyle().imageUp = second;
    } else {
      this.getStyle().imageUp = first;
    }
  }
}
