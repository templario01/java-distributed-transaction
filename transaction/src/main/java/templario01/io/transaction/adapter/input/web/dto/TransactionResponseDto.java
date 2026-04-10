package templario01.io.transaction.adapter.input.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import templario01.io.transaction.domain.entity.TransactionStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponseDto {
    private Long id;

    private UUID transactionExternalId;

    private String accountExternalIdDebit;

    private String accountExternalIdCredit;

    private TransactionStatusEnum transactionStatus;

    private Integer transferTypeId;

    private Double value;

    private LocalDateTime createdAt;
}
