package app;

import application.fs.DirectoryReader;
import application.workspace.FileNavigationService;
import application.workspace.WorkspaceService;
import infrastructure.fs.NioDirectoryReader;
import javafx.application.Application;
import javafx.stage.Stage;
import presentation.WorkspaceViewModel;
import ui.shell.MainWindow;
import ui.theme.ThemeManager;
import ui.workspace.WorkspaceView;

public class FileManagerApplication extends Application {
    @Override
    public void start(Stage stage) {
        ThemeManager themeManager = new ThemeManager();
        themeManager.applyDefaultTheme();

        WorkspaceService workspaceService = new WorkspaceService();
        DirectoryReader directoryReader = new NioDirectoryReader();
        FileNavigationService fileNavigationService = new FileNavigationService(directoryReader);
        WorkspaceViewModel workspaceViewModel = new WorkspaceViewModel(
            workspaceService,
            fileNavigationService
        );
        WorkspaceView workspaceView = new WorkspaceView(workspaceViewModel);

        MainWindow mainWindow = new MainWindow(stage, workspaceView, themeManager);
        mainWindow.show();
    }
}
