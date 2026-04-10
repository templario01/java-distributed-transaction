package templario01.io.transaction.adapter.input.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatedTransactionEventInbound {
    private UUID transactionExternalId;

    private TransactionStatusEnum transactionStatus;
}
