package cake.backend.auth.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cake.backend.auth.component.UserAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
        private UserAuthenticationFilter userAuthenticationFilter;

        @Autowired
        public SecurityConfiguration(UserAuthenticationFilter userAuthenticationFilter) {
                this.userAuthenticationFilter = userAuthenticationFilter;
        }

        /**
         * Endpoints que não requerem autenticação.
         */
        public static final String[] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
                "/",
                "/auth/signin",
                "/auth/signup",
                "/product/create",
                "/product/list",
                "/product/update",
                "/product/get",
                "/product/delete",
                "/image/upload",
                "/image/get",
                "/cart/create",
                "/cart/getById",
                "/cart/add",
                "/cart/remove",
                "/cart/clear",
                "/catalog/all",
                "/catalog/pagination",
                "/catalog/search",
                "/client/getAll",
                "/client/get",
                "/client/create",
                "/client/update",
                "/client/delete",
                "/order/getAll",
                "/order/get",
                "/order/create",
                "/order/update",
                "/order/delete",
                "/order/cancel",
                "/order/all",
                "/feedback/create",
                "/feedback/getByUserId",
                "/feedback/getByOrderId",
                "/mp/card",
                "/websocket"
        };

        /**
         * Endpoints que requerem autenticação.
         */
        protected static final String[] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {
                        "/auth/test"
        };

        /**
         * Endpoints para o perfil de cliente.
         */
        protected static final String[] ENDPOINTS_CUSTOMER = {
                        "/auth/test/customer"
        };

        /**
         * Endpoints para o perfil de administrador.
         */
        protected static final String[] ENDPOINTS_ADMINISTRATOR = {
                        "/auth/test/administrator"
        };

        /**
         * Configura as regras de segurança da aplicação.
         * 
         * @param httpSecurity Objeto que permite configurar as regras de segurança.
         * @return Objeto que representa a cadeia de filtros de segurança.
         * @throws Exception Exceção lançada caso ocorra um erro ao configurar as regras
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                return httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(AbstractHttpConfigurer::disable)
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED)
                                                .permitAll()
                                                .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED)
                                                .authenticated()
                                                .requestMatchers(ENDPOINTS_ADMINISTRATOR)
                                                .hasRole("ADMINISTRATOR")
                                                .requestMatchers(ENDPOINTS_CUSTOMER)
                                                .hasRole("CUSTOMER").anyRequest().denyAll())
                                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        /**
         * Retorna o gerenciador de autenticação.
         * 
         * @param authenticationConfiguration Configuração de autenticação.
         * @return Gerenciador de autenticação.
         * @throws Exception Exceção lançada caso ocorra um erro ao configurar o
         *                   gerenciador de autenticação
         */
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        /**
         * Retorna o codificador de senhas.
         * 
         * @return Codificador de senhas.
         */
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
