package io.com.bank.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureMockMvc // Mock(가짜) 환경에 MockMvc가 등록됨
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityConfigTest {

    // 가짜 환경에 등록된 MockMvc를 DI함
    private MockMvc mvc;

    @Test
    void authentication_test() throws Exception {
        // given


        // when


        // then


    }

    @Test
    void authorization_test() throws Exception {
        // given


        // when


        // then


    }
}
