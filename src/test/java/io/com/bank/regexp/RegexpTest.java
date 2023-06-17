package io.com.bank.regexp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class RegexpTest {

    @Test
    @DisplayName("한글만 허용 테스트")
    void test1() {
        String value1 = "가";
        String value2 = "d";
        boolean result1 = Pattern.matches("^[가-힣]+$", value1);
        boolean result2 = Pattern.matches("^[가-힣]+$", value2);

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}
