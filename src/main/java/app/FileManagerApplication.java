package app;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.shell.MainWindow;
import ui.theme.ThemeManager;
import ui.workspace.WorkspaceView;

public class FileManagerApplication extends Application {
    @Override
    public void start(Stage stage) {
        ThemeManager themeManager = new ThemeManager();
        themeManager.applyDefaultTheme();

        WorkspaceView workspaceView = new WorkspaceView();

        MainWindow mainWindow = new MainWindow(stage, workspaceView, themeManager);
        mainWindow.show();
    }
}
