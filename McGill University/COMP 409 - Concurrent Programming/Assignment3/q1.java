import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Stack;

public class q1 {
  public static int numThreads, stringLength;
  //Thread safe lock based stack for threads to properly determine what part of the array to operate on
  public static Stack<Integer> id;
  //Global arrays to hold each thread's triple
  public static boolean[] ok;
  public static int[] f;
  public static int[] m;
  //If this is set to true then average execution times of 1 thread to 52 threads will be calculated
  private static boolean runAllTests = false;

  public static void main(String[] args) {
    long randomSeed;

    //Handling and parsing the input parameters into their corresponding variables.
    if (args.length < 2 || args.length > 3) {
      System.out.println("Please provide arguments as follows: n t s");
      System.out.println("n: String length\nt: Number of Threads\ns: (Optional) Seed for the String Generation");
      return;
    }
    try {
      switch (args.length) {
        case 2:
          randomSeed = System.currentTimeMillis();
          break;
        case 3:
          randomSeed = Long.parseLong(args[2]);
          break;
        default:
          throw new NumberFormatException("No random seed available");
      }
      stringLength = Integer.parseInt(args[0]);
      numThreads = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println("Please ensure that the parameters are of the form:\nn: int\nt: int\ns: long");
      e.printStackTrace();
      return;
    }

    //Construct the Bracket using the parameters input
    Bracket.construct(stringLength, randomSeed);

    //Sequential verification
    long startTimeSeq = System.currentTimeMillis();
    boolean truePositiveResults = Bracket.verify();
    long endTimeSeq = System.currentTimeMillis();

    //Execute according to input numThreads
    if (!runAllTests) {
      //Warm-starting because cold-starting was resulting in low precision (high standard deviation)
      for (int j = -1; j < 1; j++) {
        long startTimeThreads = System.currentTimeMillis();
        //Setting up the string division boundaries
        id = new Stack<>();
        for (int i = 0; i < numThreads; i++) {
          id.push(i);
        }

        //Setting up the data for each thread to access
        ok = new boolean[numThreads];
        f = new int[numThreads];
        m = new int[numThreads];

        //Create the thread factory
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        //Run all Threads
        for (int i = 0; i < numThreads; i++) {
          executor.submit(new BracketMatcher());
        }

        //Shutdown ThreadPoolExecutor
        executor.shutdown();
        try {
          while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            System.out.println("Not yet. Still waiting for termination");
          }
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }

        //Sequentially combine the threads' results
        for (int i = 1; i < numThreads; i++) {
          boolean ok1 = ok[i-1];
          boolean ok2 = ok[i];
          int f1 = f[i-1];
          int f2 = f[i];
          int m1 = m[i-1];
          int m2 = m[i];
          ok[i] = (ok1 && ok2) || ((f1 + f2 == 0) && (m1 >= 0) && (f1 + m2 >= 0));
          f[i] = f1 + f2;
          m[i] = Math.min(m1, f1 + m2);
        }

        long endTimeThreads = System.currentTimeMillis();
        //Don't print for the first loop iteration (warm-starting)
        if (j != -1) {
          System.out.println("Sequential Time: " + (endTimeSeq - startTimeSeq) + "\nMulti-Threaded Time: " + (endTimeThreads - startTimeThreads) + "\n" + ok[numThreads - 1] + " " + truePositiveResults + "\n");
        }
      }
    }

    //FOR RUNNING THREADS 1 - 52 AND GETTING AVERAGE RUN TIMES
    else {
      long[] averages = new long[52];
      for (int k = 1; k <= 52; k++) {
        numThreads = k;
        long[] average = new long[5];
        for (int j = -1; j < 5; j++) {
          long startTimeThreads = System.currentTimeMillis();
          //Setting up the string division boundaries
          id = new Stack<>();
          for (int i = 0; i < numThreads; i++) {
            id.push(i);
          }

          //Setting up the data for each thread to access
          ok = new boolean[numThreads];
          f = new int[numThreads];
          m = new int[numThreads];

          //Create the thread factory
          ExecutorService executor = Executors.newFixedThreadPool(numThreads);

          //Run all Threads
          for (int i = 0; i < numThreads; i++) {
            executor.submit(new BracketMatcher());
          }

          //Shutdown ThreadPoolExecutor
          executor.shutdown();
          try {
            while (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
              System.out.println("Not yet. Still waiting for termination");
            }
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }

          //Sequentially combine the threads' results
          for (int i = 1; i < numThreads; i++) {
            boolean ok1 = ok[i-1];
            boolean ok2 = ok[i];
            int f1 = f[i-1];
            int f2 = f[i];
            int m1 = m[i-1];
            int m2 = m[i];
            ok[i] = (ok1 && ok2) || ((f1 + f2 == 0) && (m1 >= 0) && (f1 + m2 >= 0));
            f[i] = f1 + f2;
            m[i] = Math.min(m1, f1 + m2);
          }

          long endTimeThreads = System.currentTimeMillis();
          //Warm-starting
          if (j != -1) {
            average[j] = endTimeThreads - startTimeThreads;
            System.out.println("NumThreads-" + numThreads + "Trial " + j + ":");
            System.out.println("main-f: " + f[numThreads - 1] + "\nmain-m: " + m[numThreads - 1] + "\nmain-ok: " + ok[numThreads - 1]);
            System.out.println("Multi-Threaded Time: " + average[j] + "\n" + ok[numThreads - 1] + " " + truePositiveResults + "\n");
          }
        }
        averages[k-1] = (average[0] + average[1] + average[2] + average[3] + average[4]) / 5;
      }
      System.out.println("Sequential Time: " + (endTimeSeq - startTimeSeq));
      System.out.println("Averages:");
      for (int i = 0; i < 52; i++) {
        System.out.println("NumThreads-" + (i + 1) + ": " + averages[i]);
      }
    }
  }
}

class BracketMatcher implements Runnable {
  public void run() {
    //Determine Heuristic Thread ID from Thread Safe Stack
    int id;
    id = q1.id.pop();

    //Determine indices over which to access Bracket.array (This is distributing all of the "extra" indices evenly amongst the threads)
    int startIndex, endIndex;
    if (id < q1.stringLength % q1.numThreads) {
      startIndex = id * (q1.stringLength / q1.numThreads) + id;
      endIndex = (id + 1) * (q1.stringLength / q1.numThreads) - 1 + (id + 1);
    }
    else {
      startIndex = id * (q1.stringLength / q1.numThreads) + (q1.stringLength % q1.numThreads);
      endIndex = (id + 1) * (q1.stringLength / q1.numThreads) - 1 + (q1.stringLength % q1.numThreads);
    }

    //Initialize current thread's triple
    q1.ok[id] = false;
    q1.f[id] = 0;
    q1.m[id] = 1;

    //Sequential validation on current thread's assigned portion of the thread
    for (int i = startIndex; i <= endIndex; i++) {
      if (Bracket.array[i]=='[')
        q1.f[id]++;
      else if (Bracket.array[i]==']') {
          q1.f[id]--;
          if (q1.f[id] < q1.m[id])
            q1.m[id] = q1.f[id];
      }
    }
    q1.ok[id] = (q1.f[id] == 0) && (q1.m[id] >= 0);
  }
}
