package io.com.bank.jwt;

public interface JwtVo {

    // 실제 환경에서는 SECRET은 절대 노출되어서는 안된다.
    public static final String SECRET = "넥스그리드";
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

}
