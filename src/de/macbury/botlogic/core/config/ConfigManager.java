package de.macbury.botlogic.core.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by macbury on 05.04.14.
 */
public class ConfigManager {
  private Preferences config;

  public ConfigManager() {
    this.config = Gdx.app.getPreferences("Settings");
  }

  public void putResolution(int width, int height, boolean fullscreen) {
    config.putInteger("width", width);
    config.putInteger("height", height);
    config.putBoolean("fullscreen", fullscreen);
    config.flush();
  }

  public void loadResolution() {
    Gdx.graphics.setDisplayMode(config.getInteger("width", 1024), config.getInteger("height", 768), config.getBoolean("fullscreen", false));
  }
}
