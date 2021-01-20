package com.kyivrush.mapper;

import com.kyivrush.util.Utils;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BasicMapper {

  @Named("mapUUIDToString")
  default String mapUUIDToString(UUID id) {
    return Utils.mapUUIDToString(id);
  }

  @Named("mapStringToUUID")
  default UUID mapStringToUUID(String id) {
    return Utils.mapStringToUUID(id);
  }
}
