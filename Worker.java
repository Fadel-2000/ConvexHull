import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {
        int p1, level, p3, antThreads, Threadlevel;
        int[] x, y;
        ArrayList<Integer> upOrDown, hullPoints;
        CyclicBarrier barrier;

        public Worker(ArrayList<Integer> upOrDown, int p1, int p3, int Threadlevel, int[] y,
                int[] x, int antThreads, CyclicBarrier barrier, ArrayList<Integer> hullPoints) {
            this.Threadlevel = Threadlevel;
            this.p1 = p1;
            this.p3 = p3;
            this.antThreads = antThreads;
            this.upOrDown = upOrDown;
            this.barrier = barrier;
            this.x = x;
            this.y = y;
            this.hullPoints = hullPoints;
            level =  antThreads; // (Math.log(antThreads) / Math.log(2)) + 1;
        }


        public int distanceFromLine(int max, int min, int node) {
            int a = y[min] - y[max];
            int b = x[max] - x[min];
            return a * x[node] + b * y[node] + y[max] * x[min] - y[min] * x[max]; 
        }

        @Override
        public void run() { 
            int highestIndex = 0;
            //int upOrDownSize = upOrDown.size();
            if (level > Threadlevel) { 
                int oldValue = 0;
                for (int i : upOrDown) {
                    int current = distanceFromLine(p1, p3, i);
                    if (current >= oldValue) {
                        oldValue = current;
                        highestIndex = i;
                    }
                 }
                ArrayList<Integer> conLeft = new ArrayList<>();
                ArrayList<Integer> conRight = new ArrayList<>();
                Threadlevel++;
                int rightLevel = Threadlevel;
                if (oldValue > 0) {           
                    Thread th1 = new Thread(
                    new Worker(upOrDown, highestIndex, p3, rightLevel, y, x, antThreads, barrier, conRight));
                    conRight.add(highestIndex);
                    Thread th = new Thread(
                    new Worker(upOrDown, p1, highestIndex, Threadlevel, y, x, antThreads, barrier, conLeft));
                           
                    th.start();
                    try {
                        th.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    th1.start();
                    try {
                        th1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    hullPoints.addAll(conLeft);
                    hullPoints.addAll(conRight);

                }
            }
            else {
                

                int oldValue = 0;
                highestIndex = 0;
                for (int i : upOrDown) {
                    int current = distanceFromLine(p1, p3, i);
                    if (current >= oldValue) {
                        oldValue = current;
                        highestIndex = i;
                    }

                }
                int temp = p3;

                if (oldValue > 0) {
                    p3 = highestIndex;
                    // run();
                    hullPoints.add(highestIndex);
                    p3 = temp;
                    p1 = highestIndex;
                    // run();
                }
        }
    }
}
