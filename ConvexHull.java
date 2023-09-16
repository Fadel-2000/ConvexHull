import java.util.ArrayList;

public class ConvexHull {
    int n, seed;
    int MAX_X, MAX_Y, MIN_X;
    int[] y, x;
    NPunkter17 nPunkter17;
    IntList intList;


    // The constructor of the ConvexHull class takes as input the number of points n, a seed value for generating the points,
    // and an IntList object to hold the indices of the points in the convex hull.
    // The constructor generates n random points using an external class NPunkter17 and fills the x and y arrays with their coordinates.
    public ConvexHull(int n, int seed, IntList intList) {
        this.n = n;
        this.seed = seed;
        nPunkter17 = new NPunkter17(n, seed);
        x = new int[n];
        y = new int[n];
        nPunkter17.fyllArrayer(x, y);
        this.intList = intList;
    }


    public void finnLinje() {
        int maxIndex = 0, minIndex = 0;
        for (int i = 1; i < n; i++) {
            if (x[i] > x[maxIndex]) maxIndex = i;
            if (x[i] < x[minIndex]) minIndex = i;
            if (y[i] < y[MAX_Y]) MAX_Y = y[i];
        }
        int a = y[minIndex] - y[maxIndex];
        int b = x[maxIndex] - x[minIndex];
        int c = y[maxIndex] * x[minIndex] - y[minIndex] * x[maxIndex];

        int index = 0;
        int mostNegative = 0;
        for (int i = 0; i < n; i++) {
            int temp = ((a * x[i] + b * y[i] + c));
            if (temp >= mostNegative) {
                mostNegative = temp;
                index = i;
            }
        }
        finnPunkt(minIndex, index, maxIndex, intList);
    }

    // The method distanceFromLine() calculates the signed distance between a point and a line passing through two other points.
    public int distanceFromLine(int max, int min, int node) {
        int a = y[min] - y[max];
        int b = x[max] - x[min];
        return a * x[node] + b * y[node] + y[max] * x[min] - y[min] * x[max];
    }

    
    // The main method is finnPunkt(), which takes three indices representing three points
    // (left, right, and last) and an IntList object that will contain the indices of the points in the convex hull.
    // The method first divides the set of points into two groups, 
    // one to the left and one to the right of the line passing through the left and right points.
    // Then, it recursively finds the upper and lower parts of the convex hull in each group
    // and adds the corresponding points to the IntList object.
    public void finnPunkt(int leftNode, int lastNode, int rightNode, IntList points) {
        ArrayList<Integer> venstreSide = new ArrayList<>();
        ArrayList<Integer> hoyreSide = new ArrayList<>();
        int tempOver = 0, tempUnder = 0;
        int mostNegative = 0, largestPositive = 0;
        for (int i = 0; i < n; i++) {
            int temp = distanceFromLine(rightNode, leftNode, i);
            if (temp > 0) venstreSide.add(i);
            else if (temp < 0) hoyreSide.add(i);
            if (temp < mostNegative) {
                mostNegative = temp;
                tempUnder = i;
            } else if (temp > largestPositive) {
                largestPositive = temp;
                tempOver = i;
            }
        }
        intList.add(rightNode);
        findUpperHull(rightNode, tempOver, venstreSide);
        intList.add(tempOver);
        findLowerHull(tempOver, leftNode, venstreSide);
        intList.add(leftNode);
        findUpperHull(leftNode, tempUnder, hoyreSide);
        intList.add(tempUnder);
        findLowerHull(tempUnder, rightNode, hoyreSide);
    }


    // The method findUpperHull() finds the upper part of the convex hull between two points (max and min)
    // in a given group of points (represented as an ArrayList<Integer>). 
    // It does so by recursively finding the point that is farthest away from the line passing through the two points, 
    // until no point is above the line.
    public void findUpperHull(int max, int min, ArrayList<Integer> liste) {
        int oldValue = Integer.MIN_VALUE;
        int highestIndex = 0;
        ArrayList<Integer> venstreSide = new ArrayList<>();
        for (int i : liste) {
            int temp = distanceFromLine(max, min, i);
            if (temp > 0) venstreSide.add(i);
            if (temp >= oldValue) {
                oldValue = temp;
                highestIndex = i;
            } 
        }
    
        if (oldValue > 0) {
            findUpperHull(max, highestIndex, venstreSide);
        }
        intList.add(highestIndex);
        if (oldValue > 0) {
            findUpperHull(highestIndex, min, venstreSide);
        }
    }

    // The method findLowerHull() finds the lower part of the convex hull between two points (max and min)
    // in a given group of points (represented as an ArrayList<Integer>).
    // It does so by recursively finding the point that is farthest away from the line passing through the two points,
    // until no point is below the line.
    public void findLowerHull(int max, int min, ArrayList<Integer> liste) {
        ArrayList<Integer> hoyreSide = new ArrayList<>();
        int lowestValue = (int) Double.POSITIVE_INFINITY;
        int lowestIndex = 0;
        // int size = liste.size();
        for (int i : liste) {
            int temp = distanceFromLine(min, max, i);
            if (temp < 0) hoyreSide.add(i);
            if (temp <= lowestValue) {
                lowestValue = temp;
                lowestIndex = i;
            }
        }
    
        if (lowestValue < 0) {
            findLowerHull(max, lowestIndex,hoyreSide);
            intList.add(lowestIndex);
            findLowerHull(lowestIndex, min, hoyreSide);
        }
    }

}
