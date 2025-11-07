package cake.backend.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cake.backend.cart.entity.Cart;
import cake.backend.cart.model.AddProductDto;
import cake.backend.cart.service.CartService;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService service;

    /**
     * Cria um carrinho.
     * 
     * @param id Id do usuário.
     * @return Carrinho criado.
     */
    @PostMapping("/create")
    public ResponseEntity<Cart> create(@RequestParam Long id) {
        Cart cart = service.create(id);
        if (cart == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }

    /**
     * Retorna um carrinho pelo id ou id do usuário.
     * 
     * @param id Id do carrinho ou id do usuário.
     * @return Carrinho.
     */
    @GetMapping("/getById")
    public ResponseEntity<Cart> get(@RequestParam Long id) {
        Cart cart = service.get(id);
        if (cart == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Adiciona um produto ao carrinho.
     * 
     * @param id  Id do carrinho.
     * @param dto Dados do produto.
     * @return Carrinho atualizado.
     */
    @PutMapping("/add")
    public ResponseEntity<Cart> addProduct(@RequestParam Long id, @RequestBody AddProductDto dto) {
        Cart cart = service.addProduct(id, dto);
        if (cart == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Remove um produto do carrinho.
     * 
     * @param id        Id do carrinho.
     * @param productId Id do produto.
     * @return Status da requisição.
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeProduct(@RequestParam Long id, @RequestParam Long productId) {
        service.removeProduct(id, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Limpa o carrinho.
     * 
     * @param id Id do carrinho.
     * @return Status da requisição.
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> removeProduct(@RequestParam Long id) {
        service.clear(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
