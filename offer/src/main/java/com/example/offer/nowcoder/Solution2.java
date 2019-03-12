package com.example.offer.nowcoder;

public class Solution2 {
    public String replaceSpace(StringBuffer str) {
        int spaceNum = 0;
        for (int i=0; i<str.length(); i++) {
            if (str.charAt(i) == ' ') {
                spaceNum++;
            }
        }
        int indexOld = str.length() - 1;
        int newLength = str.length() + spaceNum*2;
        int indexNew = newLength - 1;
        str.setLength(newLength);
        for(;indexOld>=0 && indexOld<newLength; --indexOld) {
            if (str.charAt(indexOld) == ' ') {
                str.setCharAt(indexNew--, '0');
                str.setCharAt(indexNew--, '2');
                str.setCharAt(indexNew--, '%');
            } else {
                str.setCharAt(indexNew--, str.charAt(indexOld));
            }
        }
        return str.toString();
    }

    public static void main(String[] args) {
        Solution2 solution = new Solution2();
        String result = solution.replaceSpace(new StringBuffer("We are Happy."));
        System.out.println(result);
    }
}
