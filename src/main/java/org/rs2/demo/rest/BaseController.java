package org.rs2.demo.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Order(1)
public class BaseController implements Filter {

    private static Set<String> tokenStorage = Collections.synchronizedSet(new HashSet<String>());

    public static String getJWTToken(String username, String password) {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("rs2JWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        password.getBytes()).compact();

        String tokenString = "Bearer " + token;
        tokenStorage.add(tokenString);
        return tokenString;
    }

    @Override
    public void doFilter(
                            ServletRequest request,
                            ServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String contextPath = req.getContextPath();
        if(!"".equals(contextPath)) {
            Enumeration<String> headerNames = req.getHeaderNames();
            String token = null;
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String header = req.getHeader(headerNames.nextElement());
                    if (header.startsWith("Bearer"))
                        token = header.split(" ")[1];
                }
            }
            if (token != null && tokenStorage.contains(token))
                chain.doFilter(request, response);
            else
                response.getWriter().print("Bad credentials.");
        }
        else
            chain.doFilter(request, response);
    }
}
