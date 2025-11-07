package cake.backend.mercadopago.model;

import java.math.BigDecimal;

public record RequestCardPaymentDto(
        BigDecimal transaction_amount,
        String token,
        String description,
        Integer installments,
        String paymentMethodId,
        RequestPayerPaymentDto payer) {
}
