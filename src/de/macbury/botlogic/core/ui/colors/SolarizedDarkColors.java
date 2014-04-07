package de.macbury.botlogic.core.ui.colors;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by macbury on 06.04.14.
 */
public class SolarizedDarkColors {
  public static Color TEXT        = rgb(147, 161, 161);
  public static Color LINE_NUMBER = rgb(208, 237, 247);
  public static Color KEYWORD     = rgb(133, 153, 0);
  public static Color NUMBER      = rgb(211, 54, 130);
  public static Color STRING      = rgb(42, 161, 152);
  public static Color COMMENT     = rgb(101, 123, 131);
  public static Color SPECIAL_KEYWORD = rgb(38, 139, 210);

  public static Color rgb(float r, float g, float b) {
    return new Color(r / 255f, g / 255f, b / 255f, 1);
  }
}
