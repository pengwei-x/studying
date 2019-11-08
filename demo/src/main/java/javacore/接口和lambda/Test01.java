package javacore.接口和lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntBinaryOperator;

/**
 * Created by Pengwei
 * Date:2019/11/2
 */
public class Test01 {
    public static void main(String[] args) {
        int compare = Double.compare(2.0, 2.0);
        System.out.println(compare);
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 5, 5, 5, 6, 7);
        numbers.forEach(a ->{
            System.out.println(a.toString());
        });


    }
}
