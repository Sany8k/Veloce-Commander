package domain.fs;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

public record FileEntry(
    Path path,
    String name,
    FileType type,
    long size,
    Instant lastModified,
    boolean hidden,
    boolean readable,
    boolean writable
) {

  public FileEntry {
    Objects.requireNonNull(path, "path must not be null");
    Objects.requireNonNull(name, "name must not be null");
    Objects.requireNonNull(type, "type must not be null");
    if (size < 0) throw new IllegalArgumentException("size must be non-negative");
    Objects.requireNonNull(lastModified, "lastModified must not be null");
  }
}
