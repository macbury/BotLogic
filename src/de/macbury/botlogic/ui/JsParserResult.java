package de.macbury.botlogic.ui;

import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.Parser;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;

import java.util.List;

/**
 * Created by macbury on 29.03.14.
 */
public class JsParserResult implements ParseResult {
  private Exception exception;
  private int firstLine;
  private int lastLine;
  private List<ParserNotice> notices;
  private AbstractParser parser;
  private long parseTime;

  public JsParserResult(Exception exception, int firstLine, int lastLine,
          List<ParserNotice> notices, AbstractParser parser,
          long parseTime) {
    this.exception = exception;
    this.firstLine = firstLine;
    this.lastLine = lastLine;
    this.notices = notices;
    this.parser = parser;
    this.parseTime = parseTime;
  }

  @Override
  public Exception getError() {
    return exception;
  }

  @Override
  public int getFirstLineParsed() {
    return firstLine;
  }

  @Override
  public int getLastLineParsed() {
    return lastLine;
  }

  @Override
  public List<ParserNotice> getNotices() {
    return notices;
  }

  @Override
  public Parser getParser() {
    return parser;
  }

  @Override
  public long getParseTime() {
    return parseTime;
  }
}
