package org.rs2.demo.rest;

import org.rs2.demo.entities.Product;
import org.rs2.demo.entities.User;
import org.rs2.demo.services.ProductsService;
import org.rs2.demo.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsersController extends BaseController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private ProductsService productsService;

    @GetMapping("/user/token/{username}/{password}")
    public String token(@PathVariable("username") String username, @PathVariable("password") String password) {
        User u = null;
        try {
            u = usersService.findByUsernameAndPassword(username, password);
        }
        catch(Throwable t) {}
        if(u != null)
            return getJWTToken(username, password);
        else
            return "";
    }

    @GetMapping("/user/{username}/basket")
    public List<Product> userBasketByUsername(@PathVariable String username) {
        User u = usersService.findByUsername(username);
        return usersService.fetchBasket(u);
    }

    @PostMapping("/user/basket/add/{username}/{productId}/{quantity}")
    public List<Product> addToBasket(@PathVariable("username") String username,
                                     @PathVariable("productId") Integer productId,
                                     @PathVariable("quantity") Integer quantity) {
        User u = usersService.findByUsername(username);
        Product p = productsService.findById(productId);
        return usersService.addToBasket(u, p, quantity);
    }

}
