package com.gradproject.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public AuthorizationHeaderFilter(Environment env){
        super(Config.class);
        this.env = env;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {


        return (exchange, chain) -> {

            //헤더에 로그인시 받았던 토큰틀 전달, 토큰이 잘 들어갔는가, 토큰이 잘 발급된건가 등등 확인
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                System.out.println("---------------No authorization header-------------");
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }



//            String authorizationHeader = request.getHeaders().get()
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            //bearer 부분을 없애고(빈 문자열로 치환) 나머지 부분이 토큰 값
            String jwt = authorizationHeader.replace("Bearer", "");

            // Create a cookie object
//            ServerHttpResponse response = exchange.getResponse();
//            ResponseCookie c1 = ResponseCookie.from("my_token", "test1234").maxAge(60 * 60 * 24).build();
//            response.addCookie(c1);

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);

        return response.setComplete();
//        byte[] bytes = "The requested token is invalid.".getBytes(StandardCharsets.UTF_8);
//        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
//        return response.writeWith(Flux.just(buffer));

    }

    private boolean isJwtValid(String jwt) {
//        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
//        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        String subject = null;
        boolean returnValue = true;

        try {
//            JwtParser jwtParser = Jwts.parserBuilder()
//                    .setSigningKey(signingKey)
//                    .build();
//
//            subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject();

            //token 의 subject 부분을 추출해서 정상적인 값인지 검사
//            setSigningKey 는 jwt 를 암호화 할때 사용했던 방법으로 다시 복호
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(jwt).getBody()
                    .getSubject();

        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }


}
