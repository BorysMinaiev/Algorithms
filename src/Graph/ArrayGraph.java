package Graph;

class ArrayGraph {
    int n;
    int[][] g;

    ArrayGraph(int n, int m) {
        this.n = n;
        __m = m;
        __fr = new int[__m];
        __to = new int[__m];
        __gSz = new int[n];
        g = new int[n][];
    }

    ArrayGraph() {
//        n = in.nextInt();
//        __m = in.nextInt();
//        for (int i = 0; i < __m; i++) {
//            int fr = in.nextInt() - 1;
//            int to = in.nextInt() - 1;
//            addEdge(fr, to);
//        }
    }

    void build() {
        for (int i = 0; i < n; i++) {
            g[i] = new int[__gSz[i]];
        }
        for (int i = 0; i < __fr.length; i++) {
            int f = __fr[i], t = __to[i];
            g[f][--__gSz[f]] = t;
            g[t][--__gSz[t]] = f;
        }
    }

    void addEdge(int fr, int to) {
        __m--;
        this.__fr[__m] = fr;
        this.__to[__m] = to;
        __gSz[fr]++;
        __gSz[to]++;
        if (__m == 0) {
            build();
        }
    }

    private int __m;
    private int[] __fr, __to;
    private int[] __gSz;
}
