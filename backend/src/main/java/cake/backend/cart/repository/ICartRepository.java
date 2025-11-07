package cake.backend.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cake.backend.cart.entity.Cart;
import cake.backend.auth.entity.User;

public interface ICartRepository extends JpaRepository<Cart, Long> {
    /**
     * Busca um carrinho pelo usuário.
     * 
     * @param user Usuário.
     * @return Carrinho.
     */
    Optional<Cart> findByUser(User user);
}
