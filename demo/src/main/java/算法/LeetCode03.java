package 算法;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pengwei
 * @date: 2019/11/15
 */
public class LeetCode03 {
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length(), ans = 0;
        int[] index = new int[127]; // current index of character
        // try to extend the range [i, j]
        for (int j = 0, i = 0; j < n; j++) {
            i = Math.max(index[s.charAt(j)], i);
            ans = Math.max(ans, j - i + 1);
            index[s.charAt(j)] = j + 1;
        }
        return ans;

    }

    public static void main(String[] args) {

        int aaddc = lengthOfLongestSubstring("aaddcbz   ");
        System.out.println(aaddc);
    }
}
