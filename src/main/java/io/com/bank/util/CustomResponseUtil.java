package io.com.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);


    // 로그인 인증 실패 시
    public static void noAuthentication(HttpServletResponse response, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(401);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("========== 서버 파싱 에러 ==========");
        }
    }


    // 미권한으로 인한 접근 실패 시
    public static void noAuthorization(HttpServletResponse response, String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(403);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("========== 서버 파싱 에러 ==========");
        }
    }


    // 로그인 성공 시
    public static void success(HttpServletResponse response, Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인 성공", object);
            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("========== 서버 파싱 에러 ==========");
        }
    }
}
