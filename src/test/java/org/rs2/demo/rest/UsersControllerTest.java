package org.rs2.demo.rest;

import org.junit.jupiter.api.Test;
import org.rs2.demo.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersControllerTest extends AbstractControllerTestWrapper.AbstractControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void token() {
        String token = restTemplate.getForObject("http://localhost:" + port + "/user/token/guest/guest", String.class);
        assertThat(token.indexOf("Bearer")==0);
    }

    @Test
    public void userBasketByUsername() {
        ResponseEntity<Product> rep = restTemplate.getForEntity("http://localhost:" + port
                + "/product/findByNameAndType?name="+"Second"+"&type="+"Books", Product.class);
        Product p = rep.getBody();
        List<Product> basket = restTemplate.getForObject("http://localhost:{port}/user/{username}/basket", List.class, port, "guest");
    }

}
