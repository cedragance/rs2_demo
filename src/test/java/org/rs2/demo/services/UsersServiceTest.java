package org.rs2.demo.services;

import org.junit.jupiter.api.Test;
import org.rs2.demo.entities.Product;
import org.rs2.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UsersServiceTest extends AbstractServiceTestWrapper.AbstractServiceTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProductsService productsService;

    @Test
    public void findByUsername() {
        User u = usersService.findByUsername("guest");
        assert u.getPassword().equals("guest");
    }

    @Test
    public void findByUsernameAndPassword() {
        User u = usersService.findByUsernameAndPassword("guest", "guest");
        assert u.getLoginName().equals("guest");
        assert u.getPassword().equals("guest");
    }

    @Test
    public void fetchBasket() {
        User u = usersService.findByUsername("guest");
        List<Product> basket = usersService.fetchBasket(u);
    }

    @Test
    public void addToBasket() {
        User u = usersService.findByUsername("guest");
        Product p = productsService.findByNameAndType("Second", "Music");
        usersService.addToBasket(u, p, 1);
        List<Product> basket = usersService.fetchBasket(u);
        assert basket.get(0).getDescription().equals("Description for Music2");
    }

    @Test
    public void removeFromBasket() {
        User u = usersService.findByUsername("guest");
        Product p = productsService.findByNameAndType("Second", "Music");
        usersService.addToBasket(u, p, 1);
        List<Product> basket = usersService.fetchBasket(u);
        basket = usersService.removeFromBasket(u, p);
        assert basket.size() == 0;
    }

}