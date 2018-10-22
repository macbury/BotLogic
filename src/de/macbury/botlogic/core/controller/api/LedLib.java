package de.macbury.botlogic.core.controller.api;

import com.badlogic.gdx.graphics.Color;
import de.macbury.botlogic.core.controller.GameController;
import de.macbury.botlogic.core.controller.actions.LedBlinkAction;

/**
 * Created by macbury on 11.04.14.
 */
public class LedLib extends BaseLib {
  private Color tempColor = new Color();
  public LedLib(GameController controller) {
    super(controller);
  }

  public void on() {
    robot.led.on();
  }

  private void color(Color color) {
    robot.led.setColor(color);
  }

  public void color(float r, float g, float b) {
    color(tempColor.set(r / 255f, g / 255f, b / 255f, 1));
  }

  public void off() {
    robot.led.off();
  }

  public void blink(int times, float delayMili) {
    LedBlinkAction action = new LedBlinkAction(times, delayMili);
    this.controller.setAction(action);
    action.waitUntilDone();
  }

  public void red() {
    color(Color.RED);
  }

  public void blue() {
    color(Color.BLUE);
  }

  public void green() {
    color(Color.GREEN);
  }

  public void yellow() {
    color(Color.YELLOW);
  }

  public void orange() {
    color(Color.ORANGE);
  }

  public void pink() {
    color(Color.PINK);
  }

  public void cyan() {
    color(Color.CYAN);
  }

  @Override
  public void reset() {
    off();
  }

  @Override
  public String getNamespace() {
    return "led";
  }
}
