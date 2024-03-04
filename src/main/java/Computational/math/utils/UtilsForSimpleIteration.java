package Computational.math.utils;

import java.util.Arrays;

public class UtilsForSimpleIteration {
    public static void printFinalTable(Double[] lastApproach, Double arrayRes){
        Arrays.stream(lastApproach).map(approach -> approach + " | ").forEach(System.out::print);
        if(arrayRes == null){
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
    public static void printSystem(Double[][] arrayToPrint) {
        for (Double[] doubles : arrayToPrint) {
            for (Double aDouble : doubles) {
                System.out.print(aDouble + "  ");
            }
            System.out.println();
        }
    }
    public static void printSystemAndAnswers(Double[][] arrayToPrint, Double[] answers) {
        for (int i = 0; i < arrayToPrint.length; i++) {
            for (int j = 0; j < arrayToPrint[i].length; j++) {
                System.out.print(arrayToPrint[i][j] + "  ");
            }
            System.out.println("|  " + answers[i]);
        }
    }
    public static boolean equalsArrays(Double[] firstArray,Double[] secondArray){
        for (int i = 0; i < firstArray.length; i++) {
            if(!firstArray[i].equals(secondArray[i])){
                return false;
            }
        }
        return true;
    }
}
