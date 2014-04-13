package de.macbury.botlogic.core.entites;

import de.macbury.botlogic.core.BotLogic;

/**
 * Created by macbury on 11.04.14.
 */
public class EntityManager {

  public RobotEntity robot() {
    return new RobotEntity(BotLogic.models.robotModel);
  }

  public LedEntity led() {
    return new LedEntity(BotLogic.models.ledModel);
  }
}
