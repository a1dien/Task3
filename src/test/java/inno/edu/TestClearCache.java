package inno.edu;

import inno.edu.utils.Fraction;
import inno.edu.utils.Fractionable;
import inno.edu.utils.Utils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.time.Duration;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.slf4j.LoggerFactory.getLogger;

public class TestClearCache {
    final Logger log = getLogger(lookup().lookupClass());
    @Test
    @SneakyThrows
    void testCacheTime() {
        log.debug("Проверка очистки кеша по timeout.");
        TimeManagement timeManagement = new TimeManagement();
        TimeManagement.start();
        CacheList cacheList = CacheList.getInstance();
        cacheList.clearCacheCompletely();

        Fraction fraction = new Fraction(3, 2);
        Fractionable frC = Utils.cache(fraction);
        frC.doubleValue();
        log.debug("Проверили, что в кеше 1 запись.");
        assertEquals(cacheList.getCacheList().size(), 1);
        Thread.sleep(3500);
        log.debug("Проверили, что в кеше удалилась запись после заданного timeout.");
        assertEquals(cacheList.getCacheList().size(), 0);
        frC.doubleValue();
        log.debug("Проверили, что в кеше вернулась 1 запись, после повторного вызова.");
        assertEquals(cacheList.getCacheList().size(), 1);
    }
}
