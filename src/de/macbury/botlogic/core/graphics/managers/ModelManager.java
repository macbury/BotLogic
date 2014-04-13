package de.macbury.botlogic.core.graphics.managers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;

/**
 * Created by macbury on 11.04.14.
 */
public class ModelManager {
  public Model ledModel;
  public Model robotModel;

  public ModelManager() {
    G3dModelLoader modelLoader = new G3dModelLoader(new UBJsonReader());
    this.robotModel            = modelLoader.loadModel(Gdx.files.getFileHandle("models/bot.g3db", Files.FileType.Internal));
    //TextureAttribute attr = (TextureAttribute) robotModel.materials.first().get(TextureAttribute.Diffuse);
    //attr.textureDescription.minFilter = Texture.TextureFilter.Nearest;
    //attr.textureDescription.magFilter = Texture.TextureFilter.Nearest;

    this.ledModel              = modelLoader.loadModel(Gdx.files.getFileHandle("models/led.g3db", Files.FileType.Internal));
  }
}
