package de.macbury.botlogic.core.ui.scroll;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;

/**
 * Created by macbury on 05.04.14.
 */
public class ScrollPaneWithoutMouseScroll extends ScrollPane {
  public ScrollPaneWithoutMouseScroll(Actor widget, ScrollPaneStyle style) {
    super(widget, style);
  }

  @Override
  protected float getMouseWheelX() {
    return 0;
  }

  @Override
  protected float getMouseWheelY() {
    return 0;
  }
}
