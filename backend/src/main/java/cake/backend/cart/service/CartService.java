package cake.backend.cart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cake.backend.auth.entity.User;
import cake.backend.auth.service.UserService;
import cake.backend.cart.entity.Cart;
import cake.backend.cart.entity.CartItem;
import cake.backend.cart.model.AddProductDto;
import cake.backend.cart.repository.ICartRepository;
import cake.backend.product.entity.Product;
import cake.backend.product.services.ProductService;

@Service
public class CartService {
    @Autowired
    private ICartRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Criar carrinho para um usuário.
     * 
     * @param id ID do Usuário no qual está vinculado ao carrinho.
     */
    public Cart create(Long id) {
        User user = userService.get(id);
        if (user == null)
            return null;
        Cart foundCart = repository.findByUser(user).orElse(null);
        if (foundCart != null)
            return foundCart;
        Cart cart = Cart.builder().user(user).cartItems(List.of()).build();
        if (cart == null)
            return null;
        return repository.save(cart);
    }

    /**
     * Obter um carrinho por ID.
     * 
     * @param id ID do carrinho.
     * @return Entidade carrinho.
     */
    public Cart get(Long id) {
        Cart cart = null;
        if (id == null)
            return null;
        cart = repository.findById(id).orElse(null);
        if (cart == null)
            return null;
        User user = userService.get(id);
        if (user == null)
            return null;
        cart = repository.findByUser(user).orElse(null);
        if (cart == null)
            return null;
        return cart;
    }

    /**
     * Adicionar produtos a um carrinho.
     * 
     * @param id  ID do carrinho.
     * @param dto Produtos que serão adicionados.
     */
    public Cart addProduct(Long id, AddProductDto dto) {
        if (id == null || dto == null || dto.productId() == null) {
            return null; // Handle invalid input
        }
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null) {
            return null;
        }

        Product product = productService.findOne(dto.productId());
        if (product == null) {
            return null; // Handle product not found
        }

        // Check if item already exists in cart (optional optimization)
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity((existingItem.getQuantity() + dto.quantity()));
        } else {
            cart.getCartItems().add(CartItem.builder().product(product).quantity(dto.quantity()).build());
        }

        return repository.save(cart);
    }

    /**
     * Remover um produto do carrinho.
     * 
     * @param id        ID do carrinho.
     * @param productId ID do produto.
     */
    public void removeProduct(Long id, Long productId) {
        if (id == null)
            return;
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null)
            return;
        List<CartItem> products = cart.getCartItems();
        products.removeIf(p -> p.getProduct().getId() == productId);
        cart.setCartItems(products);
        repository.save(cart);
    }

    /**
     * Remove todos os itens do carrinho.
     * 
     * @param id ID do carrinho.
     */
    public void clear(Long id) {
        if (id == null)
            return;
        Cart cart = repository.findById(id).orElse(null);
        if (cart == null)
            return;
        cart.setCartItems(new ArrayList<>());
        repository.save(cart);
    }
}
