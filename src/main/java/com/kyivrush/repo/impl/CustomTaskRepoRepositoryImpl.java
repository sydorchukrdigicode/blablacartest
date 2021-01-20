package com.kyivrush.repo.impl;

import static java.util.Objects.nonNull;

import com.kyivrush.domain.Department;
import com.kyivrush.domain.TaskFilter;
import com.kyivrush.domain.TaskWithDetails;
import com.kyivrush.domain.UserWithDetails;
import com.kyivrush.repo.CustomTaskRepoRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.DatabaseClient.GenericExecuteSpec;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
//todo rewrite on more elegant way @see org.springframework.data.r2dbc.core.DatabaseClient
//todo jooq?
public class CustomTaskRepoRepositoryImpl implements CustomTaskRepoRepository {

  private static final String SELECT_FROM_TASKS_SQL =
      "select t.task_id, t.name, t.details, t.status, t.creation_time, t.creator_id, t.assigner_id, "
          + " c.user_id as creator_user_id, c.name as creator_name, "
          + " c.department_id as creator_department_id, "
          + " a.user_id as assigner_user_id, a.name as assigner_name, "
          + " a.department_id as assigner_department_id ,"
          + " dc.department_id as dc_department_id, dc.name as dc_name, "
          + " da.department_id as da_department_id, da.name as da_name "
          + "from tasks t "
          + " left join users c on t.creator_id = c.user_id "
          + " left join users a on t.assigner_id = a.user_id "
          + " left join departments dc on dc.department_id = c.department_id "
          + " left join departments da on da.department_id = a.department_id ";
  private final DatabaseClient databaseClient;

  @Override
  public Flux<TaskWithDetails> findByFilter(TaskFilter taskFilter) {

    String query = SELECT_FROM_TASKS_SQL + " where 1 = 1 ";
    List<Tuple<String, Object>> binds = new ArrayList<>();

    if (nonNull(taskFilter) && nonNull(taskFilter.getAssignerDepartmentId())) {
      query += " and da.department_id = :p_da_department_id ";
      binds.add(new Tuple<>("p_da_department_id", taskFilter.getAssignerDepartmentId()));
    }

    if (nonNull(taskFilter) && nonNull(taskFilter.getCreatorDepartmentId())) {
      query += " and dc.department_id = :p_dc_department_id ";
      binds.add(new Tuple<>("p_dc_department_id", taskFilter.getCreatorDepartmentId()));
    }

    if (nonNull(taskFilter) && nonNull(taskFilter.getIsAscOrder()) && taskFilter.getIsAscOrder()) {
      query += " order by t.creation_time ";
    } else {
      query += " order by t.creation_time desc ";
    }

    GenericExecuteSpec sql = databaseClient
        .sql(query);

    for (int i = 0; i < binds.size(); i++) {
      sql = sql.bind(binds.get(i).getT1(), binds.get(i).getT2());
    }

    return sql
        .map(rowMapper())
        .all()
        .cast(TaskWithDetails.class);
  }

  @Override
  public Flux<TaskWithDetails> findWithUserById(UUID taskId) {
    return databaseClient
        .sql(
            SELECT_FROM_TASKS_SQL
                + "where t.task_id = :task_id ")
        .bind("task_id", taskId)
        .map(rowMapper())
        .all()
        .cast(TaskWithDetails.class);
  }

  @Getter
  @RequiredArgsConstructor
  private static class Tuple<T1, T2> {

    private final T1 t1;
    private final T2 t2;
  }

  private BiFunction<Row, RowMetadata, ?> rowMapper() {
    return (row, rowMetadata) ->
        TaskWithDetails.builder()
            .taskId(row.get("task_id", UUID.class))
            .name(row.get("name", String.class))
            .details(row.get("details", String.class))
            .status(row.get("status", String.class))
            .creationTime(row.get("creation_time", Instant.class))
            .creatorId(row.get("creator_id", UUID.class))
            .assignerId(row.get("assigner_id", UUID.class))
            .creator(UserWithDetails.builder()
                .userId(row.get("creator_user_id", UUID.class))
                .name(row.get("creator_name", String.class))
                .departmentId(row.get("creator_department_id", UUID.class))
                .department(Department.builder()
                    .departmentId(row.get("dc_department_id", UUID.class))
                    .name(row.get("dc_name", String.class))
                    .build())
                .build())
            .assigner(UserWithDetails.builder()
                .userId(row.get("assigner_user_id", UUID.class))
                .name(row.get("assigner_name", String.class))
                .departmentId(row.get("assigner_department_id", UUID.class))
                .department(Department.builder()
                    .departmentId(row.get("da_department_id", UUID.class))
                    .name(row.get("da_name", String.class))
                    .build())
                .build())
            .build();
  }
}
