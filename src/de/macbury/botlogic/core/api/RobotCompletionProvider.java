package de.macbury.botlogic.core.api;

import de.macbury.botlogic.core.screens.level.GameLevelScreen;
import org.fife.ui.autocomplete.*;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotCompletionProvider extends DefaultCompletionProvider {
  public RobotCompletionProvider(GameLevelScreen level) {
    super();
    registerFunction("say", "pokazuje tekst podany w parametrze");
    registerAuto();

    addCompletion(new BasicCompletion(this, "robot", "robot.test", "test robot"));
    addCompletion(new ShorthandCompletion(this, "robot.say", "robot.say"));
  }

  public void registerFunction(String name, String description) {
    FunctionCompletion completion = new FunctionCompletion(this, name, "SSSS");
    completion.setSummary("Summary");
    completion.setDefinedIn("robot");
    completion.setShortDescription("short description");
    addCompletion(completion);
  }

  public void registerAuto() {
    VariableCompletion completion = new VariableCompletion(this, "robot", "");
    addCompletion(completion);
  }
}
