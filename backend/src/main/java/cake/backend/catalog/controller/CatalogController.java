package cake.backend.catalog.controller;

import cake.backend.catalog.model.dto.SearchDto;
import cake.backend.product.entity.Product;
import cake.backend.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("catalog")
public class CatalogController {
    @Autowired
    private ProductService productService;

    /**
     * Obter todos os produtos do banco de dados.
     *
     * @return Retorna a lista de produtos.
     *
     * */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Obter lista de itens paginada.
     *
     * @param page Página que será exibida.
     * @param numberOfElements Número de elementos daquela página
     * @return Lista de produtos.
     * */
    @GetMapping("/pagination")
    public ResponseEntity<List<Product>> getByPagination(@RequestParam int page, @RequestParam int numberOfElements) {
        List<Product> products = productService.findByLimit(page, numberOfElements);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Pesquisar alguns produtos.
     *
     * @param dto Dados para pesquisa.
     * @return Lista de produtos.
     * */
    @PostMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestBody SearchDto dto) {
        List<Product> products = productService.search(dto);
        if(products.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
