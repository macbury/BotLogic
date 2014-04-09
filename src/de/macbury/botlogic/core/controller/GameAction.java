package de.macbury.botlogic.core.controller;

import de.macbury.botlogic.core.screens.level.GameLevelScreen;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class GameAction {
  protected GameLevelScreen level;
  private GameController controller;

  public GameLevelScreen getLevel() {
    return level;
  }

  public void setController(GameController controller) {
    this.controller = controller;
  }

  public enum GameActionState {
    Pending,
    Running,
    Done
  }

  private GameActionState state;

  public GameAction() {
    this.state = GameActionState.Pending;
  }

  public void start() {
    this.state = GameActionState.Running;
    onStart();
  }

  public abstract void onStart();
  public abstract void update(double delta);
  public abstract void onEnd();

  public void setLevel(GameLevelScreen level) {
    this.level = level;
  }

  public boolean isDone() {
    return this.state == GameActionState.Done && (controller == null || controller.getCurrentAction() == this);
  }

  public void finish() {
    if (this.state != GameActionState.Done) {
      this.state = GameActionState.Done;
      this.controller.setAction(null);
      onEnd();
      level = null;
      controller = null;
    }
  }

  public void waitUntilDone() {
    while(!isDone()) {
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
        finish();
      }
    }
  }
}
