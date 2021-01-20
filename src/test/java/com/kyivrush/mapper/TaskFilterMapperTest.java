package com.kyivrush.mapper;

import com.kyivrush.domain.TaskFilter;
import com.kyivrush.dto.TaskFilterDto;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@Slf4j
class TaskFilterMapperTest {

  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");

  private final TaskFilterMapper taskFilterMapper = Mappers.getMapper(TaskFilterMapper.class);

  @Test
  void shouldMap() {
    //given
    TaskFilterDto input = TaskFilterDto.builder()
        .assignerDepartmentId(UUID_1.toString())
        .creatorDepartmentId(UUID_2.toString())
        .isAscOrder(true)
        .build();
    TaskFilter expected = TaskFilter.builder()
        .assignerDepartmentId(UUID_1)
        .creatorDepartmentId(UUID_2)
        .isAscOrder(true)
        .build();

    //when
    TaskFilter actual = taskFilterMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }
}