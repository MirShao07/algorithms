import java.util.*;
public class SlidingMaxDeque {
  static int N;
  static int K;
  public static void main(String[] args){
    N = 20;
    int[]data = new int[N];
    Random rand = new Random();
    K = rand.nextInt(N)+1;
    for (int i=0;i<N;i++) {
      data[i] = rand.nextInt(N) * (rand.nextBoolean() ? -1 : 1);
    }
    System.out.println("Data: "+Arrays.toString(data));
    System.out.println();
    System.out.println("N = "+N+"   K = "+K);
    System.out.println();
    System.out.println("Max:  "+Arrays.toString(maxSlidingWindow(data,K)));
  }
  private static int[] maxSlidingWindow(int[] nums, int k) {
      int N = nums.length;
      if(N==0) return new int[0];
      Deque<Integer> dq = new ArrayDeque<>();
      int[]res = new int[N-k+1];
      for(int i=0;i<k-1;i++){
          while(!dq.isEmpty()&&nums[i]>=nums[dq.peekLast()]) dq.removeLast();
          dq.offer(i);
      }
      for(int i=k-1;i<N;i++){
          while(!dq.isEmpty()&&nums[i]>=nums[dq.peekLast()]) dq.removeLast();
          dq.offer(i);
          if((i-k+1)>dq.peekFirst()) dq.removeFirst();
          res[i-k+1] = nums[dq.peekFirst()];
      }
      return res;
  }
}
