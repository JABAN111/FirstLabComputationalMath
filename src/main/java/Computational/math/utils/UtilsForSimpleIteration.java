package Computational.math.utils;

import java.util.Arrays;

public class UtilsForSimpleIteration {
    public static void printFinalTable(Double[] lastApproach, double arrayRes){
        Arrays.stream(lastApproach).map(approach -> approach + " | ").forEach(System.out::print);
        if(arrayRes == 0d){
            System.out.print("-" + "\n");
        }
        else {
            System.out.print(roundDouble(arrayRes) + "\n");
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }
    public static Double roundDouble(Double num){
        return Double.parseDouble(String.format("%.5f",num).replace(",","."));
    }
}
