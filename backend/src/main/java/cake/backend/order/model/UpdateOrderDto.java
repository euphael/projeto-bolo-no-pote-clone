package cake.backend.order.model;

public record UpdateOrderDto(ProductItemDto product, Long userId, String endDate, String status) {
}
