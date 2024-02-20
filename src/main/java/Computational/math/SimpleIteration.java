package Computational.math;

import Computational.math.Exceptions.DiagonalPredominanceException;

public class SimpleIteration {
    private Double[][] system;
    private Double[] answers;
    private Double[][] norm;
    private Double[] startApproach;

    public SimpleIteration(Double[][] system,Double[] answers) {
        this.system = system;
        this.answers = answers;
    }


    /**
     * Приводит систему к условиям преобладания диагоналей или сообщает, что их нет
     * @throws DiagonalPredominanceException если невозможно привести данную систему к условиям преобладания диагоналей
     */
    public void toDiagonalPredominaces() throws DiagonalPredominanceException {
        int middleArray = getMiddleArray();
        for (int i = 0; i < system.length; i++) {
            for (int j = 0; j < system.length; j++) {
                swapRows(j,i);
                if(isDiagonalPredominances()){
                    return;
                }
            }
            //временно 1-далее середина массива
            //проблема есть здесь system.length - 1 хотя я не уверен !КОСЯК
//            if(i != 0) {
                swapRows(middleArray, system.length - 1);
//            }
            if(isDiagonalPredominances()){
                return;
            }
        }
        throw new DiagonalPredominanceException("Невозможно выполнить условие преобладания");
    }
    public int getMiddleArray(){
        return system.length % 2 == 0 ? system.length/2 : system.length/2;
    }

    public void swapRows(int positionFrom, int positionTo){
        Double[] tmpSystemRow= this.system[positionTo];
        Double tmpAnswer = this.answers[positionTo];
        this.answers[positionTo] = this.answers[positionFrom];
        this.answers[positionFrom] = tmpAnswer;
        this.system[positionTo] = this.system[positionFrom];
        this.system[positionFrom] = tmpSystemRow;
    }
    public void printSystemAndAnswers(Double[][] arrayToPrint,Double[] answers){
        for (int i = 0; i < arrayToPrint.length; i++) {
            for (int j = 0; j < arrayToPrint[i].length; j++) {
                System.out.print(arrayToPrint[i][j] + "  ");
            }
            System.out.println("|  " + answers[i]);
        }
    }
    public void printSystem(Double[][] arrayToPrint){
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
                if(i==j){
                    diagonal = Math.abs(this.system[i][j]);
                }
                else{
                    notDiagonalSumAbs += Math.abs(this.system[i][j]);
                }
            }
            if(diagonal < notDiagonalSumAbs) {
                return false;
            }
            notDiagonalSumAbs = 0;
        }
        return true;
    }

    public void divideByDiagonalCoefficient(){
        if(!isDiagonalPredominances()){
            return;
        }
        for (int i = 0; i < system.length; i++) {
            double divider = system[i][i];
            answers[i] = answers[i]/divider;
            for (int j = 0; j < system[0].length; j++) {
                system[i][j] = system[i][j]/divider;
            }
        }
    }
    public void expressCoefficient(){
        int dimension = system.length;
        this.norm = new Double[dimension][dimension];
        this.startApproach = answers;
        for (int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                norm[i][j] = i != j ? -system[i][j] : 0d;
            }
        }
    printSystemAndAnswers(norm,startApproach);
    }
    public boolean convergenceCondition(){
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

}
