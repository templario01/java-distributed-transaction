package templario01.io.transaction.adapter.input.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;
import templario01.io.transaction.adapter.input.web.dto.TransactionRequestDto;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class CreateTransactionIntegrationTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    @ServiceConnection
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("apache/kafka:3.7.0"));

    @Autowired
    private WebTestClient webTestClient;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void shouldCreateTransactionSuccessfully() {
        TransactionRequestDto request = TransactionRequestDto.builder()
                .accountExternalIdCredit(UUID.randomUUID().toString())
                .accountExternalIdDebit(UUID.randomUUID().toString())
                .transferTypeId(3)
                .value(100.0)
                .build();

        webTestClient.post()
                .uri("/transaction")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.transferTypeId").isEqualTo(request.getTransferTypeId())
                .jsonPath("$.accountExternalIdCredit").isEqualTo(request.getAccountExternalIdCredit())
                .jsonPath("$.accountExternalIdDebit").isEqualTo(request.getAccountExternalIdDebit())
                .jsonPath("$.value").isEqualTo(request.getValue())
                .jsonPath("$.transactionExternalId").exists()
                .jsonPath("$.createdAt").exists()
                .jsonPath("$.transactionStatus").isEqualTo("PENDING");

    }
}