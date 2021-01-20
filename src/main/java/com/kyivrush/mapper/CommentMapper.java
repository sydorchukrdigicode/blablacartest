package com.kyivrush.mapper;

import com.kyivrush.domain.Comment;
import com.kyivrush.domain.CommentWithDetails;
import com.kyivrush.dto.CommentDto;
import com.kyivrush.dto.CommentWithDetailsDto;
import com.kyivrush.dto.CommentWithIdDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends BasicMapper {

  @Mapping(source = "authorId", target = "authorId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "commentId", target = "commentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "taskId", target = "taskId", qualifiedByName = "mapUUIDToString")
  CommentWithIdDto map(Comment comment);

  @Mapping(source = "authorId", target = "authorId", qualifiedByName = "mapStringToUUID")
  @Mapping(source = "taskId", target = "taskId", qualifiedByName = "mapStringToUUID")
  Comment map(CommentDto comment);

  @Mapping(source = "authorId", target = "authorId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "commentId", target = "commentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "taskId", target = "taskId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "author.userId", target = "author.userId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "author.departmentId", target = "author.departmentId", qualifiedByName = "mapUUIDToString")
  CommentWithDetailsDto map(CommentWithDetails comment);

  List<CommentWithDetailsDto> map(List<CommentWithDetails> comments);
}
