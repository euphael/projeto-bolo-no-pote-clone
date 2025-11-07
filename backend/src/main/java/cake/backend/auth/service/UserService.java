package cake.backend.auth.service;

import cake.backend.auth.configuration.SecurityConfiguration;
import cake.backend.auth.entity.Role;
import cake.backend.auth.entity.User;
import cake.backend.auth.model.RecoveryJwtTokenDto;
import cake.backend.auth.model.SignInUserDto;
import cake.backend.auth.model.SignUpUserDto;
import cake.backend.auth.model.UserDetailsImpl;
import cake.backend.auth.repository.IUserRepository;
import cake.backend.auth.roles.EnumRoleName;
import cake.backend.client.model.dto.CreateClientDto;
import cake.backend.client.model.dto.UpdateClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private IUserRepository repository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    /**
     * Autentica um usuário.
     * 
     * @param dto Dados de autenticação do usuário.
     * @return Token de recuperação JWT.
     */
    public RecoveryJwtTokenDto authenticateUser(SignInUserDto dto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                dto.email(), dto.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Optional<User> user = repository.findByEmail(dto.email());
        if(!user.isPresent()) return null;
        Long id = user.get().getId();
        return new RecoveryJwtTokenDto(id, jwtTokenService.generateToken(userDetails));
    }

    /**
     * Cria um usuário.
     * 
     * @param dto Dados de criação do usuário.
     */
    public User createUser(SignUpUserDto dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            return null;
        }
        User newUser = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(securityConfiguration.passwordEncoder().encode(dto.password()))
                .roles(List.of(Role.builder().name(EnumRoleName.ROLE_CUSTOMER).build()))
                .build();
        if (newUser == null)
            return null;
        return repository.save(newUser);
    }

    /**
     * Criar um usuário, somente com permisão de administrador
     *
     * @param dto Dados de cadastro.
     * */
    public User createUserByAdmin(CreateClientDto dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            return null;
        }
        User newUser = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(securityConfiguration.passwordEncoder().encode(dto.password()))
                .roles(List.of(Role.builder().name(dto.role()).build()))
                .build();
        if (newUser == null)
            return null;
        return repository.save(newUser);
    }

    /**
     * Criar um update, somente com permisão de administrador
     *
     * @param dto Dados de atualização.
     * */
    public User updateUserByAdmin(Long id, UpdateClientDto dto) {
        if(id == null) return null;
        User user = repository.findById(id).orElse(null);
        if(user == null) return null;
        Role role = user.getRoles().get(0);
        role.setName(dto.role());
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(securityConfiguration.passwordEncoder().encode(dto.password()));
        return repository.save(user);
    }

    /**
     * Obter todos os usuários.
     * */
    public List<User> getAll() {
        return repository.findAll();
    }

    /**
     * Obter um usuário.
     * 
     * @param id ID do usuário.
     * @return Usuário referente ao ID.
     */
    public User get(Long id) {
        if (id == null)
            return null;
        return repository.findById(id).orElse(null);
    }

    /**
     * Remover um usuário do banco de dados.
     *
     * @param id Id do usuário.
     * */
    public void delete(Long id) {
        if (id == null) return;
        User user = repository.findById(id).orElse(null);
        if(user == null) return;
        repository.delete(user);
    }
}
