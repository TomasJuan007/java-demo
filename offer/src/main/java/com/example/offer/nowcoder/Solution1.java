package com.example.offer.nowcoder;


/*二维数组中的查找*/
/*在一个二维数组中，每一行从左到右递增，每一列从上到下递增，请完成一个函数，输入这样一个二维数组和一个整数，判断数组中是否有该整数*/
public class Solution1 {
    public boolean find(int target, int[][] array) {
        int row = array.length - 1;
        int column = 0;
        while (row >= 0 && column < array[0].length) {
            int temp = array[row][column];
            if (temp == target) {
                return true;
            } else if (target > temp) {
                row--;
            } else {
                column++;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Solution1 solution = new Solution1();
        int[][] array = new int[3][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                array[i][j] = i+j;
            }
        }
        boolean test = solution.find(2, array);
        System.out.println(test);
    }
}
