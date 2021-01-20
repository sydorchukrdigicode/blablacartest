package com.kyivrush.mapper;

import com.kyivrush.domain.Attachment;
import com.kyivrush.dto.AttachmentDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@Slf4j
class AttachmentMapperTest {

  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");

  private final AttachmentMapper attachmentMapper = Mappers.getMapper(AttachmentMapper.class);

  @Test
  void shouldMap() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    Attachment input = Attachment.builder()
        .attachmentId(UUID_1)
        .taskId(UUID_2)
        .fileName("fl-1")
        .link("link")
        .creationTime(time)
        .build();
    AttachmentDto expected = AttachmentDto.builder()
        .attachmentId(UUID_1.toString())
        .taskId(UUID_2.toString())
        .fileName("fl-1")
        .link("link")
        .creationTime(time)
        .build();

    //when
    AttachmentDto actual = attachmentMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void shouldMapList() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    List<Attachment> input = List.of(Attachment.builder()
        .attachmentId(UUID_1)
        .taskId(UUID_2)
        .fileName("fl-1")
        .link("link")
        .creationTime(time)
        .build());
    List<AttachmentDto> expected = List.of(AttachmentDto.builder()
        .attachmentId(UUID_1.toString())
        .taskId(UUID_2.toString())
        .fileName("fl-1")
        .link("link")
        .creationTime(time)
        .build());

    //when
    List<AttachmentDto> actual = attachmentMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }
}