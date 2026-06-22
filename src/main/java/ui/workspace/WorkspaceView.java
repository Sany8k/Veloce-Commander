package ui.workspace;

import domain.fs.DirectoryReadException;
import domain.workspace.PaneId;
import javafx.scene.control.SplitPane;
import presentation.FileEntryViewModel;
import presentation.FilePaneViewModel;
import presentation.WorkspaceViewModel;
import ui.pane.FilePaneView;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class WorkspaceView extends SplitPane {

  private final WorkspaceViewModel viewModel;
  private final Map<PaneId, FilePaneView> paneViews = new LinkedHashMap<>();
  private Consumer<Path> onActivePathChanged = path -> {};

  public WorkspaceView(WorkspaceViewModel viewModel) {
    this.viewModel = Objects.requireNonNull(viewModel, "Workspace view model must not be null");
    this.getStyleClass().add("workspace-view");
    renderAll();
  }

  public void addPane() {
    double[] positions = getDividerPositions();
    viewModel.addPane();
    FilePaneViewModel newPaneViewModel = viewModel.getPanes().stream()
        .filter(pane -> !paneViews.containsKey(pane.getId()))
        .reduce((first, second) -> second)
        .orElseThrow(() -> new IllegalStateException("New pane was not added"));
    FilePaneView newPaneView = createPaneView(newPaneViewModel);
    paneViews.put(newPaneViewModel.getId(), newPaneView);
    getItems().add(newPaneView);
    restoreDividerPositions(positions);
    refreshAllPaneStyles();
    notifyActivePathChanged();
  }

  public void closeActivePane() {
    double[] positions = getDividerPositions();
    viewModel.closeActivePane();
    renderAll();
    restoreDividerPositions(positions);
    notifyActivePathChanged();
  }

  public void openUserHomeInActivePane() {
    Path userHome = Path.of(System.getProperty("user.home"));
    try {
      PaneId activePaneId = viewModel.getActivePaneId();
      viewModel.openDirectoryInActivePane(userHome);
      refreshPane(activePaneId);
      notifyActivePathChanged();
    } catch (DirectoryReadException exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void renderAll() {
    getItems().clear();
    paneViews.clear();
    for (FilePaneViewModel paneViewModel : viewModel.getPanes()) {
      FilePaneView filePaneView = createPaneView(paneViewModel);
      paneViews.put(paneViewModel.getId(), filePaneView);
      getItems().add(filePaneView);
    }
  }

  public void goUpInActivePane() {
    try {
      PaneId activePaneId = viewModel.getActivePaneId();
      viewModel.goUpInActivePane();
      refreshPane(activePaneId);
      notifyActivePathChanged();
    } catch (DirectoryReadException exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void openPathInActivePane(String pathText) {
    Objects.requireNonNull(pathText, "pathText must not be null");

    String normalizedPathText = pathText.trim();
    if (normalizedPathText.isEmpty()) {
      return;
    }

    Path path;
    try {
      path = Path.of(normalizedPathText);
    } catch (InvalidPathException exception) {
      System.err.println("Invalid path: " + normalizedPathText);
      return;
    }

    try {
      PaneId activePaneId = viewModel.getActivePaneId();
      viewModel.openDirectoryInActivePane(path);
      refreshPane(activePaneId);
      notifyActivePathChanged();
    } catch (DirectoryReadException exception) {
      System.err.println(exception.getMessage());
    }
  }

  public void setOnActivePathChanged(Consumer<Path> onActivePathChanged) {
    this.onActivePathChanged = Objects.requireNonNull(
        onActivePathChanged,
        "onActivePathChanged must not be null"
    );
  }

  public void refreshActivePane() {
    try {
      PaneId activePaneId = viewModel.getActivePaneId();
      viewModel.refreshActivePane();
      refreshPane(activePaneId);
      notifyActivePathChanged();
    } catch (DirectoryReadException exception) {
      System.err.println(exception.getMessage());
    }
  }

  private void notifyActivePathChanged() {
    Path currentPath = viewModel.getActivePaneCurrentPath();
    onActivePathChanged.accept(currentPath);
  }

  private FilePaneView createPaneView(FilePaneViewModel paneViewModel) {
    Objects.requireNonNull(paneViewModel, "paneViewModel must not be null");
    return new FilePaneView(
        paneViewModel,
        entry -> openEntryInPane(paneViewModel.getId(), entry),
        () -> focusPane(paneViewModel.getId())
    );
  }

  private void refreshPane(PaneId paneId) {
    Objects.requireNonNull(paneId, "paneId must not be null");
    FilePaneView currentPaneView = paneViews.get(paneId);
    if (currentPaneView == null) {
      throw new IllegalArgumentException("Pane not found: " + paneId);
    }

    int paneIndex = getItems().indexOf(currentPaneView);
    if (paneIndex < 0) {
      throw new IllegalStateException("Pane view is not attached: " + paneId);
    }

    double[] positions = getDividerPositions();
    FilePaneView replacement = createPaneView(viewModel.getPane(paneId));
    paneViews.put(paneId, replacement);
    getItems().set(paneIndex, replacement);
    restoreDividerPositions(positions);
  }

  private void refreshAllPaneStyles() {
    for (FilePaneViewModel paneViewModel : viewModel.getPanes()) {
      FilePaneView paneView = paneViews.get(paneViewModel.getId());
      if (paneView != null) {
        paneView.setActive(paneViewModel.isActive());
      }
    }
  }

  private void openEntryInPane(PaneId paneId, FileEntryViewModel entry) {
    Objects.requireNonNull(paneId, "paneId must not be null");
    Objects.requireNonNull(entry, "entry must not be null");

    if (!entry.directory()) {
      return;
    }

    try {
      viewModel.openDirectoryInPane(paneId, entry.path());
      refreshPane(paneId);
      refreshAllPaneStyles();
      notifyActivePathChanged();
    } catch (DirectoryReadException exception) {
      System.err.println(exception.getMessage());
    }
  }

  private void focusPane(PaneId paneId) {
    Objects.requireNonNull(paneId, "paneId must not be null");
    viewModel.focusPane(paneId);
    refreshAllPaneStyles();
    notifyActivePathChanged();
  }

  private void restoreDividerPositions(double[] positions) {
    if (positions.length == 0 || getDividers().isEmpty()) {
      return;
    }

    int dividerCount = Math.min(positions.length, getDividers().size());
    double[] positionsToRestore = new double[dividerCount];
    System.arraycopy(positions, 0, positionsToRestore, 0, dividerCount);
    setDividerPositions(positionsToRestore);
  }
}
