package com.kyivrush.controller;

import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskFilterDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import com.kyivrush.service.TaskService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @GetMapping(value = "/task/{taskId}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<TaskWithDetailsDto> getTask(@PathVariable("taskId") String taskId) {
    log.info("Handled getting task by taskId = {}", taskId);
    return taskService.getTaskWithDetailsById(taskId);
  }

  @PostMapping(value = "/task/filter",
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Flux<TaskWithDetailsDto> getTaskByFiler(@Valid @RequestBody TaskFilterDto filter) {
    log.info("Handled getting task by filter = {}", filter);
    return taskService.getTasksByFilter(filter);
  }

  @PostMapping(value = "/task",
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE})
  public Mono<TaskWithIdDto> createTask(@Valid @RequestBody TaskDto taskDto) {
    log.info("Handled creation task = {}", taskDto);
    return taskService.createTask(taskDto);
  }

  @PatchMapping(value = "/task/updateStatus/{taskId}/{newStatus}")
  public Mono<Boolean> updateStatus(@PathVariable("taskId") String taskId,
      @PathVariable("newStatus") String newStatus) {
    log.info("Handled updating task(taskId = {}) status : {}", taskId, newStatus);
    return taskService.changeStatus(taskId, newStatus);
  }

  @PatchMapping(value = "/task/updateAssignerId/{taskId}/{newAssignerId}")
  public Mono<Boolean> updateAssignerId(@PathVariable("taskId") String taskId,
      @PathVariable("newAssignerId") String newAssignerId) {
    log.info("Handled updating task(taskId = {}) status : {}", taskId, newAssignerId);
    return taskService.changeAssignerId(taskId, newAssignerId);
  }
}
