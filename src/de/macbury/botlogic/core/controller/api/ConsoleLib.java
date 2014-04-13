package de.macbury.botlogic.core.controller.api;

import com.badlogic.gdx.Gdx;
import de.macbury.botlogic.core.controller.GameController;

/**
 * Created by macbury on 11.04.14.
 */
public class ConsoleLib extends BaseLib {
  private static final String TAG = "ConsoleLib";

  public ConsoleLib(GameController controller) {
    super(controller);
  }

  public void log(String content) {
    Gdx.app.log(TAG, content);
  }

  @Override
  public void reset() {

  }

  @Override
  public String getNamespace() {
    return "console";
  }
}
