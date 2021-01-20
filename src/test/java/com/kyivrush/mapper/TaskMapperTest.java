package com.kyivrush.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kyivrush.domain.Department;
import com.kyivrush.domain.Task;
import com.kyivrush.domain.TaskWithDetails;
import com.kyivrush.domain.User;
import com.kyivrush.domain.UserWithDetails;
import com.kyivrush.dto.DepartmentDto;
import com.kyivrush.dto.TaskDto;
import com.kyivrush.dto.TaskWithDetailsDto;
import com.kyivrush.dto.TaskWithIdDto;
import com.kyivrush.dto.UserDto;
import com.kyivrush.dto.UserWithDetailsDto;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@Slf4j
class TaskMapperTest {

  private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private static final UUID UUID_3 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b913");

  @Test
  void shouldMapFromDomainToDto() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    Task input = Task.builder()
        .taskId(UUID_1)
        .creationTime(time)
        .name("name-1")
        .assignerId(UUID_2)
        .creatorId(UUID_3)
        .details("some-details")
        .status("some-status")
        .build();

    TaskWithIdDto expected = TaskWithIdDto.builder()
        .taskId("460632f0-7a32-48b1-9186-324c0468b911")
        .creationTime(time)
        .name("name-1")
        .assignerId("460632f0-7a32-48b1-9186-324c0468b912")
        .creatorId("460632f0-7a32-48b1-9186-324c0468b913")
        .details("some-details")
        .status("some-status")
        .build();

    //when
    TaskDto actual = taskMapper.map(input);
    log.info("actual = {}", actual);

    //then
    assertEquals(expected, actual);
  }

  @Test
  void shouldMapTaskWithDetailsDto() {
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
                    .name("dep")
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
                    .name("dep")
                    .build())
                .build()
        )
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
                    .name("dep")
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
                    .name("dep")
                    .build())
                .build()
        )
        .build();

    //when
    TaskDto actual = taskMapper.map(input);
    log.info("actual = {}", actual);

    //then
    assertEquals(expected, actual);
  }

  @Test
  void shouldMapFromDtoToDomain() {
    //given
    Task expected = Task.builder()
        .name("name-1")
        .assignerId(UUID_2)
        .creatorId(UUID_3)
        .details("some-details")
        .status("some-status")
        .build();

    TaskDto input = TaskDto.builder()
        .name("name-1")
        .assignerId("460632f0-7a32-48b1-9186-324c0468b912")
        .creatorId("460632f0-7a32-48b1-9186-324c0468b913")
        .details("some-details")
        .status("some-status")
        .build();

    //when
    Task actual = taskMapper.map(input);
    log.info("actual = {}", actual);

    //then
    assertEquals(expected, actual);
  }
}