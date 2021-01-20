package com.kyivrush.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("attachments")
public class Attachment {

  @Id
  private UUID attachmentId;
  private UUID taskId;
  private String fileName;
  private String link;
  @CreatedDate
  private Instant creationTime;
}
