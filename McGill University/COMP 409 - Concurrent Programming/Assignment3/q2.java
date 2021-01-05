import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Exchanger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Random;

public class q2 {
  public static EliminationBackoffStack<String> stack;
  public static int randLimit, numOperations;

  private static int totalPushCount = 0;
  private static int totalSuccessfulPopCount = 0;

  public synchronized static void incTotals(int pushCount, int successfulPopCount) {
    totalPushCount += pushCount;
    totalSuccessfulPopCount += successfulPopCount;
  }

  public static void main(String[] args) {
    int numThreads, timeoutFactor, elimArraySize;
    //Handling and parsing the input parameters into their corresponding variables.
    if (args.length != 5) {
      System.out.println("Please provide arguments as follows: p d n t e");
      System.out.println("p(> 1): Number of Threads\nd(>= 0): Upper Limit on Random Delay\nn(> 0): Total Operations for each Thread\nt(>= 0): Timeout Factor\ne(> 0): Elemination Array Size");
      return;
    }
    try {
      numThreads = Integer.parseInt(args[0]);
      randLimit = Integer.parseInt(args[1]);
      numOperations = Integer.parseInt(args[2]);
      timeoutFactor = Integer.parseInt(args[3]);
      elimArraySize = Integer.parseInt(args[4]);
      if (!(numThreads > 1 && randLimit >=0 && numOperations > 0 && timeoutFactor >=0 && elimArraySize > 0))
        throw new NumberFormatException("Incorrect parameter bounds");
    } catch (NumberFormatException e) {
      System.out.println("Please provide arguments as follows: p d n t e");
      System.out.println("p(> 1): Number of Threads\nd(>= 0): Upper Limit on Random Delay\nn(> 0): Total Operations for each Thread\nt(>= 0): Timeout Factor\ne(> 0): Elemination Array Size");
      e.printStackTrace();
      return;
    }

    stack = new EliminationBackoffStack(elimArraySize, timeoutFactor);

    long startTime = System.currentTimeMillis();
    //Create the thread factory
    ExecutorService executor = Executors.newFixedThreadPool(numThreads);

    //Run all Threads
    for (int i = 0; i < numThreads; i++) {
      executor.submit(new PushPopThread());
    }

    //Shutdown ThreadPoolExecutor
    executor.shutdown();
    try {
      while (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
        System.out.println("Not yet. Still waiting for termination");
      }
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }

    long endTime = System.currentTimeMillis();
    System.out.println("ExecutionTime: " + (endTime - startTime));

    int finalStackSize = 0;
    try {
      while (true) {
        stack.pop();
        finalStackSize++;
      }
    } catch (EmptyException e) {}
    System.out.println(totalPushCount + " " + totalSuccessfulPopCount + " " + finalStackSize);
  }
}

class TimeoutException extends Exception {
  public TimeoutException() {
    super("Timeout");
  }
}

class EmptyException extends Exception {
  public EmptyException() {
    super("Empty");
  }
}

class Backoff {
  final int minDelay, maxDelay;
  int limit;
  final Random random;

  public Backoff(int min, int max) {
    minDelay = min;
    maxDelay = max;
    limit = minDelay;
    random = new Random();
  }

  public void backoff() throws InterruptedException {
    int delay = random.nextInt(limit);
    limit = Math.min(maxDelay, 2*limit);
    Thread.sleep(delay);
  }
}

class LockFreeStack<T> {
  AtomicReference<Node> top = new AtomicReference<Node>(null);
  static final int MIN_DELAY = 5;
  static final int MAX_DELAY = 10;
  Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);

  protected class Node {
    public T value;
    public Node next;
    public Node(T value) {
      value = value;
      next = null;
    }
  }

  protected boolean tryPush(Node node) {
    Node oldTop = top.get();
    node.next = oldTop;
    return (top.compareAndSet(oldTop, node));
  }

  public void push(T value) {
    Node node = new Node(value);
    while (true) {
      if (tryPush(node)) {
        return;
      } else try {
        backoff.backoff();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  protected Node tryPop() throws EmptyException {
    Node oldTop = top.get();
    if (oldTop == null) {
      throw new EmptyException();
    }
    Node newTop = oldTop.next;
    if (top.compareAndSet(oldTop, newTop)) {
      return oldTop;
    } else {
      return null;
    }
  }

  public T pop() throws EmptyException {
    while (true) {
      Node returnNode = tryPop();
      if (returnNode != null) {
        return returnNode.value;
      } else try {
        backoff.backoff();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

class EliminationArray<T> {
  private static int duration;
  Exchanger<T>[] exchanger;
  Random random;

  public EliminationArray(int timeoutFactor, int capacity) {
    duration = timeoutFactor;
    exchanger = (Exchanger<T>[]) new Exchanger[capacity];
    for (int i = 0; i < capacity; i++) {
      exchanger[i] = new Exchanger<T>();
    }
    random = new Random();
  }

  public T visit(T value, int range) throws TimeoutException {
    int slot = random.nextInt(range);
    try {
      return (exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS));
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      return null;
    }
  }
}

class EliminationBackoffStack<T> extends LockFreeStack<T> {
  private static class RangePolicy {
    int maxRange;
    int currentRange = 1;

    RangePolicy(int maxRange) {
      this.maxRange = maxRange;
    }

    public void recordEliminationSuccess() {
      if (currentRange < maxRange) {
        currentRange++;
      }
    }

    public void recordEliminationTimeout() {
      if (currentRange > 1) {
        currentRange--;
      }
    }

    public int getRange() {
      return currentRange;
    }
  }

  static int capacity;
  EliminationArray<T> eliminationArray;
  static ThreadLocal<RangePolicy> policy;

  public EliminationBackoffStack(int capacity, int duration) {
    super();
    this.capacity = capacity;
    eliminationArray = new EliminationArray<T>(capacity, duration);
    policy = new ThreadLocal<RangePolicy>() {
      protected synchronized RangePolicy initialValue() {
        return new RangePolicy(capacity);
      }
    };
  }

  public void push(T value) {
    RangePolicy rangePolicy = policy.get();
    Node node = new Node(value);
    while (true) {
      if (tryPush(node)) {
        return;
      } else try {
        T otherValue = eliminationArray.visit(value, rangePolicy.getRange());
        if (otherValue == null) {
          rangePolicy.recordEliminationSuccess();
          return;
        }
      } catch (TimeoutException ex) {
        rangePolicy.recordEliminationTimeout();
      }
    }
  }

  public T pop() throws EmptyException {
    RangePolicy rangePolicy = policy.get();
    while (true) {
      Node returnNode = tryPop();
      if (returnNode != null) {
        return returnNode.value;
      } else try {
        T otherValue = eliminationArray.visit(null, rangePolicy.getRange());
        if (otherValue != null) {
          rangePolicy.recordEliminationSuccess();
          return otherValue;
        }
      } catch (TimeoutException ex) {
        rangePolicy.recordEliminationTimeout();
      }
    }
  }
}

class PushPopThread implements Runnable {
  public void run() {
    String[] lastTwenty = new String[20];
    int index = 0;
    int count = 0;
    int pushCount = 0;
    int successfulPopCount = 0;
    long threadID = Thread.currentThread().getId();
    int newNodeID = 0;
    ThreadLocalRandom current = ThreadLocalRandom.current();

    for (int i = 0; i < q2.numOperations; i++) {
      if (current.nextBoolean()) {
        //System.out.println("PushOperation");
        if (count > 0 && current.nextBoolean()) {
          q2.stack.push(lastTwenty[index]);
          index = (index - 1) % 20;
          count--;
        } else {
          q2.stack.push(threadID + "-" + newNodeID);
          newNodeID++;
        }
        pushCount++;
      } else {
        //System.out.println("PopOperation");
        try {
          lastTwenty[index] = q2.stack.pop();
          successfulPopCount++;
        } catch (EmptyException e) {
          //System.out.println(e.toString());
        }
        index = (index + 1) % 20;
        if (count < 20)
          count++;
      }
      int rand = current.nextInt(q2.randLimit);
      //System.out.println("Sleeping for " + rand + " milliseconds.");
      try {
        Thread.sleep(rand);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    q2.incTotals(pushCount, successfulPopCount);
  }
}
