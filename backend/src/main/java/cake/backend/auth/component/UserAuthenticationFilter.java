package cake.backend.auth.component;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import cake.backend.auth.configuration.SecurityConfiguration;
import cake.backend.auth.entity.User;
import cake.backend.auth.model.UserDetailsImpl;
import cake.backend.auth.repository.IUserRepository;
import cake.backend.auth.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private JwtTokenService jwtTokenService;
    private IUserRepository userRepository;

    @Autowired
    public UserAuthenticationFilter(JwtTokenService jwtTokenService, IUserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    
    /**
     * Intercepta as requisições para autenticar o usuário.
     * 
     * @param request     Requisição HTTP.
     * @param response    Resposta HTTP.
     * @param filterChain Filtro de cadeia.
     */
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) {
        if (checkIfEndpointIsNotPublic(request)) {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                User user = userRepository.findByEmail(subject).orElse(null);
                if(user == null) return;
                UserDetailsImpl userDetails = new UserDetailsImpl(user);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new IllegalArgumentException("O token está ausente");
            }
        }
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera o token do cabeçalho da requisição.
     * 
     * @param request Requisição HTTP.
     * @return Token do cabeçalho da requisição.
     */
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) {
            return null;
        }
        return authorizationHeader.replace("Bearer ", "");
    }

    /**
     * Verifica se o endpoint não é público.
     * 
     * @param request Requisição HTTP.
     * @return true se o endpoint não é público, caso contrário, false.
     */
    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return !Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).contains(requestUri);
    }
}
