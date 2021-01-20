package com.kyivrush.service.impl;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskFilterDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import com.kyivrush.mapper.AttachmentMapper;
import com.kyivrush.mapper.CommentMapper;
import com.kyivrush.mapper.TaskFilterMapper;
import com.kyivrush.mapper.TaskMapper;
import com.kyivrush.repo.AttachmentRepo;
import com.kyivrush.repo.CommentRepo;
import com.kyivrush.repo.TaskRepo;
import com.kyivrush.service.CacheService;
import com.kyivrush.service.RatingService;
import com.kyivrush.service.TaskService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private static final String INVALID_TASK_ID_ERROR_MESSAGE = "Invalid task id = ";
  private static final String INVALID_ASSIGNMENT_ID_ERROR_MESSAGE = "Invalid assignment id = ";
  private static final Integer DEFAULT_RATING = 0;

  private final TaskMapper taskMapper;
  private final CommentMapper commentMapper;
  private final AttachmentMapper attachmentMapper;
  private final TaskFilterMapper taskFilterMapper;

  private final TaskRepo taskRepo;
  private final CommentRepo commentRepo;
  private final AttachmentRepo attachmentRepo;

  private final CacheService cacheService;
  private final RatingService ratingService;

  @Override
  public Flux<TaskWithDetailsDto> getTasksByFilter(TaskFilterDto taskFilterDto) {
    log.debug("Getting tasks by filter = {}", taskFilterDto);
    return taskRepo.findByFilter(taskFilterMapper.map(taskFilterDto))
        .map(taskMapper::map)
        .flatMap(task ->
            Mono
                .zip(
                    cacheService
                        .getCachedValue(CacheService.USER_RATING_REQUEST, task.getAssignerId(),
                            DEFAULT_RATING, ratingService::getRating),
                    cacheService
                        .getCachedValue(CacheService.USER_RATING_REQUEST, task.getCreatorId(),
                            DEFAULT_RATING, ratingService::getRating))
                .map(assignerRating_creatorRating ->
                    task.toBuilder()
                        .assigner(
                            task.getAssigner().toBuilder()
                                .rating(assignerRating_creatorRating.getT1())
                                .build())
                        .creator(
                            task.getCreator().toBuilder()
                                .rating(assignerRating_creatorRating.getT2())
                                .build())
                        .build()));
  }

  @Override
  public Mono<TaskWithDetailsDto> getTaskWithDetailsById(String taskId) {
    log.debug("Getting task with details by taskId = {}", taskId);

    if (isEmpty(taskId)) {
      return Mono.error(new RuntimeException(INVALID_TASK_ID_ERROR_MESSAGE + taskId));
    }

    UUID taskUUID = UUID.fromString(taskId);

    return Mono
        .zip(
            taskRepo.findWithUserById(taskUUID).singleOrEmpty(),
            commentRepo.findWithUserByTaskId(taskUUID).collectList(),
            attachmentRepo.findByTaskId(taskUUID).collectList())
        .map(task_comments_attachments ->
            taskMapper.map(task_comments_attachments.getT1())
                .toBuilder()
                .comments(commentMapper.map(task_comments_attachments.getT2()))
                .attachments(attachmentMapper.map(task_comments_attachments.getT3()))
                .build())
        .cast(TaskWithDetailsDto.class)
        .doOnSuccess(
            task -> log.debug("Got task(taskId = {}) : {}", taskId, task));
  }

  @Override
  public Mono<TaskWithIdDto> createTask(TaskDto newTask) {
    log.debug("Creation new task = {}", newTask);

    return taskRepo.save(taskMapper.map(newTask))
        .map(taskMapper::map)
        .doOnSuccess(createdTask -> log.debug("Created task = {}", createdTask));
  }

  @Override
  public Mono<Boolean> changeStatus(String taskId, String newStatus) {
    log.debug("Updating task(taskId = {}) status to {}", taskId, newStatus);

    if (isEmpty(taskId)) {
      return Mono.error(new RuntimeException(INVALID_TASK_ID_ERROR_MESSAGE + taskId));
    }

    return taskRepo.updateStatus(UUID.fromString(taskId), newStatus)
        .thenReturn(true)
        .doOnSuccess(
            ignore -> log.debug("Updated task(taskId = {}) status to {}", taskId, newStatus));
  }

  @Override
  public Mono<Boolean> changeAssignerId(String taskId, String newAssignerId) {
    log.debug("Updating task(taskId = {}) assignerId to {}", taskId, newAssignerId);

    if (isEmpty(taskId)) {
      return Mono.error(new RuntimeException(INVALID_TASK_ID_ERROR_MESSAGE + taskId));
    }

    if (isEmpty(newAssignerId)) {
      return Mono.error(new RuntimeException(INVALID_ASSIGNMENT_ID_ERROR_MESSAGE + newAssignerId));
    }

    return taskRepo.updateAssignerId(UUID.fromString(taskId), UUID.fromString(newAssignerId))
        .thenReturn(true)
        .doOnSuccess(
            ignore -> log
                .debug("Updated task(taskId = {}) assignerId to {}", taskId, newAssignerId));
  }
}
