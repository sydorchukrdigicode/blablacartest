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
@Table("tasks")
public class Task {

  @Id
  private UUID taskId;
  private String name;
  @CreatedDate
  private Instant creationTime;
  private String details;
  private UUID creatorId;
  private UUID assignerId;
  private String status;
}
