package io.com.bank.type;

import org.junit.jupiter.api.Test;

public class LongTypeTest {

    @Test
    void long_test1() {
        // given
        Long num1 = 1111L;
        Long num2 = 1111L;
        Long num3 = 100L;
        Long num4 = 1000L;

        // when
        if (num1 == num2) {
            System.out.println("값이 동일합니다");
        }
        else {
            System.out.println("값이 동일하지 않습니다");
        }

        if (num1.longValue() == num2.longValue()) {
            System.out.println("값이 동일합니다");
        }
        else {
            System.out.println("값이 동일하지 않습니다");
        }

        if (num3 < num4) {
            System.out.println("num3이 작습니다");
        }
        else {
            System.out.println("num3이 작지 않습니다");
        }

        // then
    }
}
