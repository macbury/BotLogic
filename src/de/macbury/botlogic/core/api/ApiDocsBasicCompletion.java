package de.macbury.botlogic.core.api;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by macbury on 01.04.14.
 */
@Root
public class ApiDocsBasicCompletion {
  @Element
  public String name;


}
