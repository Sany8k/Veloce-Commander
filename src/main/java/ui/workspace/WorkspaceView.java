package ui.workspace;

import javafx.scene.control.SplitPane;
import ui.pane.FilePaneView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkspaceView extends SplitPane {

  private final List<FilePaneView> panes = new ArrayList<>();

  public WorkspaceView() {
    this.getStyleClass().add("workspace-view");
    addNewPane();
    addNewPane();
  }

  public void addNewPane() {
    int paneNumber = panes.size() + 1;
    FilePaneView filePaneView = new FilePaneView("Pane " + paneNumber);
    addPane(filePaneView);
  }

  private void addPane(FilePaneView filePaneView) {
    Objects.requireNonNull(filePaneView, "FilePaneView must not be null");

    panes.add(filePaneView);
    getItems().add(filePaneView);
  }
}
