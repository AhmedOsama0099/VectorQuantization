package com.vectorquantization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        /*int[][] imageMatrix = {{1, 2, 7, 9, 4, 11},
                {3, 4, 6, 6, 12, 12},
                {4, 9, 15, 14, 9, 9},
                {10, 10, 20, 18, 8, 8},
                {4, 3, 17, 16, 1, 4},
                {4, 5, 18, 18, 5, 6}};*/
        VectorQuantization v=new VectorQuantization();
        v.compress(2,2,64,"test.jpg");
        try {
            v.deCompress("test.jpg.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
