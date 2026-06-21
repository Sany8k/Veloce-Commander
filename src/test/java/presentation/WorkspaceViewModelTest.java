package presentation;

import application.workspace.FileNavigationService;
import application.workspace.WorkspaceService;
import domain.fs.DirectoryListing;
import domain.workspace.FilePaneState;
import domain.workspace.PaneId;
import domain.workspace.WorkspaceState;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkspaceViewModelTest {

  @Test
  void constructorCreatesInitialWorkspaceAndMapsPanes() {
    WorkspaceService workspaceService = new WorkspaceService();

    WorkspaceViewModel viewModel = createViewModel(workspaceService);

    assertThat(viewModel.getPanes()).hasSize(2);
    assertThat(viewModel.getPanes()).extracting(FilePaneViewModel::getTitle)
        .containsExactly("Pane 1", "Pane 2");
    assertThat(viewModel.getPanes()).filteredOn(FilePaneViewModel::isActive)
        .singleElement()
        .extracting(FilePaneViewModel::getTitle)
        .isEqualTo("Pane 1");
  }

  @Test
  void addPaneDelegatesToServiceAndRebuildsPanes() {
    WorkspaceService workspaceService = new WorkspaceService();
    WorkspaceViewModel viewModel = createViewModel(workspaceService);

    viewModel.addPane();

    assertThat(viewModel.getPanes()).hasSize(3);
    assertThat(viewModel.getPanes()).filteredOn(FilePaneViewModel::isActive)
        .singleElement()
        .extracting(FilePaneViewModel::getTitle)
        .isEqualTo("Pane 3");
  }

  @Test
  void focusPaneDelegatesToServiceAndRebuildsPanes() {
    WorkspaceService workspaceService = new WorkspaceService();
    WorkspaceViewModel viewModel = createViewModel(workspaceService);
    PaneId secondPaneId = viewModel.getPanes().get(1).getId();

    viewModel.focusPane(secondPaneId);

    assertThat(viewModel.getPanes()).filteredOn(FilePaneViewModel::isActive)
        .singleElement()
        .extracting(FilePaneViewModel::getId)
        .isEqualTo(secondPaneId);
  }

  @Test
  void closeActivePaneDelegatesToServiceAndRebuildsPanes() {
    WorkspaceService workspaceService = new WorkspaceService();
    WorkspaceViewModel viewModel = createViewModel(workspaceService);

    viewModel.closeActivePane();

    assertThat(viewModel.getPanes()).hasSize(1);
    assertThat(viewModel.getPanes()).singleElement()
        .extracting(FilePaneViewModel::getTitle)
        .isEqualTo("Pane 2");
  }

  @Test
  void operationsCallWorkspaceServiceWithInternalState() {
    WorkspaceService workspaceService = mock(WorkspaceService.class);
    WorkspaceState workspaceState = new WorkspaceState();
    PaneId paneId = PaneId.random();
    workspaceState.addPane(new FilePaneState(paneId, "Pane 1", "Not loaded", true));
    workspaceState.setActivePaneId(paneId);
    when(workspaceService.createInitialWorkspace()).thenReturn(workspaceState);
    WorkspaceViewModel viewModel = createViewModel(workspaceService);

    viewModel.addPane();
    viewModel.focusPane(paneId);
    viewModel.closeActivePane();

    verify(workspaceService).createInitialWorkspace();
    verify(workspaceService).addPane(workspaceState);
    verify(workspaceService).focusPane(workspaceState, paneId);
    verify(workspaceService).closeActivePane(workspaceState);
  }

  @Test
  void openDirectoryInActivePaneDelegatesToFileNavigationService() throws Exception {
    WorkspaceService workspaceService = mock(WorkspaceService.class);
    FileNavigationService fileNavigationService = mock(FileNavigationService.class);
    WorkspaceState workspaceState = new WorkspaceState();
    PaneId paneId = PaneId.random();
    workspaceState.addPane(new FilePaneState(paneId, "Pane 1", "Not loaded", true));
    workspaceState.setActivePaneId(paneId);
    when(workspaceService.createInitialWorkspace()).thenReturn(workspaceState);
    WorkspaceViewModel viewModel = new WorkspaceViewModel(workspaceService, fileNavigationService);
    Path directory = Path.of("test-directory");

    viewModel.openDirectoryInActivePane(directory);

    verify(fileNavigationService).openDirectory(workspaceState, paneId, directory);
  }

  private WorkspaceViewModel createViewModel(WorkspaceService workspaceService) {
    FileNavigationService fileNavigationService = new FileNavigationService(
        directory -> new DirectoryListing(directory, List.of())
    );
    return new WorkspaceViewModel(workspaceService, fileNavigationService);
  }
}
