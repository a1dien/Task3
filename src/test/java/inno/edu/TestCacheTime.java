package inno.edu;

import inno.edu.utils.Fraction;
import inno.edu.utils.Fractionable;
import inno.edu.utils.Utils;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.slf4j.Logger;

import java.time.Duration;

import static java.lang.invoke.MethodHandles.lookup;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.LoggerFactory.getLogger;

public class TestCacheTime {
    final Logger log = getLogger(lookup().lookupClass());
    @Test
    void testCacheTime() {
        log.debug("Проверка, что второй вызов не увеличивает время выполнения, а рез-т вызывается повторно из кеша.");
        Fraction fraction = new Fraction(3, 2);
        Fractionable frC = Utils.cache(fraction);
        assertTimeoutPreemptively(Duration.ofMillis(2000), () -> {
            frC.doubleValue();
            frC.doubleValue();
        });
    }
    @Test
    void testCacheTimeFailed() {
        log.debug("Проверка, что новый вызов увеличивает время выполнения, а рез-т вызывается не из кеша.");
        Fraction fraction = new Fraction(3, 2);
        Fractionable frC = Utils.cache(fraction);
        CacheList cacheList = CacheList.getInstance();
        try {
            assertTimeoutPreemptively(Duration.ofMillis(2000), () -> {
                frC.doubleValue();
                frC.setNum(5);
                frC.doubleValue();
            });
        } catch (AssertionFailedError e) {
            assertThat(e.getMessage(), containsString("timed out after"));
        }
    }
}
