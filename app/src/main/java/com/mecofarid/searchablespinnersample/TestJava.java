package com.mecofarid.searchablespinnersample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestJava {
    static Integer id = 0;
    public static void main(String[] args){
         sumUntil(10);
    }

    private static void sumUntil(Integer integer) {
        if (integer >= 0) {
            id++;
            System.out.println("meco "+id);
            sumUntil(integer-1);
        }
    }
}
