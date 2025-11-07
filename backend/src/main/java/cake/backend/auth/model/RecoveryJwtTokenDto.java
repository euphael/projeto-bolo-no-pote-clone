package cake.backend.auth.model;

public record RecoveryJwtTokenDto(Long userId, String token) {
}
