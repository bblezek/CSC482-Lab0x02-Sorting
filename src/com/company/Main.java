package com.company;

import java.lang.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class Main {

    //Generates lists of random characters with null bytes at the end
    public static String[] generateTestList(int N, int k, int minV, int maxV) {
        String[] outputArray = new String[N];
        Random rand = new Random();
        //New string is of length k+1 to make room for null byte
        char[] newString = new char[k+1];
        for (int outer = 0; outer < N; outer++) {
            for (int inner = 0; inner < k; inner++) {
                newString[inner] = (char) (minV + rand.nextInt(maxV - minV));
            }
            newString[k] = '\0';
            outputArray[outer] = String.valueOf(newString);
        }
        return outputArray;
    }

    //Function to test if the inner arrays of an array of arrays are sorted
    public static boolean isSorted(String[] array, int N) {
        for (int x = 0; x < N-1; x++) {
            //If element at x is greater than element at x+1, return false
                if (array[x].compareTo(array[x+1]) > 0) {
                    return false;
                }
            }
        //If elements are in order, return true
        return true;
    }

    //Simple function to print an array of strings
    public static void printArray(String[] array, int N) {
        //Iterates through array, printing each string
        for (int x = 0; x < N; x++) {
            System.out.printf("%s\t", array[x]);
        }
        System.out.printf("\n");
        return;
    }

    //Insertion sort function
    public static void insertionSort(String[] arrayToSort, int N) {
        int index, x;
        String temp;
            //Looping through each element of the array
            for (index = 1; index < N; index++) {
                x = index;
                temp = arrayToSort[index];
                //Traverses down list from inner
                //Tests whether temp is less than the next lowest element
                //If it is, move that element up
                while (x > 0 && temp.compareTo(arrayToSort[x-1]) <= 0) {
                    arrayToSort[x] = arrayToSort[x-1];
                    x--;
                }
                //When temp is no longer less than the next lowest element
                //Insert temp at x
                arrayToSort[x] = temp;
            }
        return;
    }

    //Recursive merge sort function
    public static String[] mergeSort(String[] arrayToSort, int low, int high) {
        int mid = (int) Math.ceil((high - low) / 2) + low;
        String[] sortedArray = new String[high-low];
        //If there's only 2 elements in the array
        if (high - low == 2) {
            //Put them in the appropriate order
            if (arrayToSort[low].compareTo(arrayToSort[low+1]) > 0) {
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
            String[] lowArray = mergeSort(arrayToSort, low, mid);
            String[] highArray = mergeSort(arrayToSort, mid, high);
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
                } else if (lowArray[lowIndex].compareTo(highArray[highIndex]) < 0) {
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

    //NOTE: high is the highest array index, NOT the number of elements in the array
    public static void quickSort(String[] arrayToSort, int low, int high) {
        String pivot, temp;
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
            h = high - 1;
            //Looping until we find where the pivot char should be placed
            while (!pivotLocFound) {
                //"Scanning" left until we find an element larger than the pivot
                while (l < high-1 && arrayToSort[l].compareTo(pivot) < 0) {
                    l++;
                }
                //Scanning right until we find an element smaller than the pivot
                while (h > low && arrayToSort[h].compareTo(pivot) > 0) {
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
            quickSort(arrayToSort, low, h);
            quickSort(arrayToSort, h + 1, high);
        }
        return;
    }


    public static void radixSort(String[] arrayToSort, int N, int k, int d) {
        int stringIndex, digit, arrIndex, num, y, newLoc;
        int[] digitArray = new int[256];
        String[] sortedArray = new String[N];
        //Iterate through each character
        for(stringIndex = k-1; stringIndex >= 0; stringIndex--){
            //Iterating through each digit for each outer array element
            for (digit = 1; digit <= d; digit++) {
                //Iterating through String array, examining stringIndex element of each String
                for (arrIndex = 0; arrIndex < N; arrIndex++) {
                    num = arrayToSort[arrIndex].charAt(stringIndex);
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
                for (arrIndex = N - 1; arrIndex >= 0; arrIndex--) {
                    //Get value from array, calculate integer value
                    num = arrayToSort[arrIndex].charAt(stringIndex);
                    //Calculate appropriate digit
                    num = num / (int) (Math.pow(2, 8 * (digit - 1)));
                    num = num % (int) (Math.pow(2, 8));
                    //Insert at new array according to index provided by digitArray
                    newLoc = digitArray[num] - 1;
                    sortedArray[newLoc] = arrayToSort[arrIndex];
                    //Decrementing index at that spot in the digit array
                    digitArray[num]--;
                }
                //Clear digit array for next round
                for (y = 0; y < 256; y++) {
                    digitArray[y] = 0;
                }
                //Copying output string to input string for next round
                //Changing original array so no return value
                for (arrIndex = 0; arrIndex < N; arrIndex++) {
                    arrayToSort[arrIndex] = sortedArray[arrIndex];
                }
            }
        }
        return;
    }

    public static boolean verificationTesting() {
        int N, k;
        String[] stringArray;
        //This loop is for visual verification
        for (N = 10; N < 51; N = N + 10) {
            for (k = 6; k < 48; k = k * 2) {
                //Testing insertion sort
                stringArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(stringArray, N);
                insertionSort(stringArray, N);
                if (isSorted(stringArray, N)) {
                    System.out.printf("Insertion sorted list: \n");
                    printArray(stringArray, N);
                } else {
                    System.out.printf("Insertion sort failed!\n");
                    return false;
                }

                //Testing merge sort
                stringArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(stringArray, N);
                stringArray = mergeSort(stringArray, 0, N);
                if (isSorted(stringArray, N)) {
                    System.out.printf("Merge sorted list: \n");
                    printArray(stringArray, N);
                } else {
                    System.out.printf("Merge sort failed!\n");
                    return false;
                }

                //Testing quick sort
                stringArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(stringArray, N);
                quickSort(stringArray, 0, N);
                if (isSorted(stringArray, N)) {
                    System.out.printf("Quick sorted list: \n");
                    printArray(stringArray, N);
                } else {
                    System.out.printf("Quick sort failed!\n");
                    return false;
                }

                //Testing radix sort
                stringArray = generateTestList(N, k, 65, 90);
                System.out.printf("Input array: \n");
                printArray(stringArray, N);
                radixSort(stringArray, N, k, 1);
                if (isSorted(stringArray, N)) {
                    System.out.printf("Radix sorted list: \n");
                    printArray(stringArray, N);
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
                stringArray = generateTestList(N, k, 1, (int) Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                insertionSort(stringArray, N);
                System.out.printf("Sorting with insertion sort: \n");
                if (isSorted(stringArray, N)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Insertion sort failed!\n");
                    return false;
                }

                //Testing merge sort
                stringArray = generateTestList(N, k, 1, (int) Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                stringArray = mergeSort(stringArray, 0, N);
                System.out.printf("Sorting with merge sort: \n");
                if (isSorted(stringArray, N)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Merge sort failed!\n");
                    return false;
                }

                //Testing quick sort
                stringArray = generateTestList(N, k, 1, (int) Math.pow(2, 16));
                System.out.printf("Generating list of length %d, with key width %d\n",
                        N, k);
                quickSort(stringArray, 0, N);
                System.out.printf("Sorting with quick sort: \n");
                if (isSorted(stringArray, N)) {
                    System.out.printf("Successfully sorted!\n");
                } else {
                    System.out.printf("Quick sort failed!\n");
                    return false;
                }

                //Testing radix sort
                stringArray = generateTestList(N, k, 1, (int) Math.pow(2, 24));
                System.out.printf("Generating list of length %d, with key width %d \n",
                        N, k);
                radixSort(stringArray, N, k, 3);
                System.out.printf("Sorting with radix sort: \n");
                if (isSorted(stringArray, N)) {
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
        String[] array;

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
                    insertionSort(array, N);
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
                            System.out.printf("%15d %15s %15s", averageTime, "na", "na");
                        } else {
                            System.out.printf("%15d %15.3f %15d", averageTime,
                                    (float) averageTime / prevTime[prevTimeLoc], ((N * k) / ((N / 2) * k)));
                        }
                        //Update prevTime array
                        prevTime[prevTimeLoc] = averageTime;
                        prevTimeLoc++;
                    } else {
                        System.out.printf("%15s %15s", "na", "na");
                    }
                }
                System.out.printf("\n");


                lastTime = getCpuTime() - lastTime;
            }
        }
    }


    public static void main(String[] args) {
        // write your code here
        if (verificationTesting()) {
            System.out.printf("All tests passed!\n");
        }
        //runTimeTests();
    }
}
