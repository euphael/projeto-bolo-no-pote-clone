package cake.backend.order.model;

public record ObtainOrderDto(Long id, String userName, Long userId, String productName, Long quantity, String status, Integer grade, String comment) {
}
