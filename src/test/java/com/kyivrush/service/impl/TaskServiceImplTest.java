package com.kyivrush.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kyivrush.domain.Attachment;
import com.kyivrush.domain.CommentWithDetails;
import com.kyivrush.domain.Department;
import com.kyivrush.domain.Task;
import com.kyivrush.domain.TaskFilter;
import com.kyivrush.domain.TaskWithDetails;
import com.kyivrush.domain.User;
import com.kyivrush.domain.UserWithDetails;
import com.kyivrush.dto.AttachmentDto;
import com.kyivrush.dto.CommentWithDetailsDto;
import com.kyivrush.dto.DepartmentDto;
import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskFilterDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import com.kyivrush.dto.UserDto;
import com.kyivrush.dto.UserWithDetailsDto;
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
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private static final UUID UUID_3 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b913");
  private static final UUID UUID_4 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b914");

  private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);
  private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
  private final AttachmentMapper attachmentMapper = Mappers.getMapper(AttachmentMapper.class);
  private final TaskFilterMapper taskFilterMapper = Mappers.getMapper(TaskFilterMapper.class);
  @Mock
  private TaskRepo taskRepo;
  @Mock
  private CommentRepo commentRepo;
  @Mock
  private AttachmentRepo attachmentRepo;

  private CacheService cacheService;
  private RatingService ratingService;
  private TaskService taskService;

  @BeforeEach
  void setUp() {
    CacheService cacheService = new CacheServiceImpl();
    RatingService ratingService = new RatingServiceImpl();
    taskService = new TaskServiceImpl(taskMapper, commentMapper, attachmentMapper,
        taskFilterMapper, taskRepo, commentRepo, attachmentRepo,
        cacheService, ratingService);
  }

  @Test
  void shouldReturnTasksByFilter() {
    //given
    TaskFilterDto inputDto = TaskFilterDto.builder()
        .creatorDepartmentId(UUID_1.toString())
        .assignerDepartmentId(UUID_2.toString())
        .isAscOrder(true)
        .build();

    TaskFilter input = TaskFilter.builder()
        .creatorDepartmentId(UUID_1)
        .assignerDepartmentId(UUID_2)
        .isAscOrder(true)
        .build();

    when(taskRepo.findByFilter(input))
        .thenReturn(Flux.fromIterable(
            List.of(TaskWithDetails.builder()
                .assignerId(UUID_3)
                .creatorId(UUID_4)
                .assigner(
                    UserWithDetails.builder()
                        .userId(UUID_3)
                        .build())
                .creator(
                    UserWithDetails.builder()
                        .userId(UUID_4)
                        .build())
                .build())));

    //when
    Flux<TaskWithDetailsDto> result = taskService.getTasksByFilter(inputDto).log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .assertNext(task -> {
          Assertions.assertEquals(-1354736141, task.getCreator().getRating());
          Assertions.assertEquals(-1354736142, task.getAssigner().getRating());
        })
        .verifyComplete();

    verify(taskRepo, times(1)).findByFilter(input);
  }

  @Test
  void shouldReturnTask() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    TaskWithDetails input = TaskWithDetails.builder()
        .taskId(UUID_1)
        .creationTime(time)
        .name("name-1")
        .assignerId(UUID_2)
        .creatorId(UUID_3)
        .details("some-details")
        .status("some-status")
        .assigner(
            UserWithDetails.builder()
                .userId(UUID_1)
                .departmentId(UUID_2)
                .name("name-1")
                .department(Department.builder()
                    .departmentId(UUID_2)
                    .name("dep-1")
                    .build())
                .build()
        )
        .creator(
            UserWithDetails.builder()
                .userId(UUID_2)
                .departmentId(UUID_1)
                .name("name-1")
                .department(Department.builder()
                    .departmentId(UUID_2)
                    .name("dep-1")
                    .build())
                .build()
        )
        .build();

    CommentWithDetails comment = CommentWithDetails.builder()
        .commentId(UUID_1)
        .authorId(UUID_2)
        .taskId(UUID_3)
        .value("comment")
        .creationTime(time)
        .author(User.builder()
            .userId(UUID_2)
            .departmentId(UUID_4)
            .name("nnnn")
            .build())
        .build();

    Attachment attachment = Attachment.builder()
        .attachmentId(UUID_1)
        .taskId(UUID_2)
        .fileName("fl-1")
        .link("link")
        .creationTime(time)
        .build();

    TaskWithDetailsDto expected = TaskWithDetailsDto.builder()
        .taskId(UUID_1.toString())
        .creationTime(time)
        .name("name-1")
        .assignerId(UUID_2.toString())
        .creatorId(UUID_3.toString())
        .details("some-details")
        .status("some-status")
        .assigner(
            UserWithDetailsDto.builder()
                .userId(UUID_1.toString())
                .departmentId(UUID_2.toString())
                .name("name-1")
                .department(DepartmentDto.builder()
                    .departmentId(UUID_2.toString())
                    .name("dep-1")
                    .build())
                .build()
        )
        .creator(
            UserWithDetailsDto.builder()
                .userId(UUID_2.toString())
                .departmentId(UUID_1.toString())
                .name("name-1")
                .department(DepartmentDto.builder()
                    .departmentId(UUID_2.toString())
                    .name("dep-1")
                    .build())
                .build()
        )
        .comments(List.of(
            CommentWithDetailsDto.builder()
                .commentId(UUID_1.toString())
                .authorId(UUID_2.toString())
                .taskId(UUID_3.toString())
                .value("comment")
                .creationTime(time)
                .author(UserDto.builder()
                    .userId(UUID_2.toString())
                    .departmentId(UUID_4.toString())
                    .name("nnnn")
                    .build())
                .build()
        ))
        .attachments(List.of(
            AttachmentDto.builder()
                .attachmentId(UUID_1.toString())
                .taskId(UUID_2.toString())
                .fileName("fl-1")
                .link("link")
                .creationTime(time)
                .build()
        ))
        .build();

    when(taskRepo.findWithUserById(UUID_1))
        .thenReturn(Flux.just(input));
    when(commentRepo.findWithUserByTaskId(UUID_1))
        .thenReturn(Flux.fromIterable(List.of(comment)));
    when(attachmentRepo.findByTaskId(UUID_1))
        .thenReturn(Flux.fromIterable(List.of(attachment)));

    //when
    Mono<TaskWithDetailsDto> result = taskService.getTaskWithDetailsById(UUID_1.toString())
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();

    verify(taskRepo, times(1)).findWithUserById(UUID_1);
    verify(commentRepo, times(1)).findWithUserByTaskId(UUID_1);
    verify(attachmentRepo, times(1)).findByTaskId(UUID_1);
  }

  @Test
  void shouldFailReturnTask() {
    //given

    //when
    Mono<TaskWithDetailsDto> result = taskService.getTaskWithDetailsById("")
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .verifyError(RuntimeException.class);
  }

  @Test
  void shouldCreateTask() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    TaskDto input = TaskDto.builder()
        .name("name-1")
        .assignerId(UUID_1.toString())
        .creatorId(UUID_2.toString())
        .details("some-details")
        .status("some-status")
        .build();
    Task inputDomain = Task.builder()
        .name("name-1")
        .assignerId(UUID_1)
        .creatorId(UUID_2)
        .details("some-details")
        .status("some-status")
        .build();
    TaskWithIdDto expected = TaskWithIdDto.builder()
        .taskId(UUID_3.toString())
        .creationTime(time)
        .name("name-1")
        .assignerId(UUID_1.toString())
        .creatorId(UUID_2.toString())
        .details("some-details")
        .status("some-status")
        .build();
    Task repoResult = inputDomain.toBuilder()
        .taskId(UUID_3)
        .creationTime(time)
        .build();

    when(taskRepo.save(inputDomain))
        .thenReturn(Mono.just(repoResult));

    //when
    Mono<TaskWithIdDto> result = taskService.createTask(input)
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .expectNext(expected)
        .verifyComplete();
    verify(taskRepo, times(1)).save(inputDomain);
  }

  @Test
  void shouldChangeStatus() {
    //given
    when(taskRepo.updateStatus(UUID_3, "newStatus"))
        .thenReturn(Mono.just(Task.builder().build()));

    //when
    Mono<Boolean> result = taskService.changeStatus(UUID_3.toString(), "newStatus")
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .expectNext(true)
        .verifyComplete();
    verify(taskRepo, times(1))
        .updateStatus(UUID_3, "newStatus");
  }

  @Test
  void shouldFailChangeStatus() {
    //given

    //when
    Mono<Boolean> result = taskService.changeStatus("", "newStatus")
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .verifyError(RuntimeException.class);
  }

  @Test
  void shouldChangeAssignerId() {
    //given
    when(taskRepo.updateAssignerId(UUID_3, UUID_2))
        .thenReturn(Mono.just(Task.builder().build()));

    //when
    Mono<Boolean> result = taskService
        .changeAssignerId(UUID_3.toString(), UUID_2.toString())
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .expectNext(true)
        .verifyComplete();
    verify(taskRepo, times(1))
        .updateAssignerId(UUID_3, UUID_2);
  }

  @Test
  void shouldFailChangeAssignerId_taskId() {
    //given

    //when
    Mono<Boolean> result = taskService.changeAssignerId("", "5f1aedefc0b6cb2698e2a151")
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .verifyError(RuntimeException.class);
  }

  @Test
  void shouldFailChangeAssignerId_assignerId() {
    //given

    //when
    Mono<Boolean> result = taskService.changeAssignerId("5f1aedefc0b6cb2698e2a151", "")
        .log();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .verifyError(RuntimeException.class);
  }
}