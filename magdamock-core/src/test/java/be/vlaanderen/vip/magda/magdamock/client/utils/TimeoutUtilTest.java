package be.vlaanderen.vip.magda.magdamock.client.utils;

import be.vlaanderen.vip.magda.magdamock.utils.RandomTimeoutUtil;
import be.vlaanderen.vip.magda.magdamock.utils.TimeoutUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeoutUtilTest {
    @Test
    void getTimeout() {
        int timeoutMinimum = 200, timeoutMaximum = 500;
        TimeoutUtil timeoutUtil = new RandomTimeoutUtil(timeoutMinimum, timeoutMaximum);
        long beforeTimeout = System.currentTimeMillis();
        timeoutUtil.timeout();
        long afterTimeout = System.currentTimeMillis();
        Assertions.assertTrue(afterTimeout - beforeTimeout >= timeoutMinimum);
        Assertions.assertTrue(afterTimeout - beforeTimeout <= timeoutMaximum + 50);
    }
}
