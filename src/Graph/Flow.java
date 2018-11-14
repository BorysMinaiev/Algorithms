package Graph;

import java.util.ArrayList;
import java.util.Arrays;

public class Flow {
    class Edge {
        int fr, to;
        long flow, cap;
        Edge rev;

        Edge(int fr, int to, long cap) {
            this.fr = fr;
            this.to = to;
            this.cap = cap;
        }
    }

    class FlowGraph {
        int n;
        ArrayList<Edge>[] g;

        FlowGraph(int n) {
            this.n = n;
            g = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                g[i] = new ArrayList<>();
            }
            q = new int[n];
            h = new int[n];
            cur = new int[n];
        }

        void addEdge(int fr, int to, long cap) {
            Edge e1 = new Edge(fr, to, cap);
            Edge e2 = new Edge(to, fr, 0);
            e1.rev = e2;
            e2.rev = e1;
            g[fr].add(e1);
            g[to].add(e2);
        }

        int[] h;
        int[] cur;
        int[] q;

        boolean bfs() {
            int qIt = 0, qSz = 0;
            q[qSz++] = 0;
            Arrays.fill(h, -1);
            h[0] = 0;
            while (qIt < qSz) {
                int v = q[qIt++];
                for (Edge e : g[v]) {
                    if (e.flow == e.cap)
                        continue;
                    if (h[e.to] == -1) {
                        h[e.to] = h[e.fr] + 1;
                        q[qSz++] = e.to;
                    }
                }
            }
            return h[n - 1] != -1;
        }

        long dfs(int v, long flow) {
            if (v == n - 1 || flow == 0)
                return flow;
            for (; cur[v] < g[v].size(); cur[v]++) {
                Edge e = g[v].get(cur[v]);
                if (h[e.to] != h[e.fr] + 1 || e.flow == e.cap)
                    continue;
                long add = dfs(e.to, Math.min(flow, e.cap - e.flow));
                if (add == 0)
                    continue;
                e.flow += add;
                e.rev.flow -= add;
                return add;
            }
            return 0;
        }

        long flow() {
            long res = 0;
            while (bfs()) {
                Arrays.fill(cur, 0);
                while (true) {
                    long add = dfs(0, Long.MAX_VALUE);
                    if (add == 0)
                        break;
                    res += add;
                }
            }
            return res;
        }
    }
}
