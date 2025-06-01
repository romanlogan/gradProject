package com.gradproject.apigatewayservice.filter;

import com.gradproject.apigatewayservice.exception.JwtNullTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;


@Component
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {

//    @Value("${jwt.secret}")
//    private String jwtSecret;

    private Environment env;

    public AuthorizationFilter() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            ServerHttpRequest request = exchange.getRequest();
            String token = extractJwtFromRequest(request);

            try {
                //Extract userEmail from token
                String userEmail = getSubjectInJwt(token);

                // Add user email to header
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Email", userEmail)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (JwtNullTokenException | ExpiredJwtException e) {

                // Return 401 response when JWT exception occurs
                return handleError(exchange, e.getMessage());
            }
        };
    }

    private Mono<Void> handleError(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        //Returns a 401 UNAUTHORIZED error and the error message is rewritten on the front end.
        byte[] bytes = String.format("{\"error\": \"%s\"}", errorMessage).getBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(bytes)));
    }

    private String getSubjectInJwt(String token) throws JwtNullTokenException {

        if (token == null) {
            throw new JwtNullTokenException("Invalid token.");
        }

        String replacedToken = token.replace("Bearer", "");

        if (replacedToken.equals("null")) {
            throw new JwtNullTokenException("Invalid token.");
        }

        String subject = Jwts.parser()
                .setSigningKey("kyle")
                .parseClaimsJws(replacedToken).getBody()
                .getSubject();

        return subject;
    }


    private String extractJwtFromRequest(ServerHttpRequest request) {

        return request.getHeaders().getFirst("Authorization");
    }





}