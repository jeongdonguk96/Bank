package io.com.bank.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.auth.CustomUserDetails;
import io.com.bank.dto.member.MemberRequestDto.LoginRequestDto;
import io.com.bank.dto.member.MemberResponseDto.LoginResponseDto;
import io.com.bank.util.CustomResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private final Logger log = LoggerFactory.getLogger(getClass());


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/login"); // 로그인 url 지정
        this.authenticationManager = authenticationManager;
    }


    // 강제 로그인
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("========== attemptAuthentication 호출됨 ==========");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(), loginRequestDto.getPassword());

            // UserDetailsService의 loadUserByUsername()을 호출함
            return authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            // SecurityConfig의 authenticationEntryPoint에 걸린다
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }


    // 위 attemptAuthentication()가 성공해서 Authentication 객체가 반환되면 호출됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("========== successfulAuthentication 호출됨 ==========");

        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        String jwtToken = JwtProcess.create(userDetails);
        response.addHeader(JwtVo.HEADER, jwtToken);

        LoginResponseDto loginResponseDto = new LoginResponseDto(userDetails.getMember());
        CustomResponseUtil.success(response, loginResponseDto);
    }
}
