package cake.backend.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cake.backend.product.entity.Product;
import cake.backend.product.model.CreateProductDto;
import cake.backend.product.model.UpdateProductDto;
import cake.backend.product.services.ProductService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> create(
        @RequestParam("name") String name,
        @RequestParam("price") String price,
        @RequestParam("keyWords") String keyWords,
        @RequestParam("weight") String weight,
        @RequestParam("file") MultipartFile file
    ) {
        try {
            CreateProductDto dto = new CreateProductDto(name, keyWords, weight, price);
            Product product = productService.create(dto,file);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Product>> list() {
        List<Product> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Product> get(@RequestParam Long id) {
        Product product = productService.findOne(id);
        return product == null ? 
            new ResponseEntity<>(HttpStatus.NOT_FOUND) : 
            new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/update")
    public Product update(
        @RequestParam Long id, 
        @RequestParam("name") String name,
        @RequestParam("price") String price,
        @RequestParam("keyWords") String keyWords,
        @RequestParam("weight") String weight,
        @RequestParam("file") MultipartFile file
    ) {
        UpdateProductDto dto = new UpdateProductDto(name, keyWords, weight, price);
        return productService.update(id, dto, file);
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        productService.delete(id);
    }
}
