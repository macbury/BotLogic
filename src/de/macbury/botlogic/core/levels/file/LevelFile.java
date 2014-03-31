package de.macbury.botlogic.core.levels.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by macbury on 31.03.14.
 */
@Root
public class LevelFile {

  public enum LevelFeature {
    Scanner, Robot, Led
  }
  @Element
  private String name;
  @Element
  private String description;
  @Element
  private String map;
  @Element
  private String script;

  private String filePath;

  @ElementList(inline=true, name="using", entry="using")
  private List<LevelFeature> using = new ArrayList<LevelFeature>();

  public static LevelFile load(String path) throws Exception {
    Serializer serializer = new Persister();
    LevelFile levelFile = serializer.read(LevelFile.class, Gdx.files.internal(path).file());
    levelFile.setFilePath(path);
    return levelFile;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public void use(LevelFeature feature) {
    if (!using.contains(feature)) {
      using.add(feature);
    }
  }

  public void save(String path) {
    Persister persister = new Persister();
    try {
      persister.write(this, Gdx.files.internal("maps/"+path+".level").file());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public FileHandle geometryFile() {
    return Gdx.files.internal("maps/"+map);
  }

  public static ArrayList<LevelFile> list() {
    ArrayList<LevelFile> results = new ArrayList<LevelFile>();
    File[] files = (new File(Gdx.files.internal("maps/").path())).listFiles();

    for (File file : files) {
      if (file.isFile() && file.getName().contains(".level")) {
        try {
          results.add(LevelFile.load(file.getPath()));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return results;
  }
}
