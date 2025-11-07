package cake.backend.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cake.backend.auth.entity.User;
import cake.backend.auth.model.UserDetailsImpl;
import cake.backend.auth.repository.IUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private IUserRepository repository;
    
    /**
     * Carrega um usuário pelo nome de usuário.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return new UserDetailsImpl(user);
    }
}
