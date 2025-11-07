package cake.backend.auth.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cake.backend.auth.entity.User;

public class UserDetailsImpl implements UserDetails {
    private final User user;

    /**
     * Construtor da implementação de detalhes de usuário.
     * 
     * @param user Usuário.
     */
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    /**
     * Obtém as autoridades do usuário.
     * 
     * @return Autoridades do usuário.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    /**
     * Obtém a senha do usuário.
     * 
     * @return Senha do usuário.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Obtém o nome de usuário.
     * 
     * @return Nome de usuário.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Verifica se a conta do usuário não expirou.
     * 
     * @return Verdadeiro se a conta do usuário não expirou, caso contrário falso.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Verifica se a conta do usuário não está bloqueada.
     * 
     * @return Verdadeiro se a conta do usuário não está bloqueada, caso contrário
     *         falso.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Verifica se as credenciais do usuário não expiraram.
     * 
     * @return Verdadeiro se as credenciais do usuário não expiraram, caso contrário
     *         falso.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifica se o usuário está habilitado.
     * 
     * @return Verdadeiro se o usuário está habilitado, caso contrário falso.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
