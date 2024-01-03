package org.rs2.demo.services;

import org.rs2.demo.entities.Product;
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
public class ProductsService {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Product findById(Integer id) {
        Map params = new HashMap();
        params.put("id", id);
        List<Product> products = jdbcTemplate.query(
                "select * from \"Product\" where \"ID\"=:id",
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
                });
        return products.get(0);
    }

    public List<String> findProductTypes() {
        Map params = new HashMap();
        List<String> types = jdbcTemplate.query(
                "select distinct(\"Type\") from \"Product\" order by \"Type\" asc",
                params,
                new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return rs.getString("Type");
                    }
                });
        return types;
    }

    public Product findByNameAndType(String name, String type) {
        Map params = new HashMap();
        params.put("name", name);
        params.put("type", type);
        List<Product> products = jdbcTemplate.query(
                "select * from \"Product\" where \"Name\"=:name and \"Type\"=:type",
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
                });
        return products.get(0);
    }

}
