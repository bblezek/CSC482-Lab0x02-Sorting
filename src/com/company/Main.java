package com.company;

import java.lang.*;
import java.util.Random;

public class Main {

    //Generates lists of random characters with null bytes at the end
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

    //Function to test if the inner arrays of an array of arrays are sorted
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

    //Simple function to print an array of arrays
    public static void printArray(char[][] array, int N, int k){
        String str;
        for(int out = 0; out < N; out++){
            str = String.valueOf(array[out]);
            System.out.printf("%s\t", str);
        }
        System.out.printf("\n");
        return;
    }

    public static void insertionSort(char[][] arrayToSort, int N, int k){
        int outer, inner, x;
        char temp;
        //Looping through each element, each char array, in the outer array
        for(outer = 0; outer < N; outer++){
            //Looping through each element of the inner char arrays
            for(inner = 1; inner < k; inner++) {
                x = inner;
                temp = arrayToSort[outer][inner];
                //Traverses down list from inner
                //Tests whether temp is less than the next lowest element
                //If it is, move that element up
                while (x > 0 && temp < arrayToSort[outer][x-1]){
                    arrayToSort[outer][x] = arrayToSort[outer][x - 1];
                    x--;
                }
                //When temp is no longer less than the next lowest element
                //Insert temp at x
                arrayToSort[outer][x] = temp;
            }
        }
        return;
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
                        sortedArray[x] = lowArray[lowIndex];
                        lowIndex++;
                    //Otherwise, if lowArray value is less than highArray value
                    //place low array value in sorted array
                    } else if (lowArray[lowIndex] < highArray[highIndex]){
                        sortedArray[x] = lowArray[lowIndex];
                        lowIndex++;
                    //Or place high value in sorted array
                    } else {
                        sortedArray[x] = highArray[highIndex];
                        highIndex++;
                    }
                }
            }
        return sortedArray;
    }

    //This function simply calls recursiveMergeSort on each char array
    //of the outer array of arrays
    public static void mergeSort(char[][] arrayToSort, int N, int k){
        int outer;
        for(outer = 0; outer < N; outer++) {
            arrayToSort[outer] = recursiveMergeSort(arrayToSort[outer], 0, k);
        }
        return;
    }

    //NOTE: high is the highest array index, NOT the number of elements in the array
    public static void recursiveQuickSort(char[] arrayToSort, int low, int high) {
        char pivot, temp;
        int l, h;
        boolean pivotLocFound = false;
        //If we are looking at only one element
        //Or got passed in an "incorrect" value for high
        if(high <= low){
            return;
        } else {
            //Pivot is lowest element of array
            pivot = arrayToSort[low];
            l = low + 1;
            h = high;
            //Looping until we find where the pivot char should be placed
            while(!pivotLocFound) {
                //"Scanning" left until we find an element larger than the pivot
                while (l < high && arrayToSort[l] < pivot) {
                    l++;
                }
                //Scanning right until we find an element smaller than the pivot
                while (h > low && arrayToSort[h] > pivot) {
                    h--;
                }
                //If l and h cross, we have found the new location for our pivot
                if(l >= h){
                    pivotLocFound = true;
                //Otherwise, we swap the elements at l and h
                //And continue scanning until we find our pivot location
                } else {
                    temp = arrayToSort[l];
                    arrayToSort[l] = arrayToSort[h];
                    arrayToSort[h] = temp;
                    //Incrementing l and decrementing h since we just swapped
                    //those elements and there is no need to "review" them
                    l++;
                    h--;
                }
            }
            //Placing pivot at new location
            arrayToSort[low] = arrayToSort[h];
            arrayToSort[h] = pivot;
            //Sorting elements to left and right of pivot
            recursiveQuickSort(arrayToSort, low, h - 1);
            recursiveQuickSort(arrayToSort, h + 1, high);
        }
        return;
    }

    //This function just calls recursiveQuickSort on each inner char array at
    //each location in the array of char arrays
    //This function and recursiveQuickSort are based heavily on the book's
    //implementation of quickSort on pages 290 and 291
    //I did attempt to figure out how to do this on my own, but after a few
    //hours of work, I resorted to implementing my own version of the book's
    //implementation
    public static void quickSort(char[][] arrayToSort, int N, int k){
        int outer;
        for(outer = 0; outer < N; outer++){
            recursiveQuickSort(arrayToSort[outer], 0, k-1);
        }
        return;
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
                    num = num/(int)(Math.pow(2, 8*(digit-1)));
                    num = num%(int)(Math.pow(2, 8));
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
                for(inner = k-1; inner >= 0; inner--){
                    //Get value from array, calculate integer value
                    num = inputString[inner];
                    //Calculate appropriate digit
                    num = num/(int)(Math.pow(2, 8*(digit-1)));
                    num = num%(int)(Math.pow(2, 8));
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

    public static boolean verificationTesting(){
        int N, k;
        char[][] inputArray, outputArray;
        //This loop is for visual verification
        for(N = 10; N < 51; N = N+10){
            for(k = 6; k < 48; k = k*2){
                //Testing insertion sort
                inputArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(inputArray, N, k);
                insertionSort(inputArray, N, k);
                if(isSorted(inputArray, N, k)){
                    System.out.printf("Insertion sorted list: \n");
                    printArray(inputArray, N, k);
                } else {
                    System.out.printf("List not sorted!\n");
                    return false;
                }

                //Testing merge sort
                inputArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(inputArray, N, k);
                mergeSort(inputArray, N, k);
                if(isSorted(inputArray, N, k)){
                    System.out.printf("Merge sorted list: \n");
                    printArray(inputArray, N, k);
                } else {
                    System.out.printf("List not sorted!\n");
                    return false;
                }

                //Testing quick sort
                inputArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(inputArray, N, k);
                quickSort(inputArray, N, k);
                if(isSorted(inputArray, N, k)){
                    System.out.printf("Quick sorted list: \n");
                    printArray(inputArray, N, k);
                } else {
                    System.out.printf("List not sorted!\n");
                    return false;
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
                    return false;
                }
            }
        }
        //This loop is for testing successively larger lists
        for(N = 10; N < 10000001; N=N*10){
            for(k = 6; k < 48; k=k*2){

                //Testing insertion sort
                inputArray = generateTestList(N, k, 1, (int)Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                insertionSort(inputArray, N, k);
                System.out.printf("Sorting with insertion sort: \n");
                if(isSorted(inputArray, N, k)){
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Insertion sort failed!\n");
                    return false;
                }

                //Testing merge sort
                inputArray = generateTestList(N, k, 1, (int)Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                mergeSort(inputArray, N, k);
                System.out.printf("Sorting with merge sort: \n");
                if(isSorted(inputArray, N, k)){
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Merge sort failed!\n");
                    return false;
                }

                //Testing quick sort
                inputArray = generateTestList(N, k, 1, (int)Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                quickSort(inputArray, N, k);
                System.out.printf("Sorting with quick sort: \n");
                if(isSorted(inputArray, N, k)){
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Quick sort failed!\n");
                    return false;
                }

                //Testing radix sort
                inputArray = generateTestList(N, k, 1, (int)Math.pow(2, 24));
                System.out.printf("Generating list of length %d, with key width %d \n",
                        N, k);
                outputArray = radixSort(inputArray, N, k, 3);
                System.out.printf("Sorting with radix sort: \n");
                if(isSorted(outputArray, N, k)){
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Radix sort failed!\n");
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
	// write your code here
        if(verificationTesting()){
            System.out.printf("All tests passed!\n");
        }
    }
}
