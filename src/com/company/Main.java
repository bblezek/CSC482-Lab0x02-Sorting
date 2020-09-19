package com.company;

import java.lang.*;
import java.util.Random;

public class Main {

    public static char[][] generateTestList(int N, int k, int minV, int maxV){
        Random rand = new Random();
        char[][] outputArray = new char[N][k+1];
        for(int outer = 0; outer < N; outer++){
            for(int inner = 0; inner < k; inner++){
                outputArray[outer][inner] = (char)(minV + rand.nextInt(maxV-minV));
            }
            outputArray[outer][k] = '\0';
        }
        return outputArray;
    }

    public static boolean isSorted(char[][] array, int N, int k){
        for(int outer = 0; outer < N; outer++) {
            for (int inner = 1; inner < k; inner++) {
                if (array[outer][inner] < array[outer][inner - 1]) {
                    return false;
                }
            }
        }
        return true;
    }
    public static void printArray(char[][] array, int N, int k){
        String str;
        for(int out = 0; out < N; out++){
            str = String.valueOf(array[out]);
            System.out.printf("%s\t", str);
        }
        System.out.printf("\n");
        return;
    }

    public static char[][] insertionSort(char[][] arrayToSort, int N, int k){
        int outer, inner, x;
        char temp;
        for(outer = 0; outer < N; outer++){
            for(inner = 1; inner < k; inner++) {
                x = inner;
                temp = arrayToSort[outer][inner];
                while (x > 0 && temp < arrayToSort[outer][x-1]){
                    arrayToSort[outer][x] = arrayToSort[outer][x - 1];
                    x--;
                }
                arrayToSort[outer][x] = temp;
            }
        }
        return arrayToSort;
    }

    public static char[] recursiveMergeSort(char[] arrayToSort, int low, int high){
        int mid = (int)Math.ceil((high-low)/2) + low;
        char[] sortedArray = new char[high-low];
        //If there's only 2 elements in the array
        if(high-low == 2){
            //Put them in the appropriate order
            if(arrayToSort[low] > arrayToSort[high-1]){
                sortedArray[0] = arrayToSort[high-1];
                sortedArray[1] = arrayToSort[low];
            } else {
                sortedArray[0] = arrayToSort[low];
                sortedArray[1] = arrayToSort[high-1];
            }
        //If there's only one element, simply return it
        } else if(high-low == 1) {
            sortedArray[0] = arrayToSort[low];
        //If there's more than two elements
        } else if(high - low > 2){
            //Recursively sort each half
            char[] lowArray = recursiveMergeSort(arrayToSort, low, mid);
            char[] highArray = recursiveMergeSort(arrayToSort, mid, high);
            int lowIndex = 0;
            int highIndex = 0;
            //And then merge
                for (int x = 0; x < high-low; x++) {
                    //If low array has been looped through up to mid
                    if (lowIndex >= mid-low && highIndex < high-mid) {
                        //Then loop through high array
                        sortedArray[x] = highArray[highIndex];
                        highIndex++;
                    //If high array has been looped through up to high
                    } else if (highIndex >= high-mid && lowIndex < mid-low){
                        //Then loop through low array
                        sortedArray[x] = highArray[lowIndex];
                        lowIndex++;
                    //Otherwise just place lower value as usual
                    } else if (lowArray[lowIndex] < highArray[highIndex]){
                        sortedArray[x] = lowArray[lowIndex];
                        lowIndex++;
                    } else {
                        sortedArray[x] = highArray[highIndex];
                        highIndex++;
                    }
                }
            }
        return sortedArray;
    }

    public static char[][] mergeSort(char[][] arrayToSort, int N, int k){
        char[][] outputArray = new char[N][k];
        char[] sortedArray;
        char[] inputArray = new char[k];
        int outer, inner;
        for(outer = 0; outer < N; outer++) {
            //Copy
            for(inner = 0; inner < k; inner++){
                inputArray[inner] = arrayToSort[outer][inner];
            }
            sortedArray = recursiveMergeSort(inputArray, 0, k);
            //outputArray[outer] = sortedArray;
            for (inner = 0; inner < k; inner++) {
                outputArray[outer][inner] = sortedArray[inner];
            }
        }
        return outputArray;
    }
    public static char[][] radixSort(char[][] arrayToSort, int N, int k, int d){
        int digit, inner, outer, num, y, newLoc;
        int[] digitArray = new int[256];
        char[] inputString = new char[k];
        char[] outputString = new char[k];
        char[][] outputArray = new char[N][k];
        //Iterating through each outer array element of stringToSort
        for(outer = 0; outer < N; outer++) {
            for(y = 0; y < k; y++){
                inputString[y] = arrayToSort[outer][y];
            }
            //Iterating through each digit for each outer array element
            for (digit = 1; digit <= d; digit++) {
                //Iterating through inner array elements
                for (inner = 0; inner < k; inner++){
                    num = inputString[inner];
                    //Get appropriate digit
                    num = num%(int)(Math.pow(2, 8*digit));
                    num = num/(int)(Math.pow(2, 8*(digit-1)));
                    //Adding to how many numbers have that digit in the array
                    digitArray[num]++;
                }
                //Accumulating sums in digit array to make sure indices are right
                //for next iteration
                for(y = 1; y < 256; y++){
                    digitArray[y] = digitArray[y] + digitArray[y-1];
                }
                //Updating output list
                //Iterating through inner array
                for(inner = 0; inner < k; inner++){
                    //Get value from array, calculate integer value
                    num = inputString[inner];
                    //Calculate appropriate digit
                    num = num%(int)(Math.pow(2, 8*digit));
                    num = num/(int)(Math.pow(2, 8*(digit-1)));
                    //Insert at new array according to index provided by digitArray
                    newLoc = digitArray[num]-1;
                    outputString[newLoc] = inputString[inner];
                    digitArray[num]--;
                }
                //Clear digit array for next round
                for(y = 0; y < 256; y++){
                    digitArray[y] = 0;
                }
                for(inner = 0; inner < k; inner++){
                    inputString[inner] = outputString[inner];
                }
            }
            //Copying sorted array into final array
            for(y = 0; y < k; y++) {
                outputArray[outer][y] = outputString[y];
            }
        }
        return outputArray;
    }

    public static void verificationTesting(){
        int N, k;
        char[][] inputArray, outputArray;
        for(N = 10; N < 101; N = N*2){
            for(k = 6; k < 48; k = k*2){
                inputArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(inputArray, N, k);
                outputArray = insertionSort(inputArray, N, k);
                if(isSorted(outputArray, N, k)){
                    System.out.printf("Insertion sorted list: \n");
                    printArray(outputArray, N, k);
                } else {
                    System.out.printf("List not sorted!\n");
                }
                inputArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(inputArray, N, k);
                outputArray = mergeSort(inputArray, N, k);
                if(isSorted(outputArray, N, k)){
                    System.out.printf("Merge sorted list: \n");
                    printArray(outputArray, N, k);
                } else {
                    System.out.printf("List not sorted!\n");
                }
                inputArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(inputArray, N, k);
                outputArray = radixSort(inputArray, N, k, 1);
                if(isSorted(outputArray, N, k)){
                    System.out.printf("Radix sorted list: \n");
                    printArray(outputArray, N, k);
                } else {
                    System.out.printf("List not sorted!\n");
                }
            }
        }
        /*for(N = 10; N < 100000001; N++){
            for(k = 6; k < 48; k=k*2){

            }
        }*/
        return;
    }

    public static void main(String[] args) {
	// write your code here
        verificationTesting();



    }
}
