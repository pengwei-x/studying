package VMProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pengwei
 * @date: 2019/11/10
 * VM Args -XX:PermSize=10M  -XX:MaxPermSize=10M
 *
 */
public class demo01 {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        int i=0;
        while (true){
            list.add(String.valueOf(i++).intern());

        }
    }
}
