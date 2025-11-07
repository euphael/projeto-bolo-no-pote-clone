package cake.backend.auth.controller;

import cake.backend.auth.entity.User;
import cake.backend.auth.model.RecoveryJwtTokenDto;
import cake.backend.auth.model.SignInUserDto;
import cake.backend.auth.model.SignUpUserDto;
import cake.backend.auth.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService service;

    /**
     * Endpoint para autenticação de usuário.
     * 
     * @param dto Dados de autenticação do usuário.
     * @return Token de autenticação.
     */
    @PostMapping("/signin")
    public ResponseEntity<RecoveryJwtTokenDto> signIn(@RequestBody SignInUserDto dto) {
        RecoveryJwtTokenDto token = service.authenticateUser(dto);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /**
     * Endpoint para cadastro de usuário.
     * 
     * @param dto Dados de cadastro do usuário.
     * @return Status da requisição.
     */
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpUserDto dto) {
        User user = service.createUser(dto);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Endpoint para teste de autenticação.
     * 
     * @return Mensagem de sucesso.
     */
    @GetMapping("/test")
    public ResponseEntity<String> authenticationTest() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    /**
     * Endpoint para teste de autenticação de cliente.
     * 
     * @return Mensagem de sucesso.
     */
    @GetMapping("/test/customer")
    public ResponseEntity<String> customerAuthenticationTest() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    /**
     * Endpoint para teste de autenticação de administrador.
     * 
     * @return Mensagem de sucesso.
     */
    @GetMapping("/test/administrator")
    public ResponseEntity<String> administratorAuthenticationTest() {
        return new ResponseEntity<>("Administrator autenticado com sucesso", HttpStatus.OK);
    }
}
