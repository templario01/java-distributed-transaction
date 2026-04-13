package templario01.io.transaction.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import templario01.io.transaction.adapter.input.web.dto.TransactionRequestDto;
import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.domain.entity.TransactionEntity;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;
import templario01.io.transaction.domain.repository.EventBrokerProducerRepository;
import templario01.io.transaction.domain.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateTransactionUseCaseTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private EventBrokerProducerRepository eventBrokerProducerRepository;

    @InjectMocks
    private CreateTransactionUseCase useCase;

    private TransactionEntity mockSavedTransaction;

    private TransactionResponseDto transactionResponse;

    private TransactionRequestDto transactionRequest;


    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionRequestDto();
        transactionRequest.setTransferTypeId(3);
        transactionRequest.setValue(100.0);
        transactionRequest.setAccountExternalIdDebit(UUID.randomUUID().toString());
        transactionRequest.setAccountExternalIdCredit(UUID.randomUUID().toString());

        LocalDateTime mockCreationDate = LocalDateTime.of(2025, 10, 1, 12, 0);

        mockSavedTransaction = new TransactionEntity();
        mockSavedTransaction.setId(1L);
        mockSavedTransaction.setTransactionExternalId(UUID.randomUUID());
        mockSavedTransaction.setTransactionStatus(TransactionStatusEnum.PENDING);
        mockSavedTransaction.setCreatedAt(mockCreationDate);
        mockSavedTransaction.setTransferTypeId(3);
        mockSavedTransaction.setValue(100.0);
        mockSavedTransaction.setAccountExternalIdDebit(UUID.randomUUID().toString());
        mockSavedTransaction.setAccountExternalIdCredit(UUID.randomUUID().toString());

        transactionResponse = new TransactionResponseDto();
        transactionResponse.setTransactionExternalId(mockSavedTransaction.getTransactionExternalId());
        transactionResponse.setTransactionStatus(mockSavedTransaction.getTransactionStatus());
        transactionResponse.setCreatedAt(mockSavedTransaction.getCreatedAt());
        transactionResponse.setTransferTypeId(mockSavedTransaction.getTransferTypeId());
        transactionResponse.setValue(mockSavedTransaction.getValue());
        transactionResponse.setAccountExternalIdDebit(mockSavedTransaction.getAccountExternalIdDebit());
        transactionResponse.setAccountExternalIdCredit(mockSavedTransaction.getAccountExternalIdCredit());
    }

    @Test
    void shouldCreateTransaction() {
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(Mono.just(mockSavedTransaction));
        when(eventBrokerProducerRepository.sendMessage(any(), any())).thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(transactionRequest))
                .expectSubscription()
                .expectNext(transactionResponse)
                .verifyComplete();
        verify(transactionRepository).save(argThat(entity ->
                entity.getValue().equals(transactionRequest.getValue()) &&
                        entity.getAccountExternalIdDebit().equals(transactionRequest.getAccountExternalIdDebit()) &&
                        entity.getAccountExternalIdCredit().equals(transactionRequest.getAccountExternalIdCredit()) &&
                        entity.getTransferTypeId().equals(transactionRequest.getTransferTypeId()) &&
                        entity.getTransactionStatus() == TransactionStatusEnum.PENDING &&
                        entity.getTransactionExternalId() != null && entity.getCreatedAt() != null
        ));
        verify(eventBrokerProducerRepository).sendMessage(mockSavedTransaction, "payment.transaction");
    }
}
