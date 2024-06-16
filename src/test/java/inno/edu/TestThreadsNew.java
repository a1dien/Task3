package inno.edu;

import inno.edu.utils.Fraction;
import inno.edu.utils.Fractionable;
import inno.edu.utils.Utils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

public class TestThreadsNew {
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
    void assertMultipleThreadCountNew() throws InterruptedException {
        log.debug("Создаем 3 потока с одинаковыми данными и 1 с новыми. Проверяем кол-во рез-тов в кеше = 4.");
        for (int i = 0; i < 3; i++) {
            new TestThread().run();
        }
        TimeManagement timeManagement = new TimeManagement();
        timeManagement.start();
        CacheList cacheList = CacheList.getInstance();
        Fraction fraction = new Fraction(5, 2);
        Fractionable frC = Utils.cache(fraction);
        frC.doubleValue();
        assertEquals(cacheList.getCacheList().size(), 2);
        frC.doubleValue();
        log.debug("Повторный вызов из кеша.");
        assertEquals(cacheList.getCacheList().size(), 2);
    }
}
