package org.project1.shopweb.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Pair;
import org.project1.shopweb.component.JwtTokenUtil;
import org.project1.shopweb.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;



    @Value("${api.prefix}")
    private String apiPrefix;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
//        try {
            if(isBypassToken(request)) {
                filterChain.doFilter(request, response); //enable bypass
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                logger.info("filter qua");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }

            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if (phoneNumber != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                if(jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            logger.info("done valid token");
            filterChain.doFilter(request, response); //enable bypass
//        }catch (Exception e) {
//            logger.info("filter qua bi loi");
//            logger.info(e.getMessage());
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        }

    }


    private boolean isBypassToken(@NonNull  HttpServletRequest request) {

        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories/all", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/actuator/health", apiPrefix), "GET"),
                Pair.of(String.format("%s/actuator/info", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/refreshtoken",apiPrefix),"POST"),
                Pair.of(String.format("%s/users/auth/social-login",apiPrefix),"GET"),
                Pair.of(String.format("%s/users/auth/social/callback",apiPrefix),"GET"),
                Pair.of(String.format("%s/users/login/oauth2/code/google",apiPrefix),"GET"),
                Pair.of(String.format("%s/payments/create_payment_url",apiPrefix),"POST"),
                Pair.of(String.format("%s/payments/vnpay-callback",apiPrefix),"GET")

        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        if (requestPath.equals(String.format("/%s/orders", apiPrefix))
                && requestMethod.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }

        for(Pair<String, String> bypassToken: bypassTokens) {
            String path = bypassToken.getLeft();
            String method = bypassToken.getRight();
            if (requestPath.contains(bypassToken.getLeft()) &&
                    requestMethod.equals(bypassToken.getRight())){
                logger.info("pass");
                return true;
            }


        }



        logger.info("please dung bug nua ma :((");
        return false;
    }
}
