package io.com.bank.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.com.bank.auth.CustomUserDetails;
import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtProcess {

    private final Logger log = LoggerFactory.getLogger(getClass());


    // 토큰 생성
    // 토큰은 내부를 들여다 볼 수 있기 때문에 최소한의 사용자 정보만 담는다
    public static String create(CustomUserDetails userDetails) {
        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVo.EXPIRATION_TIME))
                .withClaim("id", userDetails.getMember().getId())
                .withClaim("role", userDetails.getMember().getRole().toString())
                .sign(Algorithm.HMAC512(JwtVo.SECRET));

        return JwtVo.TOKEN_PREFIX + jwtToken;
    }


    // 토큰 검증 CustomUserDetails 객체를 강제로 시큐리티 세션에 주입할 예정
    public static CustomUserDetails verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET)).build().verify(token);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        Member member = Member.builder().id(id).role(RoleEnum.valueOf(role)).build();

        return new CustomUserDetails(member);
    }
}
