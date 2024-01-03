package org.rs2.demo.services;

import org.junit.jupiter.api.Test;
import org.rs2.demo.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductsServiceTest extends AbstractServiceTestWrapper.AbstractServiceTest {

    @Autowired
    private ProductsService productsService;

    @Test
    public void findById() {
        Product p = productsService.findById(1);
        assert p.getId().equals(1);
    }

    @Test
    public void findProductTypes() {
        List<String> productTypes = productsService.findProductTypes();
        assert productTypes.contains("Books");
        assert productTypes.contains("Music");
        assert productTypes.contains("Games");
        assert productTypes.size() == 3;
    }

    @Test
    public void findByNameAndType() {
        Product p = productsService.findByNameAndType("Second", "Music");
        assert p.getDescription().equals("Description for Music2");
    }

}