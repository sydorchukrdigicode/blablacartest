package com.kyivrush.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@JsonInclude(Include.NON_NULL)
public class TaskWithDetailsDto extends TaskWithIdDto {

  private UserWithDetailsDto creator;
  private UserWithDetailsDto assigner;
  private List<AttachmentDto> attachments;
  private List<CommentWithDetailsDto> comments;
}
