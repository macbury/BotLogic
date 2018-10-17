package de.macbury.botlogic.core.entites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 11.04.14.
 */
public class LedEntity extends ModelEntity {
  private BlendingAttribute blendingAttribute;
  private ColorAttribute colorAttr;
  public PointLight light;
  private Color ledColor = Color.RED;
  public LedEntity(Model model) {
    super(model);

    light = new PointLight();
    light.set(new Color(1f,1f, 1f, 1f), Vector3.Zero, 2f);

    for (Material material : instance.materials) {

      if (material.id.contains("Led")) {
        this.colorAttr = (ColorAttribute)material.get(ColorAttribute.Diffuse);
        colorAttr.color.set(Color.WHITE);

        this.blendingAttribute = (BlendingAttribute)material.get(BlendingAttribute.Type);
        blendingAttribute.opacity = 1.0f;
        blendingAttribute.sourceFunction = GL20.GL_ONE;
        blendingAttribute.destFunction = GL20.GL_SRC_ALPHA;

      }
    }
  }

  @Override
  public void update(double delta) {
    super.update(delta);
    light.position.set(position).add(0, 0.1f,0);
  }

  public void setColor(Color color) {
    setColor(color, true);
  }

  public void setColor(Color color, boolean replace) {
    colorAttr.color.set(color);
    light.color.set(color);
    if (replace) {
      ledColor.set(color);
    }
  }

  public void on() {
    level.environment.add(light);

    setColor(ledColor, false);
  }

  public void off() {
    setColor(Color.WHITE, false);

    level.environment.remove(light);
  }

  @Override
  public void reset() {
    super.reset();
    off();
  }
}
