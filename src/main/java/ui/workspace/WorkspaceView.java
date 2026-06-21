package ui.workspace;

import domain.fs.DirectoryReadException;
import javafx.scene.control.SplitPane;
import presentation.FilePaneViewModel;
import presentation.WorkspaceViewModel;
import ui.pane.FilePaneView;

import java.nio.file.Path;
import java.util.Objects;

public class WorkspaceView extends SplitPane {

  private final WorkspaceViewModel viewModel;

  public WorkspaceView(WorkspaceViewModel viewModel) {
    this.viewModel = Objects.requireNonNull(viewModel, "Workspace view model must not be null");
    this.getStyleClass().add("workspace-view");
    render();
  }

  public void addPane() {
    viewModel.addPane();
    render();
  }

  public void closeActivePane() {
    viewModel.closeActivePane();
    render();
  }

  public void openUserHomeInActivePane() {
    Path userHome = Path.of(System.getProperty("user.home"));
    try {
      viewModel.openDirectoryInActivePane(userHome);
      render();
    } catch (DirectoryReadException exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void render() {
    getItems().clear();
    for (FilePaneViewModel paneViewModel : viewModel.getPanes()) {
      FilePaneView filePaneView = new FilePaneView(paneViewModel);
      filePaneView.setOnMouseClicked(event -> {
        viewModel.focusPane(paneViewModel.getId());
        render();
      });
      getItems().add(filePaneView);
    }
  }
}
