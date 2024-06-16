package inno.edu;

import inno.edu.annotations.Cache;
import inno.edu.annotations.Mutator;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

//Основная идея сделать 2 потоко независмых singletone:
// 1) для хранения списка закешированных рез-тов CacheList (singletone, volatile List<CacheResult>)
// 2) для таймера, который будет запускаться в своем независимом потоке и очищать List<CacheResult>
// Методы добавления новых рез-тов и очищения synchronized clearCache + addNewResult
public class MakeCache<T> implements InvocationHandler {
    private Double cache;
    private T obj;
    private CacheList cacheList = CacheList.getInstance();

    public MakeCache(T obj) {
        this.obj = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object objectResult;
        Method currentMethod;
        String keyToCacheMap;
        CacheResult cacheResult;

        currentMethod = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
        keyToCacheMap = keyToCache(method, args);
//        System.out.println("Получившийся ключ: " + keyToCacheMap);

        if (currentMethod.isAnnotationPresent(Cache.class)) {
            cacheResult = (CacheResult) cacheList.checkCacheResult(keyToCacheMap);
            if (cacheResult != null) {
                //Если такой рез-т уже был закеширован, то
                //1) обновляем время жизни кеша
                cacheResult.refreshTimeCache(currentMethod.getAnnotation(Cache.class).value());
                // 2) возвращаем рез-т
                return cacheResult.getObj();
            }
            objectResult = method.invoke(obj, args);

            cacheList.addNewResult(
                    keyToCacheMap
                    , objectResult
                    , System.currentTimeMillis() + currentMethod.getAnnotation(Cache.class).value()
            );
            return objectResult;
        }
        if (currentMethod.isAnnotationPresent(Mutator.class)) {
            //По факту он нам незачем, потому что мы каждый раз формируем уникальную строку для состояния объекта при проверке на Cache
        }
        return method.invoke(obj, args);
    }

    //Определяем ключ для мапы по след условиям:
    //ClassName + MethodName + fields + args, если были переданы в метод
    @SneakyThrows
    private String keyToCache(Method method, Object[] args) {
        StringBuilder result = new StringBuilder(this.obj.getClass() + method.getName());
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            result.append(f.get(this.obj));
        }
        if (args != null) {
            for (Object arg : args) {
                result.append(arg);
            }
        }
        return result.toString();
    }


}








