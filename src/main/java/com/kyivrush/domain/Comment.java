package com.kyivrush.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table("comments")
public class Comment {

  @Id
  private UUID commentId;
  private UUID taskId;
  private String value;
  private UUID authorId;
  @CreatedDate
  private Instant creationTime;
}
