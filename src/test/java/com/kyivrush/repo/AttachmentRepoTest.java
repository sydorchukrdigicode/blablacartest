package com.kyivrush.repo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kyivrush.domain.Attachment;
import io.r2dbc.spi.ConnectionFactory;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.ConnectionFactoryUtils;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
class AttachmentRepoTest extends GenericRepoIntegrationTest {

  private static final UUID TASK_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b910");
  private static final UUID ATTACHMENT_ID = UUID.fromString("460632f0-7a32-48b1-9186-324c0468b914");
  private static final UUID ATTACHMENT_ID_2 = UUID
      .fromString("460632f0-7a32-48b1-9186-324c0468b915");

  @Autowired
  private AttachmentRepo attachmentRepo;

  @Autowired
  private ConnectionFactory connectionFactory;

  @BeforeEach
  void setUp() {
    ConnectionFactoryUtils.getConnection(connectionFactory)
        .flatMap(connection -> ScriptUtils
            .executeSqlScript(connection, new ClassPathResource("data.sql")))
        .block();
  }

  @Test
  void shouldFindByTaskId() {
    //when
    Mono<List<Attachment>> result = attachmentRepo.findByTaskId(TASK_ID)
        .log()
        .collectList();

    //then
    result.as(StepVerifier::create)
        .expectSubscription()
        .assertNext(attachments -> {
          List<UUID> actualIds = attachments.stream().map(Attachment::getAttachmentId)
              .collect(Collectors.toList());
          Assertions.assertEquals(2, attachments.size());
          assertTrue(actualIds.contains(ATTACHMENT_ID));
          assertTrue(actualIds.contains(ATTACHMENT_ID_2));
        })
        .verifyComplete();
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
      ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
      initializer.setConnectionFactory(connectionFactory);
      return initializer;
    }
  }
}