package com.redsponge.animationgui;

public class Utils {

    public static <T> boolean arrayContains(T[] arr, T item) {
        for (T t : arr) {
            if(t.equals(item)) {
                return true;
            }
        }
        return false;
    }

}
