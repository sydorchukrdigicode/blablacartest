package com.kyivrush.util;

import java.util.Objects;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@NoArgsConstructor
public class Utils {

  public static String mapUUIDToString(UUID id) {
    if (Objects.isNull(id)) {
      return null;
    }
    return id.toString();
  }

  public static UUID mapStringToUUID(String id) {
    if (ObjectUtils.isEmpty(id)) {
      return null;
    }
    return UUID.fromString(id);
  }
}
