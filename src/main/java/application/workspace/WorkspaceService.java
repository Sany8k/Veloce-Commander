package application.workspace;

import domain.workspace.FilePaneState;
import domain.workspace.PaneId;
import domain.workspace.WorkspaceState;

import java.util.Objects;

public class WorkspaceService {

  public WorkspaceState createInitialWorkspace() {
    PaneId paneId1 = PaneId.random();
    PaneId paneId2 = PaneId.random();
    FilePaneState paneState1 = new FilePaneState(paneId1, "Pane 1", "Not loaded", true);
    FilePaneState paneState2 = new FilePaneState(paneId2, "Pane 2", "Not loaded", false);

    WorkspaceState workspaceState = new WorkspaceState();
    workspaceState.addPane(paneState1);
    workspaceState.addPane(paneState2);
    workspaceState.setActivePaneId(paneId1);

    return workspaceState;
  }

  public void addPane(WorkspaceState workspaceState) {
    Objects.requireNonNull(workspaceState, "workspaceState must not be null");

    PaneId paneId = PaneId.random();
    int paneNumber = workspaceState.getPanes().size() + 1;
    FilePaneState filePaneState = new FilePaneState(
        paneId,
        "Pane " + paneNumber,
        "Not loaded",
        true
    );

    workspaceState.getPanes().forEach(pane -> pane.setActive(false));
    workspaceState.addPane(filePaneState);
    workspaceState.setActivePaneId(paneId);
  }

  public void focusPane(WorkspaceState workspaceState, PaneId paneId) {
    Objects.requireNonNull(workspaceState, "workspaceState must not be null");
    Objects.requireNonNull(paneId, "paneId must not be null");

    FilePaneState paneToFocus = workspaceState.getPanes().stream()
        .filter(pane -> Objects.equals(pane.getId(), paneId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Pane not found: " + paneId));

    workspaceState.getPanes().forEach(pane -> pane.setActive(false));
    paneToFocus.setActive(true);
    workspaceState.setActivePaneId(paneToFocus.getId());
  }

  public void closeActivePane(WorkspaceState state) {
    Objects.requireNonNull(state, "state must not be null");

    if (state.getPanes().size() < 2) {
      return;
    }

    PaneId activePaneId = state.getActivePaneId();
    Objects.requireNonNull(activePaneId, "activePaneId must not be null");
    int activePaneIndex = findPaneIndex(state, activePaneId);
    if (activePaneIndex < 0) {
      throw new IllegalStateException("Active pane not found: " + activePaneId);
    }

    FilePaneState removedPane = state.getPanes().get(activePaneIndex);
    state.removePane(removedPane);

    int nextActivePaneIndex = Math.min(activePaneIndex, state.getPanes().size() - 1);
    FilePaneState nextActivePane = state.getPanes().get(nextActivePaneIndex);
    state.getPanes().forEach(pane -> pane.setActive(false));
    nextActivePane.setActive(true);
    state.setActivePaneId(nextActivePane.getId());
  }

  private int findPaneIndex(WorkspaceState state, PaneId paneId) {
    for (int index = 0; index < state.getPanes().size(); index++) {
      if (Objects.equals(state.getPanes().get(index).getId(), paneId)) {
        return index;
      }
    }
    return -1;
  }
}
