package inno.edu;

import inno.edu.utils.Fraction;
import inno.edu.utils.Fractionable;
import inno.edu.utils.Utils;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.*;
import static org.slf4j.LoggerFactory.getLogger;

public class TestThreads {
    class TestThread implements Runnable {

        @Override
        public void run() {
            Fraction fraction = new Fraction(3, 2);
            Fractionable frC = Utils.cache(fraction);
            frC.doubleValue();
        }
    }
    final Logger log = getLogger(lookup().lookupClass());

    @Test
    void assertMultipleThreadCount() throws InterruptedException {
        log.debug("Создаем 3 потока для создания дробей и проверяем, что наш список закешированных рез-тов = 1.");
        TimeManagement timeManagement = new TimeManagement();
        timeManagement.start();
        CacheList cacheList = CacheList.getInstance();
        cacheList.clearCacheCompletely();

        for (int i = 0; i < 3; i++) {
            new TestThread().run();
        }

        assertEquals(cacheList.getCacheList().size(), 1);
    }


}
