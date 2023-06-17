package io.com.bank.aop;

import io.com.bank.exception.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class CustomValidationAdvice {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping(){}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping(){}

    @Around("postMapping() || putMapping()") // @PostMapping이나 @PutMapping이 붙은 모든 매서드
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs(); // 조인포인트의 매개변수들

        for (Object arg : args) { // 매개변수들을 반복문을 돌려서
            if (arg instanceof BindingResult) { // BindingResult이라는 파라미터가 있는 경우
                BindingResult bindingResult = (BindingResult) arg;

                if (bindingResult.hasErrors()) { // BindingResult에 에러 데이터가 있는 경우
                    Map<String, String> errorMap = new HashMap<>();

                    for (FieldError error : bindingResult.getFieldErrors()) {
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }

                    throw new CustomValidationException("유효성 검사 실패", errorMap);
                }
            }
        }

        // BindingResult가 없거나 있어도 에러 데이터가 없으면 정상적으로 매서드 실행
        return proceedingJoinPoint.proceed();
    }
}
