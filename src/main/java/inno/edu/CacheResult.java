package inno.edu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;

//Делаем класс для хранения всех кешей Singleton
@Getter @AllArgsConstructor
public class CacheResult {
    private String keytoCache;
    private Object obj;
    @Setter
    private long timeToClear;

    public void refreshTimeCache(long cacheValue) {
        long currentTime = System.currentTimeMillis();
        timeToClear = currentTime + cacheValue;
    }

}
