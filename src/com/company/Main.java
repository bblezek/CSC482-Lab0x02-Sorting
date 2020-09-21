package com.company;

import java.lang.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class Main {

    //Generates lists of random characters with null bytes at the end
    public static char[][] generateTestList(int N, int k, int minV, int maxV) {
        Random rand = new Random();
        char[][] outputArray = new char[N][k + 1];
        for (int outer = 0; outer < N; outer++) {
            for (int inner = 0; inner < k; inner++) {
                outputArray[outer][inner] = (char) (minV + rand.nextInt(maxV - minV));
            }
            outputArray[outer][k] = '\0';
        }
        return outputArray;
    }

    //Function to test if the inner arrays of an array of arrays are sorted
    public static boolean isSorted(char[][] array, int N, int k) {
        for (int outer = 0; outer < N; outer++) {
            for (int inner = 1; inner < k; inner++) {
                if (array[outer][inner] < array[outer][inner - 1]) {
                    return false;
                }
            }
        }
        return true;
    }

    //Simple function to print an array of arrays
    public static void printArray(char[][] array, int N) {
        String str;
        for (int out = 0; out < N; out++) {
            str = String.valueOf(array[out]);
            System.out.printf("%s\t", str);
        }
        System.out.printf("\n");
        return;
    }

    public static void insertionSort(char[][] arrayToSort, int N, int k) {
        int outer, inner, x;
        char temp;
        //Looping through each element, each char array, in the outer array
        for (outer = 0; outer < N; outer++) {
            //Looping through each element of the inner char arrays
            for (inner = 1; inner < k; inner++) {
                x = inner;
                temp = arrayToSort[outer][inner];
                //Traverses down list from inner
                //Tests whether temp is less than the next lowest element
                //If it is, move that element up
                while (x > 0 && temp < arrayToSort[outer][x - 1]) {
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

    public static char[] recursiveMergeSort(char[] arrayToSort, int low, int high) {
        int mid = (int) Math.ceil((high - low) / 2) + low;
        char[] sortedArray = new char[high - low];
        //If there's only 2 elements in the array
        if (high - low == 2) {
            //Put them in the appropriate order
            if (arrayToSort[low] > arrayToSort[high - 1]) {
                sortedArray[0] = arrayToSort[high - 1];
                sortedArray[1] = arrayToSort[low];
            } else {
                sortedArray[0] = arrayToSort[low];
                sortedArray[1] = arrayToSort[high - 1];
            }
            //If there's only one element, simply return it
        } else if (high - low == 1) {
            sortedArray[0] = arrayToSort[low];
            //If there's more than two elements
        } else if (high - low > 2) {
            //Recursively sort each half
            char[] lowArray = recursiveMergeSort(arrayToSort, low, mid);
            char[] highArray = recursiveMergeSort(arrayToSort, mid, high);
            int lowIndex = 0;
            int highIndex = 0;
            //And then merge
            for (int x = 0; x < high - low; x++) {
                //If low array has been looped through up to mid
                if (lowIndex >= mid - low && highIndex < high - mid) {
                    //Then loop through high array
                    sortedArray[x] = highArray[highIndex];
                    highIndex++;
                    //If high array has been looped through up to high
                } else if (highIndex >= high - mid && lowIndex < mid - low) {
                    //Then loop through low array
                    sortedArray[x] = lowArray[lowIndex];
                    lowIndex++;
                    //Otherwise, if lowArray value is less than highArray value
                    //place low array value in sorted array
                } else if (lowArray[lowIndex] < highArray[highIndex]) {
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
    public static void mergeSort(char[][] arrayToSort, int N, int k) {
        int outer;
        for (outer = 0; outer < N; outer++) {
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
        if (high <= low) {
            return;
        } else {
            //Pivot is lowest element of array
            pivot = arrayToSort[low];
            l = low + 1;
            h = high;
            //Looping until we find where the pivot char should be placed
            while (!pivotLocFound) {
                //"Scanning" left until we find an element larger than the pivot
                while (l < high && arrayToSort[l] < pivot) {
                    l++;
                }
                //Scanning right until we find an element smaller than the pivot
                while (h > low && arrayToSort[h] > pivot) {
                    h--;
                }
                //If l and h cross, we have found the new location for our pivot
                if (l >= h) {
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
    public static void quickSort(char[][] arrayToSort, int N, int k) {
        int outer;
        for (outer = 0; outer < N; outer++) {
            recursiveQuickSort(arrayToSort[outer], 0, k - 1);
        }
        return;
    }

    public static void radixSort(char[][] arrayToSort, int N, int k, int d) {
        int digit, inner, outer, num, y, newLoc;
        int[] digitArray = new int[256];
        char[] inputString = new char[k];
        char[] outputString = new char[k];
        //Iterating through each outer array element of stringToSort
        for (outer = 0; outer < N; outer++) {
            for (y = 0; y < k; y++) {
                inputString[y] = arrayToSort[outer][y];
            }
            //Iterating through each digit for each outer array element
            for (digit = 1; digit <= d; digit++) {
                //Iterating through inner array elements
                for (inner = 0; inner < k; inner++) {
                    num = inputString[inner];
                    //Get appropriate digit
                    num = num / (int) (Math.pow(2, 8 * (digit - 1)));
                    num = num % (int) (Math.pow(2, 8));
                    //Adding to how many numbers have that digit in the array
                    digitArray[num]++;
                }
                //Accumulating sums in digit array to make sure indices are right
                //for next iteration
                for (y = 1; y < 256; y++) {
                    digitArray[y] = digitArray[y] + digitArray[y - 1];
                }
                //Updating output list
                //Iterating through input array backwards so that
                //numbers with a higher "lower digit" are placed before
                //those with the same "current digit" but a lower "lower digit"
                for (inner = k - 1; inner >= 0; inner--) {
                    //Get value from array, calculate integer value
                    num = inputString[inner];
                    //Calculate appropriate digit
                    num = num / (int) (Math.pow(2, 8 * (digit - 1)));
                    num = num % (int) (Math.pow(2, 8));
                    //Insert at new array according to index provided by digitArray
                    newLoc = digitArray[num] - 1;
                    outputString[newLoc] = inputString[inner];
                    //Decrementing index at that spot in the digit array
                    digitArray[num]--;
                }
                //Clear digit array for next round
                for (y = 0; y < 256; y++) {
                    digitArray[y] = 0;
                }
                //Copying output string to input string for next round
                for (inner = 0; inner < k; inner++) {
                    inputString[inner] = outputString[inner];
                }
            }
            //Copying sorted array into final array
            for (y = 0; y < k; y++) {
                arrayToSort[outer][y] = outputString[y];
            }
        }
        return;
    }

    public static boolean verificationTesting() {
        int N, k;
        char[][] charArray;
        //This loop is for visual verification
        for (N = 10; N < 51; N = N + 10) {
            for (k = 6; k < 48; k = k * 2) {
                //Testing insertion sort
                charArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(charArray, N);
                insertionSort(charArray, N, k);
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Insertion sorted list: \n");
                    printArray(charArray, N);
                } else {
                    System.out.printf("Insertion sort failed!\n");
                    return false;
                }

                //Testing merge sort
                charArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(charArray, N);
                mergeSort(charArray, N, k);
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Merge sorted list: \n");
                    printArray(charArray, N);
                } else {
                    System.out.printf("Merge sort failed!\n");
                    return false;
                }

                //Testing quick sort
                charArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(charArray, N);
                quickSort(charArray, N, k);
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Quick sorted list: \n");
                    printArray(charArray, N);
                } else {
                    System.out.printf("Quick sort failed!\n");
                    return false;
                }

                //Testing radix sort
                charArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(charArray, N);
                radixSort(charArray, N, k, 1);
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Radix sorted list: \n");
                    printArray(charArray, N);
                } else {
                    System.out.printf("Radix sort failed!\n");
                    return false;
                }
            }
        }
        //This loop is for testing successively larger lists
        for (N = 10; N < 1000001; N = N * 10) {
            for (k = 6; k < 48; k = k * 2) {

                //Testing insertion sort
                charArray = generateTestList(N, k, 1, (int) Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                insertionSort(charArray, N, k);
                System.out.printf("Sorting with insertion sort: \n");
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Insertion sort failed!\n");
                    return false;
                }

                //Testing merge sort
                charArray = generateTestList(N, k, 1, (int) Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                mergeSort(charArray, N, k);
                System.out.printf("Sorting with merge sort: \n");
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Merge sort failed!\n");
                    return false;
                }

                //Testing quick sort
                charArray = generateTestList(N, k, 1, (int) Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                quickSort(charArray, N, k);
                System.out.printf("Sorting with quick sort: \n");
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Quick sort failed!\n");
                    return false;
                }

                //Testing radix sort
                charArray = generateTestList(N, k, 1, (int) Math.pow(2, 24));
                System.out.printf("Generating list of length %d, with key width %d \n",
                        N, k);
                radixSort(charArray, N, k, 3);
                System.out.printf("Sorting with radix sort: \n");
                if (isSorted(charArray, N, k)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Radix sort failed!\n");
                    return false;
                }
            }
        }
        return true;
    }

    public static long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }

    public static void runTimeTests() {
        //Looping variables
        //prevTimeLoc is for our previous time array for each value of k
        int N, k, x, prevTimeLoc;
        //Keeps track of how long each run of N takes
        long lastTime = 0;
        long maxTime = 2400000000L; //240000000000L

        //averageTime accumulates times for all runs of a particular value of N and k
        //It is then divided by timesToRun to calculate average
        long averageTime = 0;
        long timeBefore, timeAfter;

        //Number of digits for each array (will end up being 2^(8*d) characters)
        int d = 3;

        //Array to be generated and sorted
        char[][] array;

        //Number of times to run each combination of N and k
        int timesToRun = 20;

        //Keeps track of time the last run of k (for N/2) took
        long[] prevTime = {0, 0, 0, 0};

        //Testing insertion sort times
        System.out.printf("Insertion Sort: \n");
        //Printing first value of row
        System.out.printf("%31s %31s %31s %31s\n", "k=6", "k=12", "k=24", "k=48");
        System.out.printf("%31s %15s %15s %15s %15s %15s %15s %15s %15s\n", "Time", "Doubling Ratio", "Time",
                "Doubling Ratio", "Time", "Doubling Ratio", "Time", "Doubling Ratio", "Predicted");
        System.out.printf("%160s\n", "Doubling");
        System.out.printf("%15s %144s\n", "N", "Ratio");

        //Looping through N
        for (N = 1; lastTime < maxTime; N = N * 2) {
            System.out.printf("%15d ", N);
            prevTimeLoc = 0;
            lastTime = getCpuTime();
            //Looping through values of k
            for (k = 6; k <= 48; k = k * 2) {
                averageTime = 0;
                //Running a number of times for each combination of N and k
                for (x = 0; x < timesToRun; x++) {
                    array = generateTestList(N, k, 1, (int) Math.pow(2, (8 * d)));
                    timeBefore = getCpuTime();
                    insertionSort(array, N, k);
                    timeAfter = getCpuTime();
                    averageTime = averageTime + timeAfter - timeBefore;
                }
                //Dividing accumulated times by the times the sort ran to get the average time
                averageTime = averageTime / timesToRun;
                //Calculating place in array of previous time values to get average time from last value of N
                //And then replace it with the new value
                if (N == 1) {
                    System.out.printf("%15d %15s ", averageTime, "na");
                } else {
                    System.out.printf("%15d %15.3f ", averageTime, (float) averageTime / prevTime[prevTimeLoc]);
                }
                //Update prevTime array
                prevTime[prevTimeLoc] = averageTime;
                prevTimeLoc++;
            }
            System.out.printf("%15d \n", 4);
            lastTime = getCpuTime() - lastTime;
        }

        //Testing merge sort times
        System.out.printf("Merge Sort: \n");
        //Printing first value of row
        System.out.printf("%31s %31s %31s %31s\n", "k=6", "k=12", "k=24", "k=48");
        System.out.printf("%31s %15s %15s %15s %15s %15s %15s %15s %15s\n", "Time", "Doubling Ratio", "Time",
                "Doubling Ratio", "Time", "Doubling Ratio", "Time", "Doubling Ratio", "Predicted");
        System.out.printf("%160s\n", "Doubling");
        System.out.printf("%15s %144s\n", "N", "Ratio");

        //Resetting values of prevTime and lastTime
        for (x = 0; x < 4; x++) {
            prevTime[x] = 0;
        }
        lastTime = 0;
        //Looping through N
        for (N = 1; lastTime < maxTime; N = N * 2) {
            System.out.printf("%15d ", N);
            prevTimeLoc = 0;
            lastTime = getCpuTime();
            //Looping through values of k
            for (k = 6; k <= 48; k = k * 2) {
                averageTime = 0;
                //Running a number of times for each combination of N and k
                for (x = 0; x < timesToRun; x++) {
                    array = generateTestList(N, k, 1, (int) Math.pow(2, (8 * d)));
                    timeBefore = getCpuTime();
                    mergeSort(array, N, k);
                    timeAfter = getCpuTime();
                    averageTime = averageTime + timeAfter - timeBefore;
                }
                //Dividing accumulated times by the times the sort ran to get the average time
                averageTime = averageTime / timesToRun;
                //Calculating place in array of previous time values to get average time from last value of N
                //And then replace it with the new value
                if (N == 1) {
                    System.out.printf("%15d %15s ", averageTime, "na");
                } else {
                    System.out.printf("%15d %15.3f ", averageTime, (float) averageTime / prevTime[prevTimeLoc]);
                }
                //Update prevTime array
                prevTime[prevTimeLoc] = averageTime;
                prevTimeLoc++;
            }
            System.out.printf("%15.3f \n", (N * Math.log(N) / ((N / 2) * Math.log(N / 2))));
            lastTime = getCpuTime() - lastTime;
        }

        //Resetting values of prevTime and lastTime
        for (x = 0; x < 4; x++) {
            prevTime[x] = 0;
        }
        lastTime = 0;
        //Testing quick sort times
        System.out.printf("Quick Sort: \n");
        //Printing first value of row
        System.out.printf("%31s %31s %31s %31s\n", "k=6", "k=12", "k=24", "k=48");
        System.out.printf("%31s %15s %15s %15s %15s %15s %15s %15s %15s\n", "Time", "Doubling Ratio", "Time",
                "Doubling Ratio", "Time", "Doubling Ratio", "Time", "Doubling Ratio", "Predicted");
        System.out.printf("%160s\n", "Doubling");
        System.out.printf("%15s %144s\n", "N", "Ratio");

        //Looping through N
        for (N = 1; lastTime < maxTime; N = N * 2) {
            System.out.printf("%15d ", N);
            prevTimeLoc = 0;
            lastTime = getCpuTime();
            //Looping through values of k
            for (k = 6; k <= 48; k = k * 2) {
                averageTime = 0;
                //Running a number of times for each combination of N and k
                for (x = 0; x < timesToRun; x++) {
                    array = generateTestList(N, k, 1, (int) Math.pow(2, (8 * d)));
                    timeBefore = getCpuTime();
                    quickSort(array, N, k);
                    timeAfter = getCpuTime();
                    averageTime = averageTime + timeAfter - timeBefore;
                }
                //Dividing accumulated times by the times the sort ran to get the average time
                averageTime = averageTime / timesToRun;
                //Calculating place in array of previous time values to get average time from last value of N
                //And then replace it with the new value
                if (N == 1) {
                    System.out.printf("%15d %15s ", averageTime, "na");
                } else {
                    System.out.printf("%15d %15.3f ", averageTime, (float) averageTime / prevTime[prevTimeLoc]);
                }
                //Update prevTime array
                prevTime[prevTimeLoc] = averageTime;
                prevTimeLoc++;
            }
            System.out.printf("%15.3f \n", N * Math.log(N) / ((N / 2) * Math.log(N / 2)));
            lastTime = getCpuTime() - lastTime;
        }

        //Testing radix sort times

        //Testing different digits
        for (int digit = 1; digit <= 5; digit++) {
            if (digit == 5) {
                digit = 8;
            }
            System.out.printf("Radix - %d \n", digit);
            //Printing first value of row
            System.out.printf("%31s %31s %31s %31s\n", "k=6", "k=12", "k=24", "k=48");
            System.out.printf("%31s %15s %15s %15s %15s %15s %15s %15s %15s\n", "Time", "Doubling Ratio", "Predicted",
                    "Time", "Doubling Ratio", "Predicted", "Time", "Doubling Ratio", "Predicted");
            System.out.printf("%64s %46s %47s\n", "Doubling", "Doubling", "Doubling", "Doubling");
            System.out.printf("%15s %47s %47s %47s\n", "N", "Ratio", "Ratio", "Ratio", "Ratio");
            lastTime = 0;
            //Looping through N
            for (N = 1; lastTime < maxTime; N = N * 2) {
                System.out.printf("%15d ", N);
                prevTimeLoc = 0;
                lastTime = getCpuTime();
                //Looping through values of k
                for (k = 6; k <= 48; k = k * 2) {
                    if (k > digit) {
                        averageTime = 0;
                        //Running a number of times for each combination of N and k
                        for (x = 0; x < timesToRun; x++) {
                            array = generateTestList(N, k, 1, (int) Math.pow(2, (8 * digit)));
                            timeBefore = getCpuTime();
                            radixSort(array, N, k, digit);
                            timeAfter = getCpuTime();
                            averageTime = averageTime + timeAfter - timeBefore;
                        }
                        //Dividing accumulated times by the times the sort ran to get the average time
                        averageTime = averageTime / timesToRun;
                        //Calculating place in array of previous time values to get average time from last value of N
                        //And then replace it with the new value
                        if (N == 1) {
                            System.out.printf("%15d %15s ", averageTime, "na");
                        } else {
                            System.out.printf("%15d %15.3f ", averageTime, (float) averageTime / prevTime[prevTimeLoc]);
                        }
                        //Update prevTime array
                        prevTime[prevTimeLoc] = averageTime;
                        prevTimeLoc++;
                    } else {
                        System.out.printf("%15s %15s", "na", "na");
                    }
                }
                if (N != 1) {
                    System.out.printf("%15d \n", (N * k) / ((N / 2) * (k / 2)));
                } else {
                    System.out.printf("%15s \n", "na");
                }
                lastTime = getCpuTime() - lastTime;
            }
        }
    }

    public static void main(String[] args) {
        // write your code here
        if (verificationTesting()) {
            System.out.printf("All tests passed!\n");
        }
        runTimeTests();
    }
}
