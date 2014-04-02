package de.macbury.botlogic.core.api;

/**
 * Created by macbury on 01.04.14.
 */
public class ScriptDocument {
  private String content;
  private String levelPath;
  private String filePath;
  private boolean changed = false;


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
    changed = true;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public String getLevelPath() {
    return levelPath;
  }

  public void setLevelPath(String levelPath) {
    this.levelPath = levelPath;
  }

  public boolean isSaved() {
    return filePath != null;
  }
}
