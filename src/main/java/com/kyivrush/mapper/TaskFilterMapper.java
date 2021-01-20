package com.kyivrush.mapper;

import com.kyivrush.domain.TaskFilter;
import com.kyivrush.dto.TaskFilterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskFilterMapper extends BasicMapper {

  @Mapping(source = "creatorDepartmentId", target = "creatorDepartmentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "assignerDepartmentId", target = "assignerDepartmentId", qualifiedByName = "mapUUIDToString")
  TaskFilter map(TaskFilterDto taskFilterDto);
}
