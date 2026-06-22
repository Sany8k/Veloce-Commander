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
    Objects.requireNonNull(directory, "directory must not be null");
    PaneId activePaneId = workspaceState.getActivePaneId();
    Objects.requireNonNull(activePaneId, "activePaneId must not be null");
    fileNavigationService.openDirectory(workspaceState, activePaneId, directory);
    rebuildPanes();
  }

  public void openDirectoryInPane(PaneId paneId, Path directory) throws DirectoryReadException {
    Objects.requireNonNull(paneId, "paneId must not be null");
    Objects.requireNonNull(directory, "directory must not be null");

    fileNavigationService.openDirectory(workspaceState, paneId, directory);
    workspaceService.focusPane(workspaceState, paneId);
    rebuildPanes();
  }

  public PaneId getActivePaneId() {
    PaneId activePaneId = workspaceState.getActivePaneId();
    return Objects.requireNonNull(activePaneId, "activePaneId must not be null");
  }

  public FilePaneViewModel getPane(PaneId paneId) {
    Objects.requireNonNull(paneId, "paneId must not be null");
    return panes.stream()
        .filter(pane -> Objects.equals(pane.getId(), paneId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Pane not found: " + paneId));
  }

  public void goUpInActivePane() throws DirectoryReadException {
    PaneId activePaneId = getActivePaneId();
    FilePaneViewModel activePane = getPane(activePaneId);

    Path currentPath = activePane.getCurrentPath();
    if (currentPath == null) {
      return;
    }

    Path parent = currentPath.getParent();
    if (parent == null) {
      return;
    }

    fileNavigationService.openDirectory(workspaceState, activePaneId, parent);
    rebuildPanes();
  }

  public Path getActivePaneCurrentPath() {
    PaneId activePaneId = getActivePaneId();
    return getPane(activePaneId).getCurrentPath();
  }

  public void refreshActivePane() throws DirectoryReadException {
    PaneId activePaneId = getActivePaneId();
    FilePaneViewModel activePane = getPane(activePaneId);

    Path currentPath = activePane.getCurrentPath();
    if (currentPath == null) {
      return;
    }

    fileNavigationService.openDirectory(workspaceState, activePaneId, currentPath);
    rebuildPanes();
  }

  private void rebuildPanes() {
    panes.clear();
    workspaceState.getPanes().stream()
        .map(FilePaneViewModel::from)
        .forEach(panes::add);
  }
}
