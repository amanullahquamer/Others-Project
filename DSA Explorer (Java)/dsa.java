import java.util.*;

public class DSAProgram{
    private static Scanner scanner = new Scanner(System.in);

   public static void main(String[] args) {
        try {
            while (true) {
                displayOptions("DSA topic", "Sorting, Searching, Graph Algorithms, Exit");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("Exit")) {
                    System.out.println("Exiting program...");
                    break;
                }

                switch (choice.toLowerCase()) {
                    case "sorting":
                        exploreSorting();
                        break;
                    case "searching":
                        exploreSearching();
                        break;
                    case "graph algorithms":
                        exploreGraphAlgorithms();
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void exploreSorting() {
        displayOptions("sorting algorithm", "Bubble Sort, Merge Sort, Quick Sort, Selection Sort, Insertion Sort");
        String sortingAlgorithm = scanner.nextLine();
        int[] array = getIntegersFromUser(10);  // Get 10 integers from the user

        switch (sortingAlgorithm.toLowerCase()) {
            case "bubble sort":
                bubbleSort(array);
                break;
            case "merge sort":
                mergeSort(array, 0, array.length - 1);
                break;
            case "quick sort":
                quickSort(array, 0, array.length - 1);
                break;
            case "selection sort":
                selectionSort(array);
                break;
            case "insertion sort":
                insertionSort(array);
                break;
            default:
                System.out.println("Invalid sorting algorithm choice.");
        }
        printArray(array);
    }

    private static void exploreSearching() {
        displayOptions("search type", "Linear Search, Binary Search");
        String searchType = scanner.nextLine();
        System.out.println("Enter the number to search:");
        int number = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over
        int[] array = getIntegersFromUser(10);  // Get 10 integers from the user, assuming array is sorted for binary search

        int result = -1;
        switch (searchType.toLowerCase()) {
            case "linear search":
                result = linearSearch(array, number);
                break;
            case "binary search":
                result = binarySearch(array, 0, array.length - 1, number);
                break;
            default:
                System.out.println("Invalid search type.");
        }

        if (result == -1) {
            System.out.println("Number not found.");
        } else {
            System.out.println("Number found at index: " + result);
        }
    }

    private static void exploreGraphAlgorithms() {
        displayOptions("graph algorithm", "BFS, DFS");
        System.out.println("Enter the number of vertices:");
        int vertices = scanner.nextInt();
        int[][] graph = new int[vertices][vertices];
        System.out.println("Enter the adjacency matrix:");

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                graph[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Enter the starting vertex:");
        int startVertex = scanner.nextInt();

        scanner.nextLine();  // Consume newline left-over
        switch (scanner.nextLine().toLowerCase()) {
            case "bfs":
                bfs(graph, startVertex);
                break;
            case "dfs":
                boolean[] visited = new boolean[vertices];
                dfs(graph, visited, startVertex);
                break;
            default:
                System.out.println("Invalid graph algorithm choice.");
        }
    }

    // Implementation of Sorting Methods
    private static void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (array[j] > array[j + 1]) {
                    // swap temp and array[i]
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
    }

    private static void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            // Find the middle point
            int mid = (left + right) / 2;

            // Sort first and second halves
            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            // Merge the sorted halves
            merge(array, left, mid, right);
        }
    }

    private static void merge(int[] array, int left, int mid, int right) {
        // Find sizes of two subarrays to be merged
        int n1 = mid - left + 1;
        int n2 = right - mid;

        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        // /Copy data to temp arrays/
        for (int i = 0; i < n1; ++i)
            L[i] = array[left + i];
        for (int j = 0; j < n2; ++j)
            R[j] = array[mid + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
    }

    private static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            /* pi is partitioning index, arr[pi] is now at right place */
            int pi = partition(array, low, high);

            // Recursively sort elements before partition and after partition
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);  // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than the pivot
            if (array[j] < pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    private static void selectionSort(int[] array) {
        int n = array.length;

        // One by one move boundary of unsorted subarray
        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < n; j++)
                if (array[j] < array[min_idx])
                    min_idx = j;

            // Swap the found minimum element with the first element
            int temp = array[min_idx];
            array[min_idx] = array[i];
            array[i] = temp;
        }
    }

    private static void insertionSort(int[] array) {
        int n = array.length;
        for (int i = 1; i < n; ++i) {
            int key = array[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are greater than key, to one position ahead
               of their current position */
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
    }

    // Implementation of Searching Methods
    private static int linearSearch(int[] array, int x) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            if (array[i] == x)
                return i;
        }
        return -1;
    }

    private static int binarySearch(int[] array, int l, int r, int x) {
        if (r >= l) {
            int mid = l + (r - l) / 2;

            // If the element is present at the middle itself
            if (array[mid] == x)
                return mid;

            // If element is smaller than mid, then it can only be present in left subarray
            if (array[mid] > x)
                return binarySearch(array, l, mid - 1, x);

            // Else the element can only be present in right subarray
            return binarySearch(array, mid + 1, r, x);
        }

        // We reach here when element is not present in array
        return -1;
    }

    // Implementation of Graph Traversal Algorithms
    private static void bfs(int[][] graph, int start) {
        boolean visited[] = new boolean[graph.length];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        while (!queue.isEmpty()) {
            int s = queue.poll();
            System.out.print(s + " ");

            for (int i = 0; i < graph.length; i++) {
                if (graph[s][i] == 1 && !visited[i]) {
                    visited[i] = true;
                    queue.add(i);
                }
            }
        }
        System.out.println();
    }

    private static void dfs(int[][] graph, boolean[] visited, int v) {
        visited[v] = true;
        System.out.print(v + " ");

        for (int i = 0; i < graph[v].length; i++) {
            if (graph[v][i] == 1 && !visited[i]) {
                dfs(graph, visited, i);
            }
        }
    }

    private static int[] getIntegersFromUser(int count) {
        int[] array = new int[count];
        System.out.println("Enter " + count + " numbers:");
        for (int i = 0; i < count; i++) {
            array[i] = scanner.nextInt();
        }
        scanner.nextLine();  // Consume newline left-over
        return array;
    }

    private static void displayOptions(String type, String options) {
        System.out.println("Which " + type + " would you like to explore?");
        System.out.println("Options: " + options);
    }

    private static void printArray(int[] array) {
        System.out.println("Sorted array:");
        for (int i : array) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
