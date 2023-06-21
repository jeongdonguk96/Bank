package io.com.bank.jwt;

import io.com.bank.auth.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 로그인 URL에 대해서만 작동하는 JwtAuthenticationFilter와 달리 이 필터는 모든 요청에 대해 작동함(토큰 검증)
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isHeaderVerified(request, response)) {

            // 토큰으로 사용자를 검증하고 CustomUserDetails을 반환
            String token = request.getHeader(JwtVo.HEADER).replace(JwtVo.TOKEN_PREFIX, "");
            CustomUserDetails userDetails = JwtProcess.verify(token);

            // 토큰에서 반환한 CustomUserDetails로 임시 세션을 생성하고 시큐리티 컨텍스트에 저장
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }


    // 토큰 존재 여부 확인
    private boolean isHeaderVerified(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVo.HEADER);
        return header != null && header.startsWith(JwtVo.TOKEN_PREFIX);
    }
}
