package templario01.io.transaction.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.domain.entity.EntityNotFoundException;
import templario01.io.transaction.domain.entity.TransactionEntity;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;
import templario01.io.transaction.domain.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindTransactionUseCaseTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private FindTransactionUseCase useCase;

    private TransactionEntity mockTransaction;

    private TransactionResponseDto transactionResponse;

    @BeforeEach
    void setUp() {
        UUID externalId = UUID.randomUUID();
        LocalDateTime mockDate = LocalDateTime.of(2025, 10, 1, 12, 0);

        mockTransaction = new TransactionEntity();
        mockTransaction.setId(1L);
        mockTransaction.setTransactionExternalId(externalId);
        mockTransaction.setCreatedAt(mockDate);
        mockTransaction.setTransferTypeId(3);
        mockTransaction.setValue(100.0);
        mockTransaction.setAccountExternalIdDebit(UUID.randomUUID().toString());
        mockTransaction.setAccountExternalIdCredit(UUID.randomUUID().toString());
        mockTransaction.setTransactionStatus(TransactionStatusEnum.valueOf("APPROVED"));

        transactionResponse =  new TransactionResponseDto();
        transactionResponse.setTransactionExternalId(mockTransaction.getTransactionExternalId());
        transactionResponse.setTransactionStatus(mockTransaction.getTransactionStatus());
        transactionResponse.setValue(mockTransaction.getValue());
        transactionResponse.setAccountExternalIdDebit(mockTransaction.getAccountExternalIdDebit());
        transactionResponse.setAccountExternalIdCredit(mockTransaction.getAccountExternalIdCredit());
        transactionResponse.setCreatedAt(mockTransaction.getCreatedAt());
        transactionResponse.setTransferTypeId(mockTransaction.getTransferTypeId());
    }

    @Test
    void shouldFindSTransactionWhenTransactionExists() {
        UUID transactionExternalId = mockTransaction.getTransactionExternalId();
        when(transactionRepository.findByTransactionExternalId(transactionExternalId))
                .thenReturn(Mono.just(mockTransaction));

        StepVerifier.create(useCase.execute(transactionExternalId))
                .expectSubscription()
                .expectNext(transactionResponse)
                .verifyComplete();
        verify(transactionRepository, times(1)).findByTransactionExternalId(transactionExternalId);
    }

    @Test
    void shouldReturnEntityNotFoundExceptionWhenTransactionDoesNotExist() {
        UUID transactionExternalId = mockTransaction.getTransactionExternalId();

        when(transactionRepository.findByTransactionExternalId(transactionExternalId))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(transactionExternalId))
                .expectSubscription()
                .expectError(EntityNotFoundException.class)
                .verify();
        verify(transactionRepository, times(1)).findByTransactionExternalId(transactionExternalId);
    }
}
