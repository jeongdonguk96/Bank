package io.com.bank.regexp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class RegexpTest {

    @Test
    @DisplayName("Username 테스트")
    void test0() {
        String value1 = "donguk";
        boolean result1 = Pattern.matches("^[a-zA-Z0-9]{2,20}$", value1);

        assertThat(result1).isTrue();
    }

    @Test
    @DisplayName("Fullname 테스트")
    void test00() {
        String value1 = "정동욱";
        boolean result1 = Pattern.matches("^[가-힣a-zA-Z0-9]{1,20}$", value1);

        assertThat(result1).isTrue();
    }

    @Test
    @DisplayName("Email 테스트")
    void test000() {
        String value1 = "donguk@gmail.com";
        boolean result1 = Pattern.matches("^[a-zA-Z0-9]{2,20}+@[a-zA-Z0-9]{3,10}\\.[a-zA-Z]{2,5}$", value1);

        assertThat(result1).isTrue();
    }

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

    @Test
    @DisplayName("한글만 불허 테스트")
    void test2() {
        String value1 = "가";
        String value2 = "d";
        boolean result1 = Pattern.matches("^[^ㄱ-ㅎ가-힣]+$", value1);
        boolean result2 = Pattern.matches("^[^ㄱ-ㅎ가-힣]+$", value2);

        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @Test
    @DisplayName("영어만 허용 테스트")
    void test3() {
        String value1 = "가";
        String value2 = "d";
        boolean result1 = Pattern.matches("^[a-zA-Z]+$", value1);
        boolean result2 = Pattern.matches("^[a-zA-Z]+$", value2);

        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @Test
    @DisplayName("영어만 불허 테스트")
    void test4() {
        String value1 = "가";
        String value2 = "d";
        boolean result1 = Pattern.matches("^[^a-zA-Z]+$", value1);
        boolean result2 = Pattern.matches("^[^a-zA-Z]+$", value2);

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    @Test
    @DisplayName("영어와 숫자만 허용 테스트")
    void test5() {
        String value1 = "가123";
        String value2 = "d123";
        boolean result1 = Pattern.matches("^[a-zA-Z0-9]+$", value1);
        boolean result2 = Pattern.matches("^[a-zA-Z0-9]+$", value2);

        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
    }

    @Test
    @DisplayName("영어만 길이 2~4 허용 테스트")
    void test6() {
        String value1 = "가123";
        String value2 = "abcd";
        String value3 = "abcde";
        boolean result1 = Pattern.matches("^[a-zA-Z]{2,4}$", value1);
        boolean result2 = Pattern.matches("^[a-zA-Z]{2,4}$", value2);
        boolean result3 = Pattern.matches("^[a-zA-Z]{2,4}$", value3);

        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }

    @Test
    @DisplayName("구분 테스트")
    void test7() {
        String gubun = "DEPOSIT";
        boolean result = Pattern.matches("DEPOSIT", gubun);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("구분 테스트2")
    void test8() {
        String gubun = "TRANSFER";
        boolean result = Pattern.matches("DEPOSIT|TRANSFER", gubun);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("전화번호 테스트")
    void test9() {
        String tel = "01012345678";
        boolean result = Pattern.matches("^[0-9]{11}$", tel);

        assertThat(result).isTrue();
    }

}
