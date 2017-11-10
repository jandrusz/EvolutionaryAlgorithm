package com.pstkm.util;

import java.util.List;
import com.google.common.collect.Lists;

public class Utils {

    public static Integer newtonSymbol(Integer n, Integer k) {
        Integer result = 1;
        for (int i = 1; i <= k; i++)
            result = result * (n - i + 1) / i;
        return result;
    }

    public static List<Integer> prepareEmptyListWithNZeroElements(int N) {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < N; i++) {
            list.add(0);
        }
        return list;
    }

    public static List<Integer> prepareEmptyListWithSequenceElements(int N) {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        return list;
    }

}
