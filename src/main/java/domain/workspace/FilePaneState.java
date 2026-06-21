package domain.workspace;

import domain.fs.FileEntry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FilePaneState {

  private PaneId id;
  private String title;
  private String locationText;
  private boolean active;
  private Path currentPath;
  private List<FileEntry> entries;

  public FilePaneState(PaneId id, String title, String locationText, boolean active) {
    this.id = Objects.requireNonNull(id, "id must not be null");
    this.title = Objects.requireNonNull(title, "title must not be null");
    this.locationText = Objects.requireNonNull(locationText, "locationText must not be null");
    this.active = active;
    this.entries = new ArrayList<>();
  }

  public PaneId getId() {
    return id;
  }

  public void setId(PaneId id) {
    this.id = Objects.requireNonNull(id, "id must not be null");
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = Objects.requireNonNull(title, "title must not be null");
  }

  public String getLocationText() {
    return locationText;
  }

  public void setLocationText(String locationText) {
    this.locationText = Objects.requireNonNull(locationText, "locationText must not be null");
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Path getCurrentPath() {
    return currentPath;
  }

  public void setCurrentPath(Path currentPath) {
    this.currentPath = currentPath;
  }

  public List<FileEntry> getEntries() {
    return Collections.unmodifiableList(entries);
  }

  public void setEntries(List<FileEntry> entries) {
    this.entries = List.copyOf(Objects.requireNonNull(entries, "entries must not be null"));
  }
}
