package Computational.math;

public class SimpleIteration {
//    private double[][] system;
//    private double[] answers;

    public SimpleIteration() {
//        this.system = system;
//        this.answers = answers;
    }

    public boolean diagonalPredominances(Double[][] system) {
        int dimension = system.length;
        double diagonal;
        double notDiagonalSumAbs;

        for (int i = 0; i < dimension; i++) {
            diagonal = Math.abs(system[i][i]);
            notDiagonalSumAbs = 0;
            for (int j = 0; j < dimension; j++) {
                if (i != j) {
                    notDiagonalSumAbs += Math.abs(system[i][j]);
                }
            }
            if (diagonal < notDiagonalSumAbs) {
                return false;
            }
        }
        return true;
    }
}
