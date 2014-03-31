package de.macbury.botlogic.core.controller;

/**
 * Created by macbury on 31.03.14.
 */
public abstract class GameAction {

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
  public boolean isDone() {
    return this.state == GameActionState.Done;
  }

  public void finish() {
    if (this.state != GameActionState.Done) {
      this.state = GameActionState.Done;
      onEnd();
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
