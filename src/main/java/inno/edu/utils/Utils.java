package inno.edu.utils;

import inno.edu.MakeCache;
import inno.edu.TimeManagement;

import java.lang.reflect.Proxy;

public class Utils {
    public static <T> T cache(T objectIncome) {
        return (T) Proxy.newProxyInstance(
                objectIncome.getClass().getClassLoader(),
                objectIncome.getClass().getInterfaces(),
                new MakeCache((T) objectIncome)
        );
    }
}

