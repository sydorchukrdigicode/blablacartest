package com.kyivrush.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.kyivrush.domain.Department;
import com.kyivrush.domain.Task;
import com.kyivrush.domain.TaskFilter;
import com.kyivrush.domain.TaskWithDetails;
import com.kyivrush.domain.UserWithDetails;
import io.r2dbc.spi.ConnectionFactory;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import mockit.Mock;
import mockit.MockUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.ConnectionFactoryUtils;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
class TaskRepoTest extends GenericRepoIntegrationTest {

  private static final UUID DEPARTMENT_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID TASK_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b910");
  private static final UUID TASK_ID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b900");
  private static final UUID USER_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private static final UUID USER_ID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b913");

  @Autowired
  private TaskRepo taskRepo;

  @Autowired
  private ConnectionFactory connectionFactory;

  @BeforeEach
  void setUp() {
    ConnectionFactoryUtils.getConnection(connectionFactory)
        .flatMap(connection -> ScriptUtils
            .executeSqlScript(connection, new ClassPathResource("data.sql")))
        .block();
  }

  @Test
  void shouldCreateTask() {
    //given
    Instant currentTime = Instant.ofEpochSecond(1000L);
    new MockUp<Instant>() {
      @Mock
      Instant now(Clock clock) {
        return currentTime;
      }
    };
    Task expected = getTestTask();

    //when
    taskRepo.save(expected)
        .as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();

    //then
    taskRepo.findAll()
        .collectList()
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .assertNext(actual -> {
          assertEquals(actual.size(), 3);
          Task newTask = actual.stream()
              .filter(
                  task -> !task.getTaskId().equals(TASK_ID) && !task.getTaskId().equals(TASK_ID_2))
              .findFirst()
              .get();

          assertNotNull(newTask.getTaskId());
          assertEquals(expected.getName(), newTask.getName());
          assertEquals(expected.getDetails(), newTask.getDetails());
          assertEquals(expected.getStatus(), newTask.getStatus());
          assertEquals(expected.getAssignerId(), newTask.getAssignerId());
          assertEquals(expected.getCreatorId(), newTask.getCreatorId());
          assertEquals(expected.getName(), newTask.getName());
          assertEquals(currentTime, newTask.getCreationTime());

        })
        .verifyComplete();
  }

  @Test
  void shouldChangeStatus() {
    //given
    String expected = "newStatus";

    //when
    taskRepo.updateStatus(TASK_ID, expected)
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();

    //then
    taskRepo.findById(TASK_ID)
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .assertNext(task -> assertEquals(expected, task.getStatus()))
        .verifyComplete();
  }

  @Test
  void shouldChangeAssignerId() {
    //given

    //when
    taskRepo.updateAssignerId(TASK_ID, USER_ID_2)
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();

    //then
    taskRepo.findById(TASK_ID)
        .log()
        .as(StepVerifier::create)
        .expectSubscription()
        .assertNext(task -> assertEquals(USER_ID_2, task.getAssignerId()))
        .verifyComplete();
  }

  @Test
  void shouldFindByFilter() {
    //todo improve this test
    //given
    TaskFilter input = TaskFilter.builder()
        .assignerDepartmentId(DEPARTMENT_ID)
        .creatorDepartmentId(DEPARTMENT_ID)
        .isAscOrder(false)
        .build();

    //when
    Flux<TaskWithDetails> actual = taskRepo.findByFilter(input).log();

    //then
    actual.as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  void shouldFindByFilter_null() {
    //todo improve this test
    //given
    TaskFilter input = TaskFilter.builder()
        .assignerDepartmentId(null)
        .creatorDepartmentId(null)
        .isAscOrder(null)
        .build();

    //when
    Flux<TaskWithDetails> actual = taskRepo.findByFilter(input).log();

    //then
    actual.as(StepVerifier::create)
        .expectSubscription()
        .expectNextCount(2)
        .verifyComplete();
  }

  @Test
  void shouldFindWithUserById() {
    //given
    Instant currentTime = Instant.ofEpochSecond(1000L);

    TaskWithDetails expected = TaskWithDetails.builder()
        .taskId(TASK_ID_2)
        .assignerId(USER_ID)
        .assigner(UserWithDetails.builder()
            .userId(USER_ID)
            .name("user-1")
            .departmentId(DEPARTMENT_ID)
            .department(Department.builder()
                .departmentId(DEPARTMENT_ID)
                .name("department-1")
                .build())
            .build())
        .creator(UserWithDetails.builder()
            .userId(USER_ID)
            .name("user-1")
            .departmentId(DEPARTMENT_ID)
            .department(Department.builder()
                .departmentId(DEPARTMENT_ID)
                .name("department-1")
                .build())
            .build())
        .creatorId(USER_ID)
        .name("name-2")
        .details("some-details")
        .status("some-status")
        .creationTime(currentTime)
        .build();

    //when
    Mono<List<TaskWithDetails>> result = taskRepo.findWithUserById(TASK_ID_2)
        .log()
        .collectList();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .assertNext(comments -> {
          assertEquals(1, comments.size());
          assertEquals(expected, comments.get(0));
        })
        .verifyComplete();
  }

  private Task getTestTask() {
    return Task.builder()
        .name("name-1")
        .assignerId(USER_ID)
        .creatorId(USER_ID)
        .details("some-details")
        .status("some-status")
        .build();
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
      ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
      initializer.setConnectionFactory(connectionFactory);
      return initializer;
    }
  }

}