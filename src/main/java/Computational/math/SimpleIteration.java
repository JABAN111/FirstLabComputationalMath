package Computational.math;

import Computational.math.Exceptions.DiagonalPredominanceException;
import Computational.math.utils.UtilsForSimpleIteration;

import static java.lang.System.exit;

public class SimpleIteration {
    private final Double[][] system;
    private final Double[] answers;
    private Double[][] norm;
    private Double[] startApproach;
    private Double[] lastApproach;
    private final double accuracy;
    //FIXME придумать бы другую затычку для этого
    private double absoluteDeviations = 1;

    private static int MAX_ITERATION = 10000;


    public static int getIterationNumber() {
        return iterationNumber - 1;
    }

    private static int iterationNumber = 0;

    public SimpleIteration(Double[][] system, Double[] answers, double accuracy) {
        this.system = system;
        this.answers = answers;
        this.accuracy = accuracy;
    }

    public void swapRows(int positionFrom, int positionTo) {
        Double[] tmpSystemRow = this.system[positionTo];
        Double tmpAnswer = this.answers[positionTo];
        this.answers[positionTo] = this.answers[positionFrom];
        this.answers[positionFrom] = tmpAnswer;
        this.system[positionTo] = this.system[positionFrom];
        this.system[positionFrom] = tmpSystemRow;
    }
    /**
     * Приводит систему к условиям преобладания диагоналей или сообщает, что их нет
     * @throws DiagonalPredominanceException если невозможно привести данную систему к условиям преобладания диагоналей
     */
    public void toDiagonalPredominance() throws DiagonalPredominanceException {
        Double[][] matrix = system;
        int rows = system.length;
        int cols = system[0].length;
        for (int i = 0; i < rows; i++) {
            double max = matrix[i][0];
            int maxIndex = 0;
            for (int j = 1; j < cols; j++) {
                if (Math.abs(matrix[i][j]) > Math.abs(max)) {
                    max = matrix[i][j];
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                swapRows(maxIndex,i);
            }
        }
        if(!isDiagonalPredominances()){
            throw new DiagonalPredominanceException("Невозможно привести систему к преобладанию диагональных элементов");
        }

    }


    public boolean isDiagonalPredominances() {
        int dimension = this.system.length;
        double diagonal = 0;
        double notDiagonalSumAbs = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == j) {
                    diagonal = Math.abs(this.system[i][j]);
                } else {
                    notDiagonalSumAbs += Math.abs(this.system[i][j]);
                }
            }
            if (diagonal < notDiagonalSumAbs) {
                return false;
            }
            notDiagonalSumAbs = 0;
        }
        return true;
    }

    public void divideByDiagonalCoefficient() {
        if (!isDiagonalPredominances()) {
            return;
        }
        for (int i = 0; i < system.length; i++) {
            double divider = system[i][i];
            answers[i] = answers[i] / divider;
            for (int j = 0; j < system[0].length; j++) {
                system[i][j] = system[i][j] / divider;
            }
        }
    }

    /**
     * Функция, позволяющая выделить коэфициенты x1,x2,...,xn, а также найти начальное приближение(paramApproach)
     */
    public void expressCoefficient() {
        int dimension = system.length;
        this.norm = new Double[dimension][dimension];
        this.startApproach = answers;
        //FIXME возможно немного изменить логику
        this.lastApproach = startApproach;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                norm[i][j] = i != j ? -system[i][j] : 0d;
            }
        }
    }

    /**
     * Проверка на условие сходимости: ||C|| = max(startApproach) < 1
     *
     * @see #expressCoefficient()
     */
    public boolean convergenceCondition() {
        double sum = 0;
        for (Double[] row : norm) {
            for (int j = 0; j < norm.length; j++) {
                sum += row[j];
            }
            if (sum >= 1) {
                return false;
            }
            sum = 0;
        }
        return true;
    }

    public Double[] countNewApproach(Double[] approachToCount) {
        Double[] tmpTestRow = new Double[startApproach.length];
        for (int currentRow = 0; currentRow < tmpTestRow.length; currentRow++) {
            tmpTestRow[currentRow] = UtilsForSimpleIteration.roundDouble(approximationRow(currentRow, approachToCount));
        }
        return tmpTestRow;
    }


    public void approximations() {
        Double[] newTMP;
        newTMP = countNewApproach(this.lastApproach);
        if (iterationNumber == 0) {
            iterationNumber++;
            UtilsForSimpleIteration.printFinalTable(this.lastApproach, null);
            return;
        }
        double calculation = calculateAbsoluteDeviations(lastApproach, newTMP);
        this.lastApproach = newTMP;
        iterationNumber++;
        UtilsForSimpleIteration.printFinalTable(lastApproach, calculation);
    }


    /**
     * Функция для вычисления критерия по абсолютным отклонениям
     */
    public double calculateAbsoluteDeviations(Double[] currentX, Double[] previousX) {
        double[] tmp = new double[currentX.length];
        double max = 0d;
        for (int i = 0; i < currentX.length; i++) {
            tmp[i] = Math.abs(currentX[i] - previousX[i]);
            max = Math.max(tmp[i], max);
        }
        this.absoluteDeviations = max;
        return max;
    }

    /**
     * Данный метод является вспомогательным для вычисления приближения
     * @param currentRow номер ряда, для которого вычисляется приближение
     * @return сумма посчитанного приблежния
     * @see #approximations() основной метод
     */
    private Double approximationRow(int currentRow, Double[] approach) {
        double sumRow = 0d;
        for (int i = 0; i < approach.length; ++i) {
            sumRow += norm[currentRow][i] * approach[i];
        }
        return sumRow + startApproach[currentRow];
    }

    public void solve() {
        try {
            toDiagonalPredominance();
            divideByDiagonalCoefficient();
            expressCoefficient();
            if (!convergenceCondition()) {
                System.err.println("Условие сходимости не выполняется");
                exit(-1);
            }
            do {
                approximations();
                MAX_ITERATION--;
            }
            while (!(this.accuracy > this.absoluteDeviations) || MAX_ITERATION == 0);
            iterationNumber = 0;
            MAX_ITERATION = 10000;
        }
        catch (DiagonalPredominanceException e) {
            System.err.println(e.getMessage());
            iterationNumber = 0;

        }
    }
}
