package com.kyivrush.service;

import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskFilterDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskService {

  Flux<TaskWithDetailsDto> getTasksByFilter(TaskFilterDto taskFilterDto);

  Mono<TaskWithDetailsDto> getTaskWithDetailsById(String taskId);

  Mono<TaskWithIdDto> createTask(TaskDto newTask);

  Mono<Boolean> changeStatus(String taskId, String newStatus);

  Mono<Boolean> changeAssignerId(String taskId, String newAssignerId);
}
