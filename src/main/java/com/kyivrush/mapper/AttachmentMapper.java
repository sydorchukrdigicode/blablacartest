package com.kyivrush.mapper;

import com.kyivrush.domain.Attachment;
import com.kyivrush.dto.AttachmentDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper extends BasicMapper {

  @Mapping(source = "attachmentId", target = "attachmentId", qualifiedByName = "mapUUIDToString")
  @Mapping(source = "taskId", target = "taskId", qualifiedByName = "mapUUIDToString")
  AttachmentDto map(Attachment attachment);

  List<AttachmentDto> map(List<Attachment> attachment);
}
