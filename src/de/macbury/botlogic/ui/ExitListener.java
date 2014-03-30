package de.macbury.botlogic.ui;

import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by macbury on 27.03.14.
 */
public class ExitListener extends WindowAdapter {

  LwjglCanvas canvas;
  public ExitListener(LwjglCanvas lwjglCanvas) {
    canvas = lwjglCanvas;
  }
  public void windowClosing(WindowEvent e) {
    canvas.stop();
  }
}
