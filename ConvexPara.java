import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ConvexPara {
    int n, seed, antThreads;
    int minIndex, maxIndex, MAX_X, MAX_Y, MIN_X;
    int[] y, x;
    NPunkter17 nPunkter17;
    IntList intList;


    public ConvexPara(int n, int seed, IntList lst, int antThreads) {
        this.n = n;
        this.seed = seed;
        this.intList = lst;
        this.x = new int[n];
        this.y = new int[n];
        nPunkter17 = new NPunkter17(n, seed);
        nPunkter17.fyllArrayer(x, y);
        this.antThreads = antThreads;
        // finnLinje();
        // try {
        //     findThreadLevel();
        // } catch (InterruptedException | BrokenBarrierException e) {
        //     e.printStackTrace();
        // }
    }


    public int distanceFromLine(int max, int min, int node) {
        int a = y[min] - y[max];
        int b = x[max] - x[min];
        return a * x[node] + b * y[node] + y[max] * x[min] - y[min] * x[max];
    }


    public void finnLinje() {
        MAX_X = MIN_X = x[0];
        MAX_Y = y[0];
        maxIndex = minIndex = 0;
    
        for (int i = 1; i < n; i++) {
            if (x[i] > MAX_X) {
                MAX_X = x[maxIndex = i];
            } if (x[i] < MIN_X) {
                MIN_X = x[minIndex = i];
            }
            if (y[i] < MAX_Y) {
                MAX_Y = y[i];
            }
        }

        try {
            findThreadLevel();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    
    // The findThreadLevel method creates two threads, one for each half of the convex hull.
    public void findThreadLevel() throws InterruptedException, BrokenBarrierException {
        ArrayList<Integer> leftSide = new ArrayList<>();
        ArrayList<Integer> rightSide = new ArrayList<>();
        int tempOver = 0;
        int tempUnder = 0;
        int largestNegative = 0;
        int largestPositive = 0;
        for (int i = 0; i < n; i++) {
            int temp = distanceFromLine(minIndex, maxIndex, i);
            if (temp > 0) leftSide.add(i);
            if (temp < 0) rightSide.add(i);
            
            if (temp > largestNegative) {
                largestNegative = temp;
                tempUnder = i;
            }
            if (temp < largestPositive) {
                largestPositive = temp;
                tempOver = i;
            }
        }
       
        ArrayList<Integer> top = new ArrayList<>();
        ArrayList<Integer> bottom = new ArrayList<>();
        CyclicBarrier cy = new CyclicBarrier(2);
        int level = 0;
        top.add(minIndex);
       
        Thread bottomThread = new Thread( new Worker(leftSide, minIndex, maxIndex,level, y, x, antThreads, cy, top));
        Thread topThread = new Thread(new Worker(rightSide, maxIndex, minIndex, level, y, x, antThreads, cy, bottom));
        bottomThread.start();
        bottomThread.join();
        topThread.start();
        topThread.join();
        top.add(maxIndex);
        bottom.add(minIndex);

        for (Integer integer : top) intList.add(integer);

        for (Integer integer : bottom) intList.add(integer);

    }

}
