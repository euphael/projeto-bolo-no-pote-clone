package cake.backend.client.controller;

import cake.backend.auth.entity.User;
import cake.backend.auth.service.UserService;
import cake.backend.client.model.dto.CreateClientDto;
import cake.backend.client.model.dto.UpdateClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private UserService userService;

    /**
     * Obter todos os clientes do banco de dados.
     * */
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getALl() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * Endpoint para obter o usuário.
     *
     * @param id Id do usuário.
     * */
    @GetMapping("/get")
    public ResponseEntity<User> get(@RequestParam Long id) {
        User user = userService.get(id);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Endpoint para cadastro de usuário.
     *
     * @param dto Dados de cadastro do usuário.
     * @return Status da requisição.
     */
    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody CreateClientDto dto) {
        User user = userService.createUserByAdmin(dto);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar de usuário.
     *
     * @param dto Dados de atualização do usuário.
     * @return Status da requisição.
     */
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestParam Long id, @RequestBody UpdateClientDto dto) {
        User user = userService.updateUserByAdmin(id, dto);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    /**
     * Endpoint para apagar o usuário.
     *
     * @param id Id do usuário.
     * */
    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
