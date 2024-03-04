package Computational.math;

import Computational.math.Exceptions.DiagonalPredominanceException;

public class Main {
    public static void main(String[] args) {
        Double[][] system = {
                {2d, 2d, 10d},
                {10d, 1d, 1d},
                {2d, 10d, 1d}
        };
//        Double system[][] = {
//                {10d,2d,2d,2d},
//                {1d,7d,10d,1d},
//                {9d,21d,1d,1d},
//                {9d,7d,1d,92d}
//
//        };
        Double[] systemAnswers = {14d,12d,13d};
        double epsilon = 0.01;
        SimpleIteration simpleIteration = new SimpleIteration(system,systemAnswers,epsilon);
        try {
            simpleIteration.solve();


        } catch (DiagonalPredominanceException e) {
            System.out.println(e.getMessage());
        }
    }



}