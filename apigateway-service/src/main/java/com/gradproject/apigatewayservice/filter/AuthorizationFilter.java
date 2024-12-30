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

            //token 에서 userEmail 뽑기
            try {
                // token에서 사용자 이메일 추출
                String userEmail = getSubjectInJwt(token);

                // 헤더에 사용자 이메일 추가
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-User-Email", userEmail)
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            } catch (JwtNullTokenException | ExpiredJwtException e) {
                // JWT 예외 발생 시 401 응답 반환
                return handleError(exchange, e.getMessage());
            }
        };
    }

    private Mono<Void> handleError(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

//        생각해보니 401 UNAUTHORIZED 만 반환해주면 프론트에서 사용자가 볼 수 있는 에러 메세지를 생성하므로
        byte[] bytes = String.format("{\"error\": \"%s\"}", errorMessage).getBytes();
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory()
                .wrap(bytes)));
    }

    private String getSubjectInJwt(String token) throws JwtNullTokenException {

        if (token == null) {
            throw new JwtNullTokenException("유효하지 않은 token 입니다. (null)");
        }

        String replacedToken = token.replace("Bearer", "");

        if (replacedToken.equals("null")) {
            throw new JwtNullTokenException("유효하지 않은 token 입니다. (null string token)");
        }

        String subject = Jwts.parser()
                .setSigningKey("kyle")
                .parseClaimsJws(replacedToken).getBody()
                .getSubject();

        return subject;
    }


    private String extractJwtFromRequest(ServerHttpRequest request) {

        String bearerToken = request.getHeaders().getFirst("Authorization");
        return bearerToken;
    }



}