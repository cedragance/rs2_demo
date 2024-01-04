package org.rs2.demo.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BaseController implements Filter {

    private static Set<String> IGNORE_PATHS = Set.of("/user/token/*/*");

    private static Set<String> TOKEN_STORAGE = Collections.synchronizedSet(new HashSet<String>());

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
        TOKEN_STORAGE.add(token);
        return token;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI();
        AntPathMatcher antMatchers = new AntPathMatcher(); 
        if(!IGNORE_PATHS.stream().anyMatch(ip -> antMatchers.match(ip, path))) {
            Enumeration<String> headerNames = req.getHeaderNames();
            String token = null;
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String header = req.getHeader(headerNames.nextElement());
                    if (header.startsWith("Bearer"))
                        token = header.split(" ")[1];
                }
            }
            if (token != null && TOKEN_STORAGE.contains(token))
                chain.doFilter(request, response);
            else
                response.getWriter().print("Bad credentials.");
        }
        else
            chain.doFilter(request, response);
    }
}
