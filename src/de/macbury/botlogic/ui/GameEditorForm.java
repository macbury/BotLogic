package de.macbury.botlogic.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.GameManager;
import de.macbury.botlogic.core.runtime.ScriptRunner;
import de.macbury.botlogic.core.runtime.ScriptRuntimeListener;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.mozilla.javascript.RhinoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by macbury on 27.03.14.
 */
public class GameEditorForm implements ScriptRuntimeListener {
  private static final String TAG = "GameEditorForm";
  private JMenu menuTryb;
  private ButtonGroup trybGroup;
  private JRadioButtonMenuItem menuTrybSandBox;
  private JPanel rootPanel;
  private JButton runButton;
  private JButton stopButton;
  private RTextScrollPane editorScrollPane;
  private JPanel openglContainer;
  private JPanel editorContainer;
  private JPanel leftCodeEditor;
  private RSyntaxTextArea textArea;
  private JMenuBar menu;
  private JMenu menuSzkic;
  private JMenuItem menuNewItem;
  private JMenuItem menuOpenItem;
  private JMenuItem menuSaveItem;
  private JMenu menuExamples;
  private JMenuItem menuItem4;
  private JMenuItem menuExitItem;
  private JMenu menuRobot;
  private JMenuItem menuRunItem;
  private JMenuItem menuStopItem;
  private JMenu menuPomoc;
  private JMenuItem menuAboutItem;
  private LwjglCanvas gameCanvas;

  private DocumentState currentState = DocumentState.New;
  private Gutter gutter;
  private JavascriptErrorParser parser;
  private EditorFrame frame;

  public enum DocumentState {
    New, Changed, Running
  }

  private void setupToolbar() {
    runButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        runCode();
      }
    });
    stopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        stopCode();
      }
    });
  }

  private void setupTextArea() {
    RSyntaxTextArea.setTemplatesEnabled(true);

    this.textArea = new RSyntaxTextArea();
    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
    textArea.setCodeFoldingEnabled(true);
    textArea.setAntiAliasingEnabled(true);
    textArea.setTabSize(2);
    textArea.setAutoIndentEnabled(true);
    textArea.setTabsEmulated(true);
    //textArea.setMarkOccurrences(true);
    textArea.setBracketMatchingEnabled(true);
    textArea.setCloseCurlyBraces(true);
    textArea.setBorder(BorderFactory.createEmptyBorder());
    editorScrollPane = new RTextScrollPane(textArea);
    editorScrollPane.setFoldIndicatorEnabled(true);
    editorScrollPane.setBorder(BorderFactory.createEmptyBorder());
    ToolTipManager.sharedInstance().registerComponent(textArea);
    try {
      Theme theme = Theme.load(GameEditorForm.class.getResourceAsStream("dark.xml"));
      //theme.apply(textArea);
    } catch (IOException e) {
      e.printStackTrace();
    }

    CompletionProvider provider = createCompletionProvider();

    AutoCompletion ac = new AutoCompletion(provider);
    ac.setAutoActivationEnabled(true);
    ac.setAutoActivationDelay(50);
    ac.setAutoCompleteEnabled(true);

    ac.install(textArea);

    editorContainer.setLayout(new BorderLayout());
    editorContainer.add(editorScrollPane, BorderLayout.CENTER);
    ErrorStrip es = new ErrorStrip(textArea);
    editorContainer.add(es, BorderLayout.LINE_END);
    textArea.requestFocus();

    gutter = editorScrollPane.getGutter();
    gutter.setBookmarkingEnabled(true);

    parser = new JavascriptErrorParser();
    parser.setEnabled(false);

    textArea.addParser(parser);


  }

  private void setupMenu() {
    menu = new JMenuBar();
    menuSzkic = new JMenu();
    menuTryb  = new JMenu();
    menuTrybSandBox = new JRadioButtonMenuItem();
    menuNewItem = new JMenuItem();
    menuOpenItem = new JMenuItem();
    menuSaveItem = new JMenuItem();
    menuExamples = new JMenu();
    menuItem4 = new JMenuItem();
    menuExitItem = new JMenuItem();
    menuRobot = new JMenu();
    menuRunItem = new JMenuItem();
    menuStopItem = new JMenuItem();
    menuPomoc = new JMenu();
    menuAboutItem = new JMenuItem();
    menu.setMargin(new Insets(5,5,10,5));

    //======== menu ========
    {

      //======== menuSzkic ========
      {
        menuSzkic.setText("Szkic");

        //---- menuNewItem ----
        menuNewItem.setText("Nowy");
        menuNewItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        menuSzkic.add(menuNewItem);

        //---- menuOpenItem ----
        menuOpenItem.setText("Załaduj");
        menuOpenItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        menuSzkic.add(menuOpenItem);

        //---- menuSaveItem ----
        menuSaveItem.setText("Zapisz");
        menuSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
        menuSzkic.add(menuSaveItem);
        menuSzkic.addSeparator();

        //======== menuExamples ========
        {
          menuExamples.setText("Przykłady");

          //---- menuItem4 ----
          menuItem4.setText("Otwórz folder");
          menuExamples.add(menuItem4);
          menuExamples.addSeparator();
        }
        menuSzkic.add(menuExamples);

        //---- menuExitItem ----
        menuExitItem.setText("Zamknij");
        menuExitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuSzkic.add(menuExitItem);
      }
      menu.add(menuSzkic);

      menuTryb.setText("Tryb");

      trybGroup = new ButtonGroup();

      menuTrybSandBox.setText("Zabawa");
      menuTrybSandBox.setSelected(true);
      trybGroup.add(menuTrybSandBox);
      menuTryb.add(menuTrybSandBox);
      menu.add(menuTryb);

      //======== menuRobot ========
      {
        menuRobot.setText("Robot");

        //---- menuRunItem ----
        menuRunItem.setText("Uruchom");
        menuRunItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        menuRobot.add(menuRunItem);

        //---- menuStopItem ----
        menuStopItem.setText("Wstrzymaj");
        menuStopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
        menuRobot.add(menuStopItem);
      }
      menu.add(menuRobot);

      //======== menuPomoc ========
      {
        menuPomoc.setText("Pomoc");

        //---- menuAboutItem ----
        menuAboutItem.setText("O programie");
        menuAboutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        menuPomoc.add(menuAboutItem);
      }
      menu.add(menuPomoc);
    }

    menuNewItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        newDocument();
      }
    });
  }

  private CompletionProvider createCompletionProvider() {
    DefaultCompletionProvider provider = new DefaultCompletionProvider();

    // Add completions for all Java keywords. A BasicCompletion is just
    // a straightforward word completion.
    provider.addCompletion(new BasicCompletion(provider, "abstract"));
    provider.addCompletion(new BasicCompletion(provider, "assert"));
    provider.addCompletion(new BasicCompletion(provider, "break"));
    provider.addCompletion(new BasicCompletion(provider, "case"));
    provider.addCompletion(new BasicCompletion(provider, "catch"));
    provider.addCompletion(new BasicCompletion(provider, "class"));
    provider.addCompletion(new BasicCompletion(provider, "const"));
    provider.addCompletion(new BasicCompletion(provider, "continue"));
    provider.addCompletion(new BasicCompletion(provider, "default"));
    provider.addCompletion(new BasicCompletion(provider, "do"));
    provider.addCompletion(new BasicCompletion(provider, "else"));
    provider.addCompletion(new BasicCompletion(provider, "enum"));
    provider.addCompletion(new BasicCompletion(provider, "extends"));
    provider.addCompletion(new BasicCompletion(provider, "final"));
    provider.addCompletion(new BasicCompletion(provider, "finally"));
    provider.addCompletion(new BasicCompletion(provider, "for"));
    provider.addCompletion(new BasicCompletion(provider, "goto"));
    provider.addCompletion(new BasicCompletion(provider, "if"));
    provider.addCompletion(new BasicCompletion(provider, "implements"));
    provider.addCompletion(new BasicCompletion(provider, "import"));
    provider.addCompletion(new BasicCompletion(provider, "instanceof"));
    provider.addCompletion(new BasicCompletion(provider, "interface"));
    provider.addCompletion(new BasicCompletion(provider, "native"));
    provider.addCompletion(new BasicCompletion(provider, "new"));
    provider.addCompletion(new BasicCompletion(provider, "package"));
    provider.addCompletion(new BasicCompletion(provider, "private"));
    provider.addCompletion(new BasicCompletion(provider, "protected"));
    provider.addCompletion(new BasicCompletion(provider, "public"));
    provider.addCompletion(new BasicCompletion(provider, "return"));
    provider.addCompletion(new BasicCompletion(provider, "static"));
    provider.addCompletion(new BasicCompletion(provider, "strictfp"));
    provider.addCompletion(new BasicCompletion(provider, "super"));
    provider.addCompletion(new BasicCompletion(provider, "switch"));
    provider.addCompletion(new BasicCompletion(provider, "synchronized"));
    provider.addCompletion(new BasicCompletion(provider, "this"));
    provider.addCompletion(new BasicCompletion(provider, "throw"));
    provider.addCompletion(new BasicCompletion(provider, "throws"));
    provider.addCompletion(new BasicCompletion(provider, "transient"));
    provider.addCompletion(new BasicCompletion(provider, "try"));
    provider.addCompletion(new BasicCompletion(provider, "void"));
    provider.addCompletion(new BasicCompletion(provider, "volatile"));
    provider.addCompletion(new BasicCompletion(provider, "while"));
    provider.addCompletion(new FunctionCompletion(provider, "name", "name"));
    // Add a couple of "shorthand" completions. These completions don't
    // require the input text to be the same thing as the replacement text.
    provider.addCompletion(new ShorthandCompletion(provider, "sysout",
            "System.out.println(", "System.out.println("));
    provider.addCompletion(new ShorthandCompletion(provider, "syserr",
            "System.err.println(", "System.err.println("));
    provider.addCompletion(new VariableCompletion(provider, "robot", "Robot"));
    return provider;
  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  public JMenuBar getMenu() {
    return menu;
  }

  public void bind(EditorFrame editorFrame, GameManager game, LwjglApplicationConfiguration config) {
    setupMenu();
    setupTextArea();
    setupToolbar();
    editorFrame.setJMenuBar(menu);
    this.frame = editorFrame;
    this.gameCanvas = new LwjglCanvas(game, config);
    Canvas canvas = this.gameCanvas.getCanvas();
    openglContainer.setLayout(new BorderLayout());
    openglContainer.add(canvas, BorderLayout.CENTER);
    //canvas.requestFocus();

    editorFrame.addWindowListener(new ExitListener(gameCanvas));
    newDocument();
  }

  // methods

  public void setState(DocumentState state) {
    currentState = state;
    updateUI();
  }

  private void updateUI() {
    if (currentState == DocumentState.Running) {
      runButton.setEnabled(false);
      stopButton.setEnabled(true);
      textArea.setEditable(false);
      menu.setEnabled(false);
      gameCanvas.getCanvas().requestFocus();
    } else {
      runButton.setEnabled(true);
      stopButton.setEnabled(false);
      textArea.setEditable(true);
      menu.setEnabled(true);
      textArea.requestFocus();
    }
  }

  private void runCode() {
    BotLogic.game.getLevel().getScriptRunner().execute("Robot", textArea.getText());
  }

  private void stopCode() {
    BotLogic.game.getLevel().getScriptRunner().finish();
  }

  private void newDocument() {
    if (currentState != DocumentState.New) {
      closeDocument();
    }

    textArea.setText(Gdx.files.classpath("de/macbury/botlogic/code/base.js").readString());
    textArea.requestFocusInWindow();
    BotLogic.game.newGame();
    BotLogic.game.getLevel().getScriptRunner().addListener(this);
    setState(DocumentState.New);
    clearErrors();
  }

  private void clearErrors() {
    Gdx.app.log(TAG, "Clearing errors!");
    textArea.removeAllLineHighlights();
    gutter.removeAllTrackingIcons();

    parser.clear();
    textArea.forceReparsing(parser);
  }

  private void closeDocument() {
    BotLogic.game.getLevel().getScriptRunner().removeListener(this);
  }

  @Override
  public void onScriptStart(ScriptRunner runner) {
    Gdx.app.log(TAG, "onScriptStart");
    clearErrors();
    setState(DocumentState.Running);
  }

  @Override
  public void onScriptYield(ScriptRunner runner) {
    Gdx.app.log(TAG, "onScriptYield");
  }

  @Override
  public void onScriptInterput(ScriptRunner runner) {
    Gdx.app.log(TAG, "onScriptInterput");
  }

  @Override
  public void onScriptError(ScriptRunner runner, RhinoException error) {
    Gdx.app.log(TAG, "onScriptError"+ error.getMessage());
    Gdx.app.log(TAG, "Line" + error.lineNumber());

    parser.add(error.lineNumber(), error.details());
    textArea.forceReparsing(parser);

    JOptionPane.showMessageDialog(frame,
            "Bląd w lini: " + error.lineNumber() +"\n"+error.details(),
            "Syntax Error",
            JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void onScriptFinish(ScriptRunner runner) {
    Gdx.app.log(TAG, "onScriptFinish");
    setState(DocumentState.Changed);
  }

  public class JavascriptErrorParser extends AbstractParser {
    private ArrayList<ParserNotice> notices;

    public JavascriptErrorParser() {
      this.notices = new ArrayList<ParserNotice>();
    }

    public void add(int line, String text) {
      notices.add(new JsParserNotice(line, text));
    }

    @Override
    public ParseResult parse(RSyntaxDocument doc, String style) {
      return new JsParserResult(null, 0, doc.getDefaultRootElement().getElementCount(), notices, this, 0);
    }

    public void clear() {
      notices.clear();
    }
  }
}
