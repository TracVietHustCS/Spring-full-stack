//package org.project1.shopweb.filter;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.coyote.BadRequestException;
//import org.apache.tomcat.util.bcel.Const;
//import org.project1.shopweb.constants.ApplicationConstant;
//import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.core.env.Environment;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//public class JwtTokenValidFIlter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String jwt = request.getHeader(ApplicationConstant.JWT_HEADER);
//        if (jwt != null) {
//            try{
//                Environment enviroment = getEnvironment();
//                if(enviroment != null){
//                    String secret = enviroment.getProperty(ApplicationConstant.JWT_SECRET_KEY,
//                            ApplicationConstant.JWT_SECRET_DEFAULT_VALUE);
//                    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//                    if(secretKey != null){
//                        Claims claims = Jwts.parser().verifyWith(secretKey)
//                                .build().parseEncryptedClaims(jwt).getPayload();
//                    String username = String.valueOf(claims.get("username"));
//                    String authorities = String.valueOf(claims.get("authorities"));
//                        Authentication authentication = new UsernamePasswordAuthenticationToken(username,null
//                                , AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                    }
//
//                }
//            } catch (Exception e){
//                throw  new BadRequestException("Invalid token");
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}