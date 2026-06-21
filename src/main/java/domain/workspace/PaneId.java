package domain.workspace;

import java.util.Objects;
import java.util.UUID;

public record PaneId(
    UUID value

) {

  public PaneId {
    Objects.requireNonNull(value, "value must not be null");
  }

  public static PaneId random() {
    return new PaneId(UUID.randomUUID());
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
