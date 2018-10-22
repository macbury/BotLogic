package de.macbury.botlogic.core.ui.labels;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.StringBuilder;
/**
 * Created by macbury on 08.04.14.
 */
public class TimerLabel extends Label {
  private static final char TIME_DELIMETER = ':';
  private static final char MILI_DELIMETER = ';';
  private StringBuilder stringBuilder;
  private boolean running = false;
  private double time       = 0;

  double secondsInMilli = 1;
  double minutesInMilli = secondsInMilli * 60;

  public TimerLabel(LabelStyle style) {
    super("00:00;00", style);
    this.stringBuilder = new StringBuilder(16);
  }

  public void tick(double delta, float speed) {
    if (running) {
      time += delta * speed;
      double different    = time;

      double elapsedMinutes = different / minutesInMilli;
      different = different % minutesInMilli;

      double elapsedSeconds = different / secondsInMilli;

      stringBuilder.delete(0, stringBuilder.capacity());
      if (elapsedMinutes < 10.0) {
        stringBuilder.append(0);
      }
      stringBuilder.append((int)elapsedMinutes);
      stringBuilder.append(TIME_DELIMETER);
      if (elapsedSeconds < 10.0) {
        stringBuilder.append(0);
      }

      stringBuilder.append((int)elapsedSeconds);

      int miliseconds = (int)(different % 1.0 * 100);
      stringBuilder.append(MILI_DELIMETER);

      if (miliseconds < 10.0) {
        stringBuilder.append(0);
      }

      stringBuilder.append(miliseconds);
      this.setText(stringBuilder);
    }
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
    if (running) {
      time = 0;
    }
  }
}
