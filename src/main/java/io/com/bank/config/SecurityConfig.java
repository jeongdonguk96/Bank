package io.com.bank.config;

import io.com.bank.domain.RoleEnum;
import io.com.bank.jwt.JwtAuthenticationFilter;
import io.com.bank.jwt.JwtAuthorizationFilter;
import io.com.bank.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());


    // JWT 필터 등록
    public static class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            builder.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(builder);
        }
    }


    // 시큐리티 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("========== SecurityFilterChain 빈 등록 완료 ==========");

        http.headers().frameOptions().disable(); // iframe 허용 안함
        http.csrf().disable(); // enable 시 포스트맨 작동하지 않음
        http.cors().configurationSource(corsConfigurationSource()); // cors 정책
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 생성 정책
        http.formLogin().disable(); // 폼 태그 로그인 방식 disable
        http.httpBasic().disable();
        http.authorizeRequests() // 인가 설정
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole(String.valueOf(RoleEnum.ADMIN))
                .anyRequest().permitAll();
        http.apply(new CustomSecurityFilterManager()); // JWT 필터 등록
//        http.apply(new AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity>() {
//            @Override
//            public void configure(HttpSecurity builder) throws Exception {
//                AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
//                builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
//                super.configure(builder);
//            }
//        });
//        http.exceptionHandling() // 인증 예외 설정
//                .authenticationEntryPoint((request, response, authenticationException)->{ // 예외 가로채기
//                        CustomResponseUtil.fail(response, "로그인을 진행해주세요", HttpStatus.UNAUTHORIZED);
//                });
        http.exceptionHandling() // 인가 예외 설정
                .accessDeniedHandler((request, response, e) -> {
                        CustomResponseUtil.fail(response, "권한이 없습니다", HttpStatus.FORBIDDEN);
                });


        return http.build();
    }


//    // JWT 필터 등록
//    public static class CustomSecurityFilterManager extends AbstractHttpConfigurer<CustomSecurityFilterManager, HttpSecurity> {
//
//        @Override
//        public void configure(HttpSecurity builder) throws Exception {
//            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
//            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
//            super.configure(builder);
//        }
//    }


    // cors 정책 설정
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("========== CorsConfigurationSource cors 설정이 SecurityFilterChain에 등록 완료 ==========");

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*"); // 모든 헤더 형식 허용
        corsConfiguration.addAllowedMethod("*"); // 모든 매서드 허용
        corsConfiguration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용
        corsConfiguration.setAllowCredentials(true); // 서버는 클라이언트의 자격 증명을 포함하는 요청에 응답할 수 있음

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }


    // 패스워드 비크립트 암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        log.info("========== BCryptPasswordEncoder 빈 등록 완료 ==========");
        return new BCryptPasswordEncoder();
    }
}
