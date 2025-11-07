package cake.backend.feedback.model.dto;

public record CreateFeedbackDto(Long orderId, Long userId, Integer grade, String comment) {
}
