package de.macbury.botlogic.core.runtime.ext;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

/**
 * Created by macbury on 28.03.14.
 */
public class ScriptContext extends Context {
  private boolean interrputFlag = false;

  protected ScriptContext(ContextFactory factory) {
    super(factory);
    setInstructionObserverThreshold(5);
  }

  public boolean isInterrputFlag() {
    return interrputFlag;
  }

  public void setInterrputFlag(boolean nif) {
    this.interrputFlag = nif;
  }

  public void interrputScript() {
    setInterrputFlag(true);
  }

  @Override
  protected void observeInstructionCount(int instructionCount) {
    if(isInterrputFlag()) {
      throw new ScriptInterputException();
    } else {
      super.observeInstructionCount(instructionCount);
    }
  }
}
