package com.kyivrush.mapper;

import com.kyivrush.domain.Task;
import com.kyivrush.domain.TaskWithDetails;
import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper extends BasicMapper {

  @Mapping(source = "taskId", target = "taskId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "creatorId", target = "creatorId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "assignerId", target = "assignerId", qualifiedByName = "mapUUIDToString")
  TaskWithIdDto map(Task task);

  @Mapping(source = "taskId", target = "taskId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "creatorId", target = "creatorId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "assignerId", target = "assignerId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "creator.userId", target = "creator.userId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "creator.departmentId", target = "creator.departmentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "assigner.userId", target = "assigner.userId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "assigner.departmentId", target = "assigner.departmentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "assigner.department.departmentId", target = "assigner.department.departmentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "creator.department.departmentId", target = "creator.department.departmentId", qualifiedByName = "mapUUIDToString")
  TaskWithDetailsDto map(TaskWithDetails task);

  @Mapping(source = "creatorId", target = "creatorId", qualifiedByName = "mapStringToUUID")
  @Mapping(source = "assignerId", target = "assignerId", qualifiedByName = "mapStringToUUID")
  Task map(TaskDto taskDto);
}
