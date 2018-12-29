import java.util.*;
class SlidingMaxTwoPointer {
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
      if(k==1) return nums;

      int size = N-k+1;
      int[] res = new int[size];
      int maxIndex = findMax(nums,0,k-1);
      res[0] = nums[maxIndex];
      for(int i=1;i<size;i++){
          if(i-1!=maxIndex){
              if(nums[i+k-1]<res[i-1]) res[i] = res[i-1];
              else{ maxIndex = i+k-1; res[i] = nums[maxIndex];}
          }
          else{ maxIndex = findMax(nums,i,i+k-1); res[i] = nums[maxIndex];}
      }
      return res;
  }

  private static int findMax(int[]data,int left,int right){
      int res = left;
      int max = data[left];
      for(int i=left+1;i<=right;i++){
          if(data[i]>=max){
              res = i;
              max = data[i];
          }
      }
      return res;
  }
}
