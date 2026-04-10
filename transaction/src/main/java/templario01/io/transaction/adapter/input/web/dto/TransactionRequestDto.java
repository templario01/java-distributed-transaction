package templario01.io.transaction.adapter.input.web.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequestDto {

    @NotBlank(message = "Account external ID debit is required")
    private String accountExternalIdDebit;

    @NotBlank(message = "Account external ID credit is required")
    private String accountExternalIdCredit;

    @NotNull(message = "Transfer type ID is required")
    private Integer transferTypeId;

    @NotNull(message = "Value is required")
    @Positive(message = "Value must be positive")
    private Double value;
}
