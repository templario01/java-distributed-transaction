package templario01.io.transaction.adapter.input.web;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import templario01.io.transaction.adapter.input.web.dto.TransactionRequestDto;
import templario01.io.transaction.adapter.input.web.dto.TransactionResponseDto;
import templario01.io.transaction.application.CreateTransactionUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final CreateTransactionUseCase createTransactionUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
    }
    @PostMapping
    public Mono<ResponseEntity<TransactionResponseDto>> createTransfer(@Valid @RequestBody TransactionRequestDto request) {
        return this.createTransactionUseCase.execute(request)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
                .onErrorResume(error -> {
                    log.error("Error during transaction creation", error);
                    TransactionResponseDto errorResponse = new TransactionResponseDto();
                    errorResponse.setTransactionStatus(templario01.io.transaction.domain.entity.TransactionStatusEnum.REJECTED);
                    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                });
    }

}
