package inno.edu;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class CacheList {
    private static volatile CacheList instance;
    @Getter
    private static volatile List<CacheResult> cacheList = new CopyOnWriteArrayList<>();

    private CacheList() {}
    public static CacheList getInstance() {
        CacheList localInstance = instance;
        if (localInstance == null) {
            synchronized (CacheList.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CacheList();
                }
            }
        }
        return localInstance;
    }
    public Object checkCacheResult (String keyToCache) {
        for (CacheResult result : cacheList) {
            if (result.getKeytoCache().equals(keyToCache)) return result;
        }
        return null;
    }
    public synchronized void addNewResult (String keyToCache, Object result, Long timeToLeft) {
        cacheList.add(new CacheResult(keyToCache, result, timeToLeft));
    }

    //Метод для очистки кеша. Если текущее время больше,
    // чем время отведенное для жизни рез-та из CacheList, то удаляем из листа ссылку на рез-т.
    public synchronized static void clearCashe() {
        long currentTime = System.currentTimeMillis();

        for (CacheResult result : cacheList) {
            if (currentTime > result.getTimeToClear()) cacheList.remove(result);
        }
    }

    //Полная очистка кеша по требованию
    public static void clearCacheCompletely() {
        cacheList.clear();
    }


}
