import java.util.Arrays;
// import java.util.concurrent.BrokenBarrierException;

public class Main {
    public static void main(String[] args) { 


        int n = Integer.parseInt(args[0]);
        int seed = Integer.parseInt(args[1]);
        int maksThreads = Runtime.getRuntime().availableProcessors();

        long [] medianSek = new long[7];
 
        for (int i = 0; i < 7; i++) {
            IntList intList = new IntList(n);
            ConvexHull convex = new ConvexHull(n, seed, intList);
            Long start = System.nanoTime();
            convex.finnLinje();
            Long end = System.nanoTime() - start;
            medianSek[i] = end; 
         }


        Arrays.sort(medianSek);
        System.out.println("\n" +"Median of sequential convexHull: " + medianSek[3] / 1_000_000_000.00 + " Sek for " + n + " n");
         

        long [] medianPara = new long[7];
        Oblig4PrecodePara paraCode = null;
        for (int i = 0; i < 7; i++) {
            if(n <= 0){
                IntList intList = new IntList(n);
                ConvexHull convex = new ConvexHull(n, seed, intList);
                Long paraStart = System.nanoTime();
                convex.finnLinje();
                Long paraEnd = System.nanoTime() - paraStart;
                medianPara[i] = paraEnd;
                continue;
             }

            
            IntList paraList = new IntList(n);
            // Long paraStart = System.nanoTime();
            ConvexPara para = new ConvexPara(n, seed, paraList, maksThreads);
            Long paraStart = System.nanoTime();
            para.finnLinje();
        //     try {
        //     para.findThreadLevel();
        // } catch (InterruptedException | BrokenBarrierException e) {
        //     e.printStackTrace();
        // }
            Long paraEnd = System.nanoTime() - paraStart;
            medianPara[i] = paraEnd;
            paraCode = new Oblig4PrecodePara(para, paraList);
        
        }

        Arrays.sort(medianPara);
        System.out.println("Median of Parallel convexHull: " + (medianPara[3] / 1_000_000_000.00) + " Sek for " + n + " n");
        System.out.println("SPEEDUP for ConvexH = "+ (medianSek[3] / 1_000_000_000.00) / (medianPara[3] / 1_000_000_000.00) + "\n");
        
        paraCode.writeHullPoints();
        paraCode.drawGraph();
     }
}
