package com.webdev.jobify._aux;





public class MatrixFactorization {

    private static int dotProduct(int vect_A[], int vect_B[])
    {
        int product = 0;
        int n = vect_A.length;
        // Loop for calculate cot product
        for (int i = 0; i < n; i++)
            product = product + vect_A[i] * vect_B[i];
        return product;
    }

    public static MatrixFactElement getMax1(MatrixFactElement[] inputArray){
        MatrixFactElement maxValue = new MatrixFactElement();

        maxValue.value = inputArray[0].value;
        maxValue.object1 = inputArray[0].object2;

        for(int i=1;i < inputArray.length;i++){

            if(inputArray[i].value > maxValue.value){
                maxValue.value = inputArray[i].value;
                maxValue.object1 = inputArray[i].object2;
            }
        }
        return maxValue;
    }

    public static MatrixFactElement getMax2(MatrixFactElement[] inputArray){
        MatrixFactElement maxValue = new MatrixFactElement();

        maxValue.value = inputArray[0].value;
        maxValue.object1 = inputArray[0].object2;

        for(int i=1;i < inputArray.length;i++){

            if(inputArray[i].value > maxValue.value){
                maxValue.value = inputArray[i].value;
                maxValue.object1 = inputArray[i].object2;
            }
        }
        return maxValue;
    }

    public static MatrixFactElement[][] matrixFactorization(int[][] R, MatrixFactElement[][] P, MatrixFactElement[][] Q, int K) {

        for(int step=0 ; step < 5000 ; step++){

                for(int i=0 ; i < R.length ; i++){
                    for(int j=0 ; j < R[0].length ; j++){

                        if(R[i][j] > 0) {

                            int[] Ri = R[i]; // row
                            int[] Rj = new int[R.length]; // column
                            for(int index = 0 ; index < R.length ; index++) {
                                Rj[index] = R[index][j];
                            }

                            int eij = R[i][j] - dotProduct(Ri, Rj);

                            for(int k = 0 ; k < K ; k++) {
                                P[i][k].value = (P[i][k].value + 0.0002 * (2 * eij * Q[k][j].value - 0.02 * P[i][k].value));
                                Q[k][j].value = (P[k][j].value + 0.0002 * (2 * eij * P[i][k].value - 0.02 * Q[k][j].value));
                            }
                        }
                    }
                }
                double e = 0.0;

                for(int i=0 ; i < R.length ; i++) {
                    for (int j = 0; j < R[0].length; j++) {

                        if (R[i][j] > 0) {

                            int[] Ri = R[i]; // row
                            int[] Rj = new int[R.length]; // column
                            for (int index = 0; index < R.length; index++) {
                                Rj[index] = R[index][j];
                            }

                            int eij = R[i][j] - dotProduct(Ri, Rj);
                            e = e + Math.pow(eij, 2);
                            for(int k = 0 ; k < K ; k++) {
                                e = e + (0.02/2) * (Math.pow(P[i][k].value,2) + Math.pow(P[k][j].value,2));
                            }
                        }
                    }
                }
                if(e < 0.001) {
                    break;
                }

        }


        MatrixFactElement[][]Rd = new MatrixFactElement[R.length][R[0].length];

        for(int a = 0 ; a < R.length ; a++) {
            for(int b = 0 ; b < R[0].length ; b++){
                Rd[a][b] = new MatrixFactElement();
            }
        }

        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < Q[0].length; j++) {
                for (int k = 0; k < P[0].length; k++) {
                    Rd[i][j].value += P[i][k].value * Q[k][j].value;
                    Rd[i][j].object1 = P[i][k].object1;
                    Rd[i][j].object2 = Q[k][j].object1;
                }
            }
        }


        return Rd;

    }

}
