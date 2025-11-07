package cake.backend.client.model.dto;

import cake.backend.auth.roles.EnumRoleName;

public record CreateClientDto(String name, String email, String password, EnumRoleName role) {
}
