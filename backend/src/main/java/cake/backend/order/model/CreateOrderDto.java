package cake.backend.order.model;

import java.util.List;

public record CreateOrderDto(ProductItemDto product, Long userId, String endDate) {
}
