package io.com.bank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class CustomResponseUtil {

    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);


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


    // 인증 및 인가 실패 시
    public static void fail(HttpServletResponse response, String message, HttpStatus httpStatus) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);
            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("========== 서버 파싱 에러 ==========");
        }
    }
}
