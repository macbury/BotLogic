package de.macbury.botlogic.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
import de.macbury.botlogic.core.BotLogic;
import de.macbury.botlogic.core.GameManager;
import de.macbury.botlogic.core.api.RobotCompletionProvider;
import de.macbury.botlogic.core.api.ScriptDocument;
import de.macbury.botlogic.core.screens.level.file.LevelFile;
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
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
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
  private JComboBox simulationSpeedComboBox;
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
  private AutoCompletion autoComplete;
  private ScriptDocument document;
  private UndoManager undoManager;

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
    textArea.setMarkOccurrences(true);
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

      menuTryb.setText("Misja");
      menu.add(menuTryb);
      for(final LevelFile level : LevelFile.list()) {
        JMenuItem item = new JMenuItem();
        item.setText(level.getName());
        item.setToolTipText(level.getDescription());
        item.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            newDocument(level.getFilePath());
          }
        });
        menuTryb.add(item);
      }

      //======== menuRobot ========
      {
        menuRobot.setText("Robot");

        //---- menuRunItem ----
        menuRunItem.setText("Uruchom");
        menuRunItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        menuRunItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            runCode();
          }
        });
        menuRobot.add(menuRunItem);

        //---- menuStopItem ----
        menuStopItem.setText("Wstrzymaj");
        menuStopItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
        menuStopItem.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            stopCode();
          }
        });
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
        newDocument(BotLogic.game.getLevel().getFile().getFilePath());
      }
    });
  }

  public JPanel getRootPanel() {
    return rootPanel;
  }

  public JMenuBar getMenu() {
    return menu;
  }

  public void bind(EditorFrame editorFrame, GameManager game, LwjglApplicationConfiguration config) {
    this.frame        = editorFrame;
    this.undoManager  = new UndoManager();
    this.gameCanvas   = new LwjglCanvas(game, config);
    Canvas canvas     = this.gameCanvas.getCanvas();
    openglContainer.setLayout(new BorderLayout());
    openglContainer.add(canvas, BorderLayout.CENTER);
    //canvas.requestFocus();

    editorFrame.addWindowListener(new ExitListener(gameCanvas));
    setupMenu();
    setupTextArea();
    setupToolbar();
    editorFrame.setJMenuBar(menu);

    simulationSpeedComboBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        int speed = simulationSpeedComboBox.getSelectedIndex() + 1;
        BotLogic.game.getLevel().setSpeed(speed);
      }
    });


    newDocument(Gdx.files.internal("maps/playground.level").path());

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
      menuSzkic.setEnabled(false);
      menuTryb.setEnabled(false);
      menuRunItem.setEnabled(false);
      menuStopItem.setEnabled(true);
    } else {
      runButton.setEnabled(true);
      stopButton.setEnabled(false);
      textArea.setEditable(true);
      menu.setEnabled(true);
      textArea.requestFocus();
      menuSzkic.setEnabled(true);
      menuTryb.setEnabled(true);
      menuRunItem.setEnabled(true);
      menuStopItem.setEnabled(false);
    }

    if (document != null) {
      if (document.isSaved()) {
        frame.setTitle("BotLogic - ["+document.getFilePath()+"]");
      } else {
        frame.setTitle("BotLogic - [Nowy]");
      }
    }
  }

  private void runCode() {
    BotLogic.game.getController().run(textArea.getText());
  }

  private void stopCode() {
    BotLogic.game.getController().stop();
  }

  private void newDocument(String levelPath) {
    if (currentState != DocumentState.New) {
      closeDocument();
    }

    document = new ScriptDocument();
    document.setLevelPath(levelPath);
    String body = "";

    textArea.requestFocusInWindow();
    simulationSpeedComboBox.setSelectedIndex(0);
    BotLogic.game.newGame(document.getLevelPath());
    BotLogic.game.getScriptRunner().addListener(this);
    setState(DocumentState.New);
    clearErrors();

    for(LevelFile.LevelFeature feature : BotLogic.game.getLevel().getFeatures()) {
      body += Gdx.files.internal("sketches/features/"+feature.toString()+".js").readString() + "\n";
    }

    body += "\n" + Gdx.files.internal("sketches/new/blank.js").readString();

    if (autoComplete != null) {
      autoComplete.uninstall();
    }

    document.setContent(body);
    textArea.setText(document.getContent());

    this.autoComplete = new AutoCompletion(new RobotCompletionProvider(BotLogic.game.getLevel()));
    autoComplete.setAutoActivationEnabled(true);
    autoComplete.setAutoActivationDelay(50);
    autoComplete.setAutoCompleteEnabled(true);
    autoComplete.setShowDescWindow(true);
    autoComplete.install(textArea);
  }

  private void clearErrors() {
    Gdx.app.log(TAG, "Clearing errors!");
    textArea.removeAllLineHighlights();
    gutter.removeAllTrackingIcons();

    parser.clear();
    textArea.forceReparsing(parser);
  }

  private void closeDocument() {
    BotLogic.game.getScriptRunner().removeListener(this);

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
    System.gc();
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
