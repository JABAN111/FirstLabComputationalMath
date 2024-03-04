package Computational.math;

import Computational.math.Exceptions.DiagonalPredominanceException;
import Computational.math.utils.UtilsForSimpleIteration;

import static java.lang.System.exit;

public class SimpleIteration {
    private Double[][] system;
    private Double[] answers;
    private Double[][] norm;
    private Double[] startApproach;
    private Double[] lastApproach;
    private final double accuracy;
    //FIXME придумать бы другую затычку для этого
    private double absoluteDeviations = 111;

    public SimpleIteration(Double[][] system, Double[] answers, double accuracy) {
        this.system = system;
        this.answers = answers;
        this.accuracy = accuracy;
    }

    /**
     * Приводит систему к условиям преобладания диагоналей или сообщает, что их нет
     *
     * @throws DiagonalPredominanceException если невозможно привести данную систему к условиям преобладания диагоналей
     */
    public void toDiagonalPredominance() throws DiagonalPredominanceException {
        int middleArray = getMiddleArray();
        for (int i = 0; i < system.length; i++) {
            for (int j = 0; j < system.length; j++) {
                swapRows(j, i);
                if (isDiagonalPredominances()) {
                    return;
                }
            }
            //FIXME временно 1-далее середина массива проблема есть здесь system.length - 1
//            if(i != 0) {
            swapRows(middleArray, system.length - 1);
//            }
            if (isDiagonalPredominances()) {
                return;
            }
        }
        throw new DiagonalPredominanceException("Невозможно выполнить условие преобладания");
    }

    public int getMiddleArray() {
        //TODO возможно необходимо сменить логику нахождения середины массива
        return system.length % 2 == 0 ? system.length / 2 : system.length / 2;
    }

    public void swapRows(int positionFrom, int positionTo) {
        Double[] tmpSystemRow = this.system[positionTo];
        Double tmpAnswer = this.answers[positionTo];
        this.answers[positionTo] = this.answers[positionFrom];
        this.answers[positionFrom] = tmpAnswer;
        this.system[positionTo] = this.system[positionFrom];
        this.system[positionFrom] = tmpSystemRow;
    }

    public void printSystem(Double[][] arrayToPrint) {
        for (int i = 0; i < arrayToPrint.length; i++) {
            for (int j = 0; j < arrayToPrint[i].length; j++) {
                System.out.print(arrayToPrint[i][j] + "  ");
            }
            System.out.println();
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

    public void printSystemAndAnswers(Double[][] arrayToPrint, Double[] answers) {
        for (int i = 0; i < arrayToPrint.length; i++) {
            for (int j = 0; j < arrayToPrint[i].length; j++) {
                System.out.print(arrayToPrint[i][j] + "  ");
            }
            System.out.println("|  " + answers[i]);
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


    /**
     * приближение
     */
    public void approximations() {
        Double[] tmpTestRow = new Double[lastApproach.length];
        for (int currentRow = 0; currentRow < norm.length; currentRow++) {
            tmpTestRow[currentRow] = UtilsForSimpleIteration.roundDouble(approximationRow(currentRow));
        }
        if(!equalsArrays(startApproach,lastApproach)){
            UtilsForSimpleIteration.printFinalTable(lastApproach, calculateAbsoluteDeviations(tmpTestRow, lastApproach));
            lastApproach = tmpTestRow;
        }
        else{
            UtilsForSimpleIteration.printFinalTable(lastApproach,0);
            for (double st : startApproach){
                System.out.println(st);
            }
            lastApproach = tmpTestRow;
        }
    }
    public boolean equalsArrays(Double[] firstArray,Double[] secondArray){
        for (int i = 0; i < firstArray.length; i++) {
            if(!firstArray[i].equals(secondArray[i])){
                return false;
            }
        }
        return true;
    }
    /**
     * Функция для вычисления критерия по абсолютным отклонениям
     */
    public double calculateAbsoluteDeviations(Double[] currentX, Double[] previousX){
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
     * todo правильно ли называть его рядом?(речь про currentRow)
     *
     * @param currentRow номер ряда, для которого вычисляется приближение
     * @return сумма посчитанного приблежния
     * @see #approximations() основной метод
     */
    private Double approximationRow(int currentRow) {
        double sumRow = 0d;
        for (int i = 0; i < lastApproach.length; ++i) {
            sumRow += norm[currentRow][i] * lastApproach[i];
        }
        return sumRow + startApproach[currentRow];
    }
    public void solve() throws DiagonalPredominanceException {
        toDiagonalPredominance();
//            simpleIteration.printSystemAndAnswers();
        divideByDiagonalCoefficient();
        expressCoefficient();
        //^ последний адекватный этап
        if(!convergenceCondition()){
            System.out.println("ne rabotaet");
            exit(-1);
        }
        do{
            approximations();
        }
        while (!(this.accuracy > this.absoluteDeviations));
    }
}
