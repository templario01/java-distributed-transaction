package templario01.io.transaction.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import templario01.io.transaction.adapter.input.event.dto.ValidatedTransactionEventInbound;
import templario01.io.transaction.domain.entity.TransactionEntity;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;
import templario01.io.transaction.domain.repository.TransactionRepository;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UpdateValidatedTransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private UpdateValidatedTransactionUseCase useCase;

    private ValidatedTransactionEventInbound eventData;
    private TransactionEntity mockTransaction;

    @BeforeEach
    void setUp() {
        UUID externalId = UUID.randomUUID();

        eventData = new ValidatedTransactionEventInbound();
        eventData.setTransactionExternalId(externalId);
        eventData.setTransactionStatus(TransactionStatusEnum.valueOf("APPROVED"));

        mockTransaction = new TransactionEntity();
        mockTransaction.setTransactionExternalId(externalId);
        mockTransaction.setTransactionStatus(TransactionStatusEnum.valueOf("PENDING"));
    }

    @Test
    void shouldUpdateStatusWhenTransactionExists() {
        when(transactionRepository.findByTransactionExternalId(eventData.getTransactionExternalId()))
                .thenReturn(Mono.just(mockTransaction));

        when(transactionRepository.save(any(TransactionEntity.class)))
                .thenReturn(Mono.just(mockTransaction));

        StepVerifier.create(useCase.execute(eventData))
                .expectSubscription()
                .verifyComplete(); // Esperamos que termine sin errores (Mono<Void>)

        verify(transactionRepository, times(1)).findByTransactionExternalId(any());
        verify(transactionRepository, times(1)).save(argThat(t ->
                t.getTransactionStatus() == TransactionStatusEnum.APPROVED
        ));
    }

    @Test
    void shouldDoNothingWhenTransactionDoesNotExist() {
        when(transactionRepository.findByTransactionExternalId(eventData.getTransactionExternalId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute(eventData))
                .verifyComplete();

        verify(transactionRepository, never()).save(any());
    }
}