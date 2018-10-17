package test;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Set;

public class TestGeneric {
    public static void test() {
        Set<String> ss = new HashSet<>();
        ss.add("abc");
        ss.add("123");

//        ss.toArray(Array.newInstance((Class<String>) ((ParameterizedType) ss.getClass().getGenericSuperclass()).getActualTypeArguments()[0], 10));

//        Object[] s = (Object[]) Array.newInstance(ss.getClass().getGenericSuperclass().getClass(), 10);
        Object[] s = ss.toArray();

        System.out.println((String) s[0]);
        System.out.println((String) s[1]);
    }
}
