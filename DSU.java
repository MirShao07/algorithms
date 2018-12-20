// Lightweight Disjoint Set Union, suitable as a subroutine
// Weighted path compression implementation
class DSU{
        int N;
        int[] parentIndex;
        int[] size;

        DSU(int range){
            N = range;
            parentIndex = new int[N];
            size = new int[N];
            for(int i=0;i<N;i++){
                parentIndex[i]=i;
                size[i] = 1;
            }
        }

        private int find(int x){
            while(parentIndex[x]!=x){
                parentIndex[x] = parentIndex[parentIndex[x]];
                x = parentIndex[x];
            }
            return x;
        }

        private void union(int x, int y){
            int xroot = find(x);
            int yroot = find(y);
            if(xroot!=yroot){
                if(size[xroot]>size[yroot]){
                    int temp = xroot;
                    xroot = yroot;
                    yroot = temp;
                }
                parentIndex[xroot] = yroot;
                size[yroot] += size[xroot];
            }
        }
    }
