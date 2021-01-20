package com.kyivrush.mapper;

import com.kyivrush.domain.Comment;
import com.kyivrush.domain.CommentWithDetails;
import com.kyivrush.domain.User;
import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithDetailsDto;
import com.kyivrush.dto.CommentWithIdDto;
import com.kyivrush.dto.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@Slf4j
class CommentMapperTest {

  private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);
  private static final UUID UUID_1 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b911");
  private static final UUID UUID_2 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b912");
  private static final UUID UUID_3 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b913");
  private static final UUID UUID_4 = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b914");

  @Test
  void shouldMapFromDtoToDomain() {
    //given
    Comment expected = Comment.builder()
        .authorId(UUID_1)
        .taskId(UUID_2)
        .value("comment")
        .build();
    CommentDto input = CommentDto.builder()
        .authorId("460632f0-7a32-48b1-9186-324c0468b911")
        .taskId("460632f0-7a32-48b1-9186-324c0468b912")
        .value("comment")
        .build();

    //when
    Comment actual = commentMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void shouldMapFromDtoWithIdToDomain() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    Comment input = Comment.builder()
        .commentId(UUID_1)
        .authorId(UUID_2)
        .taskId(UUID_3)
        .value("comment")
        .creationTime(time)
        .build();
    CommentWithIdDto expected = CommentWithIdDto.builder()
        .commentId("460632f0-7a32-48b1-9186-324c0468b911")
        .authorId("460632f0-7a32-48b1-9186-324c0468b912")
        .taskId("460632f0-7a32-48b1-9186-324c0468b913")
        .value("comment")
        .creationTime(time)
        .build();

    //when
    CommentWithIdDto actual = commentMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void shouldMapCommentWithDetails() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    CommentWithDetails input = CommentWithDetails.builder()
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
    CommentWithDetailsDto expected = CommentWithDetailsDto.builder()
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
        .build();

    //when
    CommentWithIdDto actual = commentMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }

  @Test
  void shouldMapCommentWithDetailsList() {
    //given
    Instant time = Instant.ofEpochSecond(1000L);
    List<CommentWithDetails> input = List.of(
        CommentWithDetails.builder()
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
            .build());
    List<CommentWithDetailsDto> expected = List.of(
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
            .build());

    //when
    List<CommentWithDetailsDto> actual = commentMapper.map(input);
    log.info("actual = {}", actual);

    //then
    Assertions.assertEquals(expected, actual);
  }
}