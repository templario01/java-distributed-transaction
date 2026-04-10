package templario01.io.antifraud.adapter.input.event.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventInbound {

    private UUID transactionExternalId;

    private String accountExternalIdDebit;

    private String accountExternalIdCredit;

    private Integer transferTypeId;

    private Double value;

    private LocalDateTime createdAt;

}
