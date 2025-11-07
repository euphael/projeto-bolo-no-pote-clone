package cake.backend.mercadopago.model;

public record RequestPayerPaymentDto(String email, String firstName, RequestIdentificationDto identification) {
}
