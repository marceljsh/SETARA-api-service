package org.synrgy.setara.contact.dto;

import lombok.Builder;
import lombok.Data;
import org.synrgy.setara.transaction.model.TransactionType;

@Data
@Builder
public class FavoriteTransactionsResponse {
    TransactionType type;
    String bankOrEwalletName;
    String name;
}
