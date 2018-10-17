package de.macbury.botlogic.core.runtime.ext;


import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

/**
 * Created by macbury on 28.03.14.
 */
public class ScriptContextFactory extends ContextFactory {

  @Override
  protected Context makeContext() {
    ScriptContext context = new ScriptContext(this);
    return context;
  }

}
