package cake.backend.auth.model;

import cake.backend.auth.entity.Role;

import java.util.List;

public record RecoveryUserDto(Long id, String email, List<Role> roles) {
}
