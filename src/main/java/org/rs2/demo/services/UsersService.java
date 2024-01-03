package org.rs2.demo.services;

import org.rs2.demo.entities.Product;
import org.rs2.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public User findByUsername(String username) {
        Map params = new HashMap();
        params.put("username", username);
        List<User> users = jdbcTemplate.query(
                "select * from \"User\" where \"LoginName\"=:username",
                params,
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User u = new User();
                        u.setId(rs.getInt("UserID"));
                        u.setLoginName(rs.getString("LoginName"));
                        u.setPassword(rs.getString("Password"));
                        return u;
                    }
                });
        return users.get(0);
    }

    public User findByUsernameAndPassword(String username, String password) {
        Map params = new HashMap();
        params.put("username", username);
        params.put("password", password);
        List<User> users = jdbcTemplate.query(
                "select * from \"User\" where \"LoginName\"=:username and \"Password\"=:password",
                params,
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User u = new User();
                        u.setId(rs.getInt("UserID"));
                        u.setLoginName(rs.getString("LoginName"));
                        u.setPassword(rs.getString("Password"));
                        return u;
                    }
                });
        return users.get(0);
    }

    public List<Product> fetchBasket(User user) {
        Map params = new HashMap();
        params.put("UserID", user.getId());
        List<Product> products = jdbcTemplate.query(
                "select p.* from \"Product\" p " +
                        " inner join \"Basket\" b on b.\"ProductID\" = p.\"ID\" " +
                        " inner join \"User\" u on u.\"UserID\" = b.\"UserID\" " +
                        " where u.\"UserID\"=:UserID",
                params,
                new RowMapper<Product>() {
                    @Override
                    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Product p = new Product();
                        p.setId(rs.getInt("ID"));
                        p.setName(rs.getString("Name"));
                        p.setType(rs.getString("Type"));
                        p.setDescription(rs.getString("Description"));
                        return p;
                    }
                }
        );
        return products;
    }

    public List<Product> addToBasket(User user, Product product, Integer quantity) {
        Map params = new HashMap();
        params.put("UserID", user.getId());
        params.put("ProductID", product.getId());
        for(int i = 0; i < quantity; i++) {
            jdbcTemplate.update(
                    "insert into \"Basket\" (\"ProductID\", \"UserID\") " +
                            " values(:ProductID, :UserID)",
                    params
            );
        }
        return fetchBasket(user);
    }

    public List<Product> removeFromBasket(User user, Product product) {
        Map params = new HashMap();
        params.put("UserID", user.getId());
        params.put("ProductID", product.getId());
        jdbcTemplate.update(
                "delete from \"Basket\" b " +
                        " where b.\"ProductID\"=:ProductID " +
                          " and b.\"UserID\"=:UserID",
                params
        );
        return fetchBasket(user);
    }

}
