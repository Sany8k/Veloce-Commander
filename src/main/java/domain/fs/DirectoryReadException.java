package domain.fs;

import java.nio.file.Path;
import java.util.Objects;

public class DirectoryReadException extends Exception {

  private final DirectoryReadError error;
  private final Path path;

  public DirectoryReadException(DirectoryReadError error, Path path) {
    super("Could not read directory '" + path + "': " + error);
    this.error = Objects.requireNonNull(error, "error must not be null");
    this.path = Objects.requireNonNull(path, "path must not be null");
  }

  public DirectoryReadError getError() {
    return error;
  }

  public Path getPath() {
    return path;
  }
}
