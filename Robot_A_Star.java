import java.util.*;

public class Robot_A_Star{
  static int N;
  static int clutter;
  static int[][]board;
  static Set<Integer> closedSet;
  static PriorityQueue<int[]> fringe;
  static int[]fScore;
  static int[]gScore;
  static int[]parent;

  public static void main(String[] args){
    // Generate random N*N board to simulate robot motion planning, clutter determines the number of obstacles.
    N = 20;
    clutter = 4;
    board = new int[N][N];
    Random rand = new Random();
    // 0: walkable, 1: obstacle. Place obstacles.
    for(int i=0;i<clutter*N;i++) board[rand.nextInt(N)][rand.nextInt(N)]=1;
    for(int[]row:board) System.out.println(Arrays.toString(row));

    // Start is somewhere in the upper left corner;
    int sr = rand.nextInt(N/4);
    int sc = rand.nextInt(N/4);
    // Robot must start from a 0 cell.
    while(board[sr][sc]==1){sr = rand.nextInt(N/4); sc = rand.nextInt(N/4);}

    // Target is somewhere in the lower right quadrant.
    int tr = rand.nextInt(N/2)+N/2;
    int tc = rand.nextInt(N/2)+N/2;
    // Target must be a 0 cell.
    while(board[tr][tc]==1){tr = rand.nextInt(N/2)+N/2; tc = rand.nextInt(N/2)+N/2;}
    System.out.println("start: "+sr+" "+sc);
    System.out.println("target: "+tr+" "+tc);

    // Set up for A*, assuming heuristic is monotonic (taxicab here).
    closedSet = new HashSet<>();
    fringe = new PriorityQueue<int[]>((int[]e1,int[]e2)->(e1[1]-e2[1]));
    fScore = new int[N*N];
    Arrays.fill(fScore,1000000);
    gScore = new int[N*N];
    Arrays.fill(gScore,1000000);
    parent = new int[N*N];
    Arrays.fill(parent,-1);

    // Start A* search!
    A_Star(sr,sc,tr,tc);
  }

  private static void A_Star(int sr,int sc,int tr,int tc){
    //Robot can only move in four cardinal directions.
    int[]dr = new int[]{0,0,1,-1};
    int[]dc = new int[]{1,-1,0,0};

    gScore[sr*N+sc] = 0;
    fScore[sr*N+sc] = hValue(sr,sc,tr,tc);
    fringe.offer(new int[]{sr*N+sc,fScore[sr*N+sc]});
    while(!fringe.isEmpty()){
      int[]cur = fringe.poll();
      // As we don't perform change value in priority queue, we allow old values.
      // So we must check if the node is the latest or not. Discard old values.
      if(cur[1]>fScore[cur[0]]) continue;
      int r = cur[0]/N; int c = cur[0]%N;
      // Target reached!
      if(r==tr && c==tc){markPath(sr,sc,tr,tc); return;}
      closedSet.add(cur[0]);
      // Use 9 to mark this cell has been explored for visualsation purposes.
      board[cur[0]/N][cur[0]%N] = 9;
      for(int i=0;i<4;i++){
        int nr = r+dr[i]; int nc = c+dc[i];
        if(nr>-1 && nr<N && nc>-1 && nc<N && board[nr][nc]==0){
          if(closedSet.contains(nr*N+nc)) continue;
          int tempG = gScore[cur[0]]+1;
          if(tempG>=gScore[nr*N+nc]) continue;
          int tempF = tempG+hValue(nr,nc,tr,tc);
          fringe.offer(new int[]{nr*N+nc,tempF});
          parent[nr*N+nc] = cur[0];
          gScore[nr*N+nc] = tempG;
          fScore[nr*N+nc] = tempF;
        }
      }
    }
    System.out.println("No path found between "+sr+","+sc+" and "+tr+","+tc);
    board[sr][sc] = 4;
    board[tr][tc] = 4;
    for(int[]row:board) System.out.println(Arrays.toString(row));
  }

  private static void markPath(int sr,int sc,int tr,int tc){
    int cur = tr*N+tc;
    // Use 5 to mark the path from target back to start.
    while(parent[cur]!=-1){
      cur = parent[cur];
      board[cur/N][cur%N] = 5;
    }
    board[sr][sc] = 4;
    board[tr][tc] = 4;
    for(int[]row:board) System.out.println(Arrays.toString(row));
  }

  private static int hValue(int sr,int sc,int tr,int tc){
    // taxicab, admissible, monotonic, therefore can use closed set.
    return Math.abs(tr-sr)+Math.abs(tc-sc);
  }
}
