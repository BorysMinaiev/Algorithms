package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastFlow {
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

    class Flow {
        int n;
        int[] q;
        int[] dist;
        int[] cntOnLayer;
        int[] prev;
        int[] curEdge;
        boolean[] was;
        boolean canReachSink;
        Edge[] prevEdge;
        List<Edge>[] g;

        Flow(int n) {
            this.n = n;
            g = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                g[i] = new ArrayList<>();
            }
            q = new int[n];
            was = new boolean[n];
            dist = new int[n];
            cntOnLayer = new int[n + 1];
            prev = new int[n];
            curEdge = new int[n];
            prevEdge = new Edge[n];
        }

        Edge addEdge(int fr, int to, long cap) {
            Edge e1 = new Edge(fr, to, cap);
            Edge e2 = new Edge(to, fr, 0);
            e1.rev = e2;
            e2.rev = e1;
            g[fr].add(e1);
            g[to].add(e2);
            return e1;
        }

        boolean bfs() {
            Arrays.fill(was, false);
            Arrays.fill(dist, n);
            Arrays.fill(cntOnLayer, 0);
            cntOnLayer[n] = n;
            dist[n - 1] = 0;
            was[n - 1] = true;
            cntOnLayer[0]++;
            cntOnLayer[n]--;
            int qIt = 0, qSz = 0;
            q[qSz++] = n - 1;
            while (qIt < qSz) {
                int v = q[qIt++];
                for (Edge e : g[v]) {
                    if (was[e.to] || e.rev.flow == e.rev.cap) {
                        continue;
                    }
                    was[e.to] = true;
                    q[qSz] = e.to;
                    cntOnLayer[dist[e.to]]--;
                    dist[e.to] = dist[v] + 1;
                    cntOnLayer[dist[e.to]]++;
                    qSz++;
                }
            }
            return dist[0] != n;
        }

        long augment() {
            long flow = Long.MAX_VALUE;
            for (int i = n - 1; i != 0; i = prev[i]) {
                flow = Math.min(flow, prevEdge[i].cap - prevEdge[i].flow);
            }
            for (int i = n - 1; i != 0; i = prev[i]) {
                prevEdge[i].flow += flow;
                prevEdge[i].rev.flow -= flow;
            }
            return flow;
        }

        int retreat(int v) {
            int newDist = n - 1;
            for (Edge e : g[v]) {
                if (e.flow < e.cap && dist[e.to] < newDist) {
                    newDist = dist[e.to];
                }
            }
            cntOnLayer[dist[v]]--;
            if (cntOnLayer[dist[v]] == 0) {
                if (newDist + 1 > dist[v]) {
                    canReachSink = false;
                }
            }
            dist[v] = 1 + newDist;
            cntOnLayer[dist[v]]++;
            if (v != 0) {
                v = prev[v];
            }
            return v;
        }

        void clear() {
            for (int v = 0; v < n; v++) {
                for (Edge e : g[v]) {
                    e.flow = 0;
                }
            }
        }

        long flow() {
            long res = 0;
            canReachSink = true;
            Arrays.fill(prev, 0);
            Arrays.fill(curEdge, 0);
            clear();
            if (!bfs()) {
                return 0;
            }
            int v = 0;
            while (dist[0] < n) {
                for (; curEdge[v] < g[v].size(); curEdge[v]++) {
                    Edge e = g[v].get(curEdge[v]);
                    if (e.flow < e.cap && dist[v] == dist[e.to] + 1)
                        break;
                }
                if (curEdge[v] != g[v].size()) {
                    Edge e = g[v].get(curEdge[v]);
                    prev[e.to] = v;
                    prevEdge[e.to] = e;
                    v = e.to;
                    if (v == n - 1) {
                        res += augment();
                        v = 0;
                    }
                } else {
                    curEdge[v] = 0;
                    v = retreat(v);
                    if (!canReachSink) {
                        break;
                    }
                }
            }
            return res;
        }
    }
}
