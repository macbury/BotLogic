package de.macbury.botlogic.core.api;

import de.macbury.botlogic.core.levels.BaseLevel;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.VariableCompletion;

/**
 * Created by macbury on 31.03.14.
 */
public class RobotCompletionProvider extends DefaultCompletionProvider {
  public RobotCompletionProvider(BaseLevel level) {
    super();
    registerFunction("say", "pokazuje tekst podany w parametrze");
    registerAuto();
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
