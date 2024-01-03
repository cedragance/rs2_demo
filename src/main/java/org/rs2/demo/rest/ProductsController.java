package org.rs2.demo.rest;

import org.rs2.demo.entities.Product;
import org.rs2.demo.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController extends BaseController {

    @Autowired
    private ProductsService productsService;

    @GetMapping("/products/types")
    public List<String> findProductTypes() {
        return productsService.findProductTypes();
    }

    @GetMapping("/product/findByNameAndType")
    public Product findByNameAndType(@RequestParam String name, @RequestParam String type) {
        Product p = productsService.findByNameAndType(name, type);
        return p;
    }

}
