package Computational.math;

public class Main {
    public static void main(String[] args) {
        Double system[][] = {
                {2d, 2d, 10d},
                {10d, 1d, 1d},
                {2d, 10d, 1d}
        };
        Double systemAnswers[] = {14d,12d,13d};
        SimpleIteration simpleIteration = new SimpleIteration();
        System.out.println(simpleIteration.diagonalPredominances(system));
    }
}