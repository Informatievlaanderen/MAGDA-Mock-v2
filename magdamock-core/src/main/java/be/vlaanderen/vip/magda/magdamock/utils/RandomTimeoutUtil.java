package be.vlaanderen.vip.magda.magdamock.utils;

import lombok.SneakyThrows;

import java.util.Random;

// In order to mimic the potential timeout that MAGDA resources may have (as they may have to do requests to other endpoints in the background) we have a timeout util
public class RandomTimeoutUtil implements TimeoutUtil {
    private final Integer minimumTimeoutMillis;
    private final Integer maximumTimeoutMillis;
    private final Random random;

    public RandomTimeoutUtil(Integer minimumTimeoutMillis, Integer maximumTimeoutMillis) {
        this.minimumTimeoutMillis = minimumTimeoutMillis;
        this.maximumTimeoutMillis = maximumTimeoutMillis;
        this.random = new Random();
    }

    @SneakyThrows
    public void timeout() {
        int randomMillis = random.nextInt(maximumTimeoutMillis - this.minimumTimeoutMillis) + this.minimumTimeoutMillis;
        Thread.sleep(randomMillis);
    }
}
