package inno.edu;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

//Делаем Timer Singleton
public class TimeManagement {
    private static Timer timer = new Timer();
    private static volatile TimeManagement instance;

    public static TimeManagement getInstance() {
        TimeManagement localInstance = instance;
        if (localInstance == null) {
            synchronized (TimeManagement.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new TimeManagement();
                }
            }
        }
        return localInstance;
    }
    public static void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                System.out.println("Пробуем произвести очистку кеша");
                CacheList.clearCashe();
            }
        }, 0, 1000);
    }

}