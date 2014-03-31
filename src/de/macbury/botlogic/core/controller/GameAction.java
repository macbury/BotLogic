package de.macbury.botlogic.core.controller;

import de.macbury.botlogic.core.levels.BaseLevel;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class GameAction {
  protected BaseLevel level;

  public BaseLevel getLevel() {
    return level;
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

  public void setLevel(BaseLevel level) {
    this.level = level;
  }

  public boolean isDone() {
    return this.state == GameActionState.Done;
  }

  public void finish() {
    if (this.state != GameActionState.Done) {
      this.state = GameActionState.Done;
      onEnd();
      level = null;
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
