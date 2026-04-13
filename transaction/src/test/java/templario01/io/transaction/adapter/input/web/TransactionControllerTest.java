package templario01.io.transaction.adapter.input.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import templario01.io.transaction.adapter.input.web.dto.TransactionRequestDto;
import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.application.CreateTransactionUseCase;
import templario01.io.transaction.application.FindTransactionUseCase;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private CreateTransactionUseCase createTransactionUseCase;

    @Mock
    private FindTransactionUseCase findTransactionUseCase;

    @InjectMocks
    private TransactionController controller;

    private TransactionRequestDto transactionRequest;

    private TransactionResponseDto transactionResponse;

    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionRequestDto();
        transactionRequest.setTransferTypeId(3);
        transactionRequest.setValue(100.0);
        transactionRequest.setAccountExternalIdDebit(UUID.randomUUID().toString());
        transactionRequest.setAccountExternalIdCredit(UUID.randomUUID().toString());

        LocalDateTime mockCreationDate = LocalDateTime.of(2025, 10, 1, 12, 0);

        transactionResponse = new TransactionResponseDto();
        transactionResponse.setTransferTypeId(transactionRequest.getTransferTypeId());
        transactionResponse.setValue(transactionRequest.getValue());
        transactionResponse.setAccountExternalIdDebit(transactionRequest.getAccountExternalIdDebit());
        transactionResponse.setAccountExternalIdCredit(transactionRequest.getAccountExternalIdCredit());
        transactionResponse.setCreatedAt(mockCreationDate);
        transactionResponse.setTransactionStatus(TransactionStatusEnum.PENDING);
    }

    @Test
    void shouldReturn201AndCallUseCase() {
        ResponseEntity<TransactionResponseDto> expectedResponse = ResponseEntity.status(201).body(transactionResponse);
        when(createTransactionUseCase.execute(any(TransactionRequestDto.class)))
                .thenReturn(Mono.just(transactionResponse));

        StepVerifier.create(controller.createTransfer(transactionRequest))
                .expectSubscription()
                .expectNext(expectedResponse)
                .verifyComplete();

        verify(createTransactionUseCase).execute(transactionRequest);
    }

}
