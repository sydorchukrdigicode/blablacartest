package com.kyivrush.repo;

import com.kyivrush.config.DatabaseConfig;
import com.kyivrush.config.FlywayConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest
@Testcontainers
@Import(value = {FlywayConfig.class, DatabaseConfig.class})
public abstract class GenericRepoIntegrationTest {

  @Container
  static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:10.15");

  @DynamicPropertySource
  static void registerDynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://"
        + postgreSQLContainer.getHost() + ":" + postgreSQLContainer.getFirstMappedPort()
        + "/" + postgreSQLContainer.getDatabaseName());
    registry.add("spring.r2dbc.username", () -> postgreSQLContainer.getUsername());
    registry.add("spring.r2dbc.password", () -> postgreSQLContainer.getPassword());

    registry.add("spring.flyway.url", () -> postgreSQLContainer.getJdbcUrl());
    registry.add("spring.flyway.user", () -> postgreSQLContainer.getUsername());
    registry.add("spring.flyway.password", () -> postgreSQLContainer.getPassword());

    log.info("spring.flyway.url = {}", postgreSQLContainer.getJdbcUrl());
    log.info("spring.flyway.user = {}", postgreSQLContainer.getUsername());
    log.info("spring.flyway.password = {}", postgreSQLContainer.getPassword());
  }
}
