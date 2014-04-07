package de.macbury.botlogic.core.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.macbury.botlogic.core.BotLogic;

/**
 * Created by macbury on 05.04.14.
 */
public class ConfigManager {
  private Preferences config;
  private int outlineQualityCache = -1;
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

  public int getOutlineQuality() {
    if (outlineQualityCache == -1) {
      outlineQualityCache = config.getInteger("outline_quality", 1);
    }
    return outlineQualityCache;
  }

  public void setOutlineQuality(int quality) {
    outlineQualityCache = -1;
    config.putInteger("outline_quality", quality);
    config.flush();
  }

  public void setMusicVolume(float volume) {
    config.putFloat("music_volume", volume);
    config.flush();
  }

  public void load() {
    loadResolution();
    loadAudio();
  }

  public void loadAudio() {
    BotLogic.audio.mainMenuMusic.setVolume(getMusicVolume());
    BotLogic.audio.gameMusic.setVolume(getMusicVolume());
  }

  public float getMusicVolume() {
    return config.getFloat("music_volume", 0.6f);
  }
}
