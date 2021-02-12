package edu.neu.coe.info6205;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        double n = 1.0;
        System.out.println(n==1);
    }

    public static double iteLog(double n){
        if (n==1){
            return 0.0;
        }
        else
            return (1+iteLog(log2(n)));
    }

    public static double log2(double n){
        return Math.log(n)/Math.log(2);
    }
}
