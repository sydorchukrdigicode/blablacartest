package com.kyivrush.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kyivrush.dto.AttachmentDto;
import com.kyivrush.dto.CommentWithDetailsDto;
import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskFilterDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import com.kyivrush.dto.UserDto;
import com.kyivrush.dto.UserWithDetailsDto;
import com.kyivrush.service.TaskService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@WebFluxTest(TaskController.class)
class TaskControllerTest {

  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private static final UUID UUID_3 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b913");
  private static final UUID UUID_4 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b914");

  @MockBean
  private TaskService taskService;

  @Autowired
  private WebTestClient webClient;

  @Test
  void shouldReturnTask() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);

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
                .build()
        )
        .creator(
            UserWithDetailsDto.builder()
                .userId(UUID_2.toString())
                .departmentId(UUID_1.toString())
                .name("name-1")
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

    when(taskService.getTaskWithDetailsById(UUID_1.toString())).thenReturn(Mono.just(expected));

    //when
    webClient.get()
        .uri("/task/" + UUID_1.toString())
        .exchange()
        .expectStatus().isOk()
        .expectBody(TaskWithDetailsDto.class).isEqualTo(expected);

    //then
    verify(taskService, times(1)).getTaskWithDetailsById(UUID_1.toString());
  }

  @Test
  void shouldReturnTasksByFilter() {
    //given
    ParameterizedTypeReference<List<TaskWithDetailsDto>> resultTypeReference =
        new ParameterizedTypeReference<>() {
        };
    TaskFilterDto filter = TaskFilterDto.builder()
        .assignerDepartmentId(UUID_1.toString())
        .creatorDepartmentId(UUID_2.toString())
        .isAscOrder(true)
        .build();

    TaskWithDetailsDto expected = TaskWithDetailsDto.builder()
        .taskId(UUID_1.toString())
        .build();

    when(taskService.getTasksByFilter(filter)).thenReturn(Flux.just(expected));

    //when
    webClient.post()
        .uri("/task/filter")
        .bodyValue(filter)
        .exchange()
        .expectStatus().isOk()
        .expectBody(resultTypeReference)
        .isEqualTo(List.of(expected));

    //then
    verify(taskService, times(1)).getTasksByFilter(filter);
  }

  @Test
  void shouldCreateTask() {
    //given
    TaskDto input = TaskDto.builder()
        .name("name-1")
        .assignerId("5f1aedefc0b6cb2698e2a160")
        .creatorId("5f1aedefc0b6cb2698e2a161")
        .details("some-details")
        .status("some-status")
        .build();

    TaskWithIdDto expected = TaskWithIdDto.builder()
        .taskId("5f1aedefc0b6cb2698e2a163")
        .name("name-1")
        .assignerId("5f1aedefc0b6cb2698e2a160")
        .creatorId("5f1aedefc0b6cb2698e2a161")
        .details("some-details")
        .status("some-status")
        .build();

    when(taskService.createTask(input)).thenReturn(Mono.just(expected));

    //when
    webClient.post()
        .uri("/task")
        .bodyValue(input)
        .exchange()
        .expectStatus().isOk()
        .expectBody(TaskWithIdDto.class).isEqualTo(expected);

    //then
    verify(taskService, times(1)).createTask(input);
  }

  @Test
  void shouldUpdateStatus() {
    //given
    String taskId = "1";
    String status = "status";

    when(taskService.changeStatus(taskId, status)).thenReturn(Mono.just(true));

    //when
    webClient.patch()
        .uri("/task/updateStatus/" + taskId + "/" + status)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Boolean.class).isEqualTo(true);

    //then
    verify(taskService, times(1)).changeStatus(taskId, status);
  }

  @Test
  void shouldUpdateAssignerId() {
    //given
    String taskId = "1";
    String assignerId = "assignerId";

    when(taskService.changeAssignerId(taskId, assignerId)).thenReturn(Mono.just(true));

    //when
    webClient.patch()
        .uri("/task/updateAssignerId/" + taskId + "/" + assignerId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Boolean.class).isEqualTo(true);

    //then
    verify(taskService, times(1)).changeAssignerId(taskId, assignerId);
  }
}