package presentation;

import application.workspace.FileNavigationService;
import application.workspace.WorkspaceService;
import domain.fs.DirectoryReadException;
import domain.workspace.PaneId;
import domain.workspace.WorkspaceState;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WorkspaceViewModel {

  private final WorkspaceService workspaceService;
  private final FileNavigationService fileNavigationService;
  private final WorkspaceState workspaceState;
  private final List<FilePaneViewModel> panes = new ArrayList<>();

  public WorkspaceViewModel(
      WorkspaceService workspaceService,
      FileNavigationService fileNavigationService
  ) {
    this.workspaceService = Objects.requireNonNull(
        workspaceService,
        "workspaceService must not be null"
    );
    this.fileNavigationService = Objects.requireNonNull(
        fileNavigationService,
        "fileNavigationService must not be null"
    );
    this.workspaceState = this.workspaceService.createInitialWorkspace();
    rebuildPanes();
  }

  public List<FilePaneViewModel> getPanes() {
    return Collections.unmodifiableList(panes);
  }

  public void addPane() {
    workspaceService.addPane(workspaceState);
    rebuildPanes();
  }

  public void closeActivePane() {
    workspaceService.closeActivePane(workspaceState);
    rebuildPanes();
  }

  public void focusPane(PaneId paneId) {
    workspaceService.focusPane(workspaceState, paneId);
    rebuildPanes();
  }

  public void openDirectoryInActivePane(Path directory) throws DirectoryReadException {
    PaneId activePaneId = workspaceState.getActivePaneId();
    Objects.requireNonNull(activePaneId, "activePaneId must not be null");
    fileNavigationService.openDirectory(workspaceState, activePaneId, directory);
    rebuildPanes();
  }

  private void rebuildPanes() {
    panes.clear();
    workspaceState.getPanes().stream()
        .map(FilePaneViewModel::from)
        .forEach(panes::add);
  }
}
