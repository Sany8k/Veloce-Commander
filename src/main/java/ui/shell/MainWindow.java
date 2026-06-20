package ui.shell;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.theme.ThemeManager;
import ui.workspace.WorkspaceView;

import java.util.Objects;

public class MainWindow {

  private final Stage stage;
  private final WorkspaceView workspaceView;
  private final ThemeManager themeManager;

  private static final int DEFAULT_WIDTH = 1100;
  private static final int DEFAULT_HEIGHT = 720;
  private static final int MIN_WIDTH = 900;
  private static final int MIN_HEIGHT = 600;

  public MainWindow(Stage stage, WorkspaceView workspaceView, ThemeManager themeManager) {
    this.stage = Objects.requireNonNull(stage, "stage must not be null");
    this.workspaceView = Objects.requireNonNull(workspaceView, "workspaceView must not be null");
    this.themeManager = Objects.requireNonNull(themeManager, "themeManager must not be null");
  }

  public void show() {
    BorderPane root = createRoot();
    root.getStyleClass().add("main-window-root");

    Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    themeManager.installAppStyles(scene);

    configureStage(scene);

    stage.show();
  }

  private void configureStage(Scene scene) {
    stage.setTitle("Veloce Commander");
    stage.setMinWidth(MIN_WIDTH);
    stage.setMinHeight(MIN_HEIGHT);
    stage.setScene(scene);
  }

  private BorderPane createRoot() {
    BorderPane newBorderPane = new BorderPane();
    ToolBar toolBar = createToolBar();

    newBorderPane.setTop(toolBar);
    newBorderPane.setCenter(workspaceView);

    return newBorderPane;
  }

  private ToolBar createToolBar() {
    Button addPaneButton = new Button("+ Pane");
    addPaneButton.getStyleClass().add("main-toolbar-button");

    addPaneButton.setOnAction(event -> workspaceView.addNewPane());

    ToolBar toolBar = new ToolBar(addPaneButton);
    toolBar.getStyleClass().add("main-toolbar");
    return toolBar;
  }
}
