package application.workspace;

import domain.workspace.FilePaneState;
import domain.workspace.PaneId;
import domain.workspace.WorkspaceState;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WorkspaceServiceTest {

  private final WorkspaceService workspaceService = new WorkspaceService();

  @Test
  void createInitialWorkspaceCreatesTwoPanesWithFirstActive() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();

    assertThat(workspaceState.getPanes()).hasSize(2);
    assertThat(workspaceState.getPanes()).extracting(FilePaneState::getTitle)
        .containsExactly("Pane 1", "Pane 2");
    assertThat(workspaceState.getPanes()).extracting(FilePaneState::getLocationText)
        .containsExactly("Not loaded", "Not loaded");
    assertThat(workspaceState.getPanes().get(0).isActive()).isTrue();
    assertThat(workspaceState.getPanes().get(1).isActive()).isFalse();
    assertThat(workspaceState.getActivePaneId()).isEqualTo(workspaceState.getPanes().get(0).getId());
  }

  @Test
  void addPaneAddsNextNumberedPaneAndMakesItActive() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();

    workspaceService.addPane(workspaceState);

    FilePaneState addedPane = workspaceState.getPanes().get(2);
    assertThat(workspaceState.getPanes()).hasSize(3);
    assertThat(addedPane.getTitle()).isEqualTo("Pane 3");
    assertThat(addedPane.getLocationText()).isEqualTo("Not loaded");
    assertThat(workspaceState.getActivePaneId()).isEqualTo(addedPane.getId());
    assertThat(workspaceState.getPanes()).filteredOn(FilePaneState::isActive)
        .singleElement()
        .isEqualTo(addedPane);
  }

  @Test
  void focusPaneMakesSelectedPaneActive() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();
    PaneId secondPaneId = workspaceState.getPanes().get(1).getId();

    workspaceService.focusPane(workspaceState, secondPaneId);

    assertThat(workspaceState.getActivePaneId()).isEqualTo(secondPaneId);
    assertThat(workspaceState.getPanes()).filteredOn(FilePaneState::isActive)
        .singleElement()
        .extracting(FilePaneState::getId)
        .isEqualTo(secondPaneId);
  }

  @Test
  void focusPaneThrowsWhenPaneDoesNotExist() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();
    PaneId missingPaneId = PaneId.random();

    assertThatThrownBy(() -> workspaceService.focusPane(workspaceState, missingPaneId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Pane not found: " + missingPaneId);
  }

  @Test
  void closeActivePaneFocusesPaneOnTheRight() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();
    workspaceService.addPane(workspaceState);
    PaneId secondPaneId = workspaceState.getPanes().get(1).getId();
    PaneId thirdPaneId = workspaceState.getPanes().get(2).getId();

    workspaceService.focusPane(workspaceState, secondPaneId);
    workspaceService.closeActivePane(workspaceState);

    assertThat(workspaceState.getPanes()).extracting(FilePaneState::getId)
        .containsExactly(workspaceState.getPanes().get(0).getId(), thirdPaneId);
    assertThat(workspaceState.getActivePaneId()).isEqualTo(thirdPaneId);
    assertThat(workspaceState.getPanes()).filteredOn(FilePaneState::isActive)
        .singleElement()
        .extracting(FilePaneState::getId)
        .isEqualTo(thirdPaneId);
  }

  @Test
  void closeActivePaneFocusesLeftPaneWhenClosingLastPane() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();
    PaneId firstPaneId = workspaceState.getPanes().get(0).getId();
    PaneId secondPaneId = workspaceState.getPanes().get(1).getId();

    workspaceService.focusPane(workspaceState, secondPaneId);
    workspaceService.closeActivePane(workspaceState);

    assertThat(workspaceState.getPanes()).extracting(FilePaneState::getId)
        .containsExactly(firstPaneId);
    assertThat(workspaceState.getActivePaneId()).isEqualTo(firstPaneId);
    assertThat(workspaceState.getPanes().get(0).isActive()).isTrue();
  }

  @Test
  void closeActivePaneKeepsSinglePaneOpen() {
    WorkspaceState workspaceState = workspaceService.createInitialWorkspace();
    PaneId firstPaneId = workspaceState.getPanes().get(0).getId();
    PaneId secondPaneId = workspaceState.getPanes().get(1).getId();

    workspaceService.focusPane(workspaceState, secondPaneId);
    workspaceService.closeActivePane(workspaceState);
    workspaceService.closeActivePane(workspaceState);

    assertThat(workspaceState.getPanes()).hasSize(1);
    assertThat(workspaceState.getActivePaneId()).isEqualTo(firstPaneId);
    assertThat(workspaceState.getPanes().get(0).isActive()).isTrue();
  }
}
