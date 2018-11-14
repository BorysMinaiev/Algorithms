package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TwoSat {
    int[][] g;
    int[][] gRev;
    List<Integer> from, to;

    int n;

    TwoSat(int n) {
        this.n = n * 2;
        from = new ArrayList<>();
        to = new ArrayList<>();
    }

    void add(int x, boolean xOk, int y, boolean yOk) {
        x = x * 2 + (xOk ? 1 : 0);
        y = y * 2 + (yOk ? 1 : 0);
        from.add(x);
        to.add(y);
    }

    void buildGraph() {
        int[] gSz = new int[n];
        int[] gRevSz = new int[n];
        for (int i = 0; i < from.size(); i++) {
            int v = from.get(i), u = to.get(i);
            gSz[v]++;
            gRevSz[v ^ 1]++;
            gRevSz[u]++;
            gSz[u ^ 1]++;
        }
        g = new int[n][];
        gRev = new int[n][];
        for (int i = 0; i < n; i++) {
            g[i] = new int[gSz[i]];
            gRev[i] = new int[gRevSz[i]];
        }
        for (int i = 0; i < from.size(); i++) {
            int v = from.get(i), u = to.get(i);
            gSz[v]--;
            g[v][gSz[v]] = u;
            gRevSz[v ^ 1]--;
            gRev[v ^ 1][gRevSz[v ^ 1]] = u ^ 1;
            gRevSz[u]--;
            gRev[u][gRevSz[u]] = v;
            gSz[u ^ 1]--;
            g[u ^ 1][gSz[u ^ 1]] = v ^ 1;
        }
    }

    boolean[] used;
    int[] comp;
    int[] order;
    int oSz;

    int[] q;
    int[] it;

    void dfs1_nonRec(int v) {
        used[v] = true;
        int qSz = 1;
        q[qSz - 1] = v;
        it[0] = 0;
        while (qSz > 0) {
            int vertex = q[qSz - 1];
            if (it[qSz - 1] == g[vertex].length) {
                it[qSz - 1] = 0;
                qSz--;
                order[oSz++] = vertex;
            } else {
                int to = g[vertex][it[qSz - 1]++];
                if (!used[to]) {
                    used[to] = true;
                    q[qSz++] = to;
                }
            }
        }
    }

    void dfs1(int v) {
        used[v] = true;
        for (int i = 0; i < g[v].length; i++) {
            int to = g[v][i];
            if (!used[to]) {
                dfs1(to);
            }
        }
        order[oSz++] = v;
    }

    void dfs2(int v, int cl) {
        comp[v] = cl;
        for (int i = 0; i < gRev[v].length; i++) {
            int to = gRev[v][i];
            if (comp[to] == -1) {
                dfs2(to, cl);
            }
        }
    }

    boolean[] findSol() {
        buildGraph();
        int n = g.length;
        order = new int[n];
        used = new boolean[n];
        comp = new int[n];
        Arrays.fill(comp, -1);
        q = new int[n];
        it = new int[n];
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                dfs1_nonRec(i);
            }
        }
        for (int i = 0, j = 0; i < n; ++i) {
            int v = order[n - i - 1];
            if (comp[v] == -1) {
                dfs2(v, j++);
            }
        }

        for (int i = 0; i < n; ++i) {
            if (comp[i] == comp[i ^ 1]) {
                return null;
            }
        }
        boolean[] ans = new boolean[n / 2];
        for (int i = 0; i < n; ++i) {
            ans[i / 2] = comp[i ^ 1] < comp[i];
        }
        return ans;
    }

    String vertex(int v) {
        return (v % 2 == 0 ? "!" : "") + Integer.toString(v / 2);
    }

    void printGraph() {
        for (int i = 0; i < g.length; i++) {
            for (int to : g[i]) {
                System.err.println(vertex(i) + " -> " + vertex(to));
            }
        }
    }
}