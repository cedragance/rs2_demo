package org.rs2.demo.rest;

import org.junit.jupiter.api.Test;
import org.rs2.demo.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductsControllerTest extends AbstractControllerTestWrapper.AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void findProductTypes() {
        List<String> productTypes = restTemplate.getForObject("http://localhost:" + port + "/products/types", List.class);
        assertThat(productTypes.size() == 3);
        assertThat(productTypes.contains("Books"));
        assertThat(productTypes.contains("Music"));
        assertThat(productTypes.contains("Games"));
    }

    @Test
    public void findByNameAndType() {
        ResponseEntity<Product> rep = restTemplate.getForEntity("http://localhost:" + port
                + "/product/findByNameAndType?name="+"Second"+"&type="+"Books", Product.class);
        Product p = rep.getBody();
        assertThat(p.getDescription().equals("Description for Book2"));
    }

}
