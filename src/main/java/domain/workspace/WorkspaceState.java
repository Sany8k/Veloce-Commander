package domain.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WorkspaceState {

  private final List<FilePaneState> panes = new ArrayList<>();
  private PaneId activePaneId;

  public List<FilePaneState> getPanes() {
    return Collections.unmodifiableList(panes);
  }

  public void addPane(FilePaneState filePaneState) {
    panes.add(Objects.requireNonNull(filePaneState, "filePaneState must not be null"));
  }

  public void removePane(FilePaneState filePaneState) {
    panes.remove(Objects.requireNonNull(filePaneState, "filePaneState must not be null"));
  }

  public PaneId getActivePaneId() {
    return activePaneId;
  }

  public void setActivePaneId(PaneId activePaneId) {
    Objects.requireNonNull(activePaneId, "activePaneId must not be null");
    this.activePaneId = activePaneId;
  }
}
