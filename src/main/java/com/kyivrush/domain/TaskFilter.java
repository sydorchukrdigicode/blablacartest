package com.kyivrush.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class TaskFilter {

  private UUID creatorDepartmentId;
  private UUID assignerDepartmentId;
  private Boolean isAscOrder;
}
