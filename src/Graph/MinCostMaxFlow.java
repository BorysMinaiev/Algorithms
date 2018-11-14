package Graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class MinCostMaxFlow {
    int n;
    ArrayList<Edge>[] g;

    class Edge {
        int from, to;
        long cap, flow, cost;
        Edge rev;

        public Edge(int from, int to, long cap, long flow, long cost) {
            super();
            this.from = from;
            this.to = to;
            this.cap = cap;
            this.flow = flow;
            this.cost = cost;
        }
    }

    class Vertex implements Comparable<Vertex> {
        int v;
        Edge e;
        long d;

        public Vertex(int v) {
            super();
            this.v = v;
        }

        @Override
        public int compareTo(Vertex o) {
            return d < o.d ? -1 : d > o.d ? 1 : v - o.v;
        }

    }

    public MinCostMaxFlow(int n) {
        super();
        this.n = n;
        g = new ArrayList[n];
        for (int i = 0; i < n; i++)
            g[i] = new ArrayList<Edge>();
    }

    public void addEdge(int fr, int to, int cap, long cost) {
        Edge e1 = new Edge(fr, to, cap, 0, cost);
        Edge e2 = new Edge(to, fr, 0, 0, -cost);
        e1.rev = e2;
        e2.rev = e1;
        g[fr].add(e1);
        g[to].add(e2);
    }

    public long[] getMinCostMaxFlow(int source, int target) {
        long[] h = new long[n];
        for (boolean changed = true; changed; ) {
            changed = false;
            for (int i = 0; i < n; i++) {
                for (Edge e : g[i]) {
                    if (e.cap > 0 && h[e.to] > h[e.from] + e.cost) {
                        h[e.to] = h[e.from] + e.cost;
                        changed = true;
                    }
                }
            }
        }
        Vertex[] vertices = new Vertex[n];
        long[] d = new long[n];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex(i);
        }
        int flow = 0;
        long cost = 0;
        while (true) {
            dijkstra(source, vertices, d, h);
            if (d[target] == Long.MAX_VALUE) {
                break;
            }
            long addFlow = Long.MAX_VALUE;
            Vertex v = vertices[target];
            while (v != vertices[source]) {
                addFlow = Math.min(addFlow, v.e.cap - v.e.flow);
                v = vertices[v.e.from];
            }
            cost += (d[target] + h[target] - h[source]) * addFlow;
            flow += addFlow;
            v = vertices[target];
            while (v != vertices[source]) {
                v.e.flow += addFlow;
                v.e.rev.flow -= addFlow;
                v = vertices[v.e.from];
            }
            for (int i = 0; i < n; i++) {
                h[i] += d[i] == Long.MAX_VALUE ? 0 : d[i];
            }
        }
        return new long[]{flow, cost};
    }

    void dijkstra(int source, Vertex[] vertices, long[] d, long[] h) {
        TreeSet<Vertex> ts = new TreeSet<Vertex>();
        Arrays.fill(d, Long.MAX_VALUE);
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].d = Long.MAX_VALUE;
        }
        d[source] = 0;
        vertices[source].d = 0;
        ts.add(vertices[source]);
        while (!ts.isEmpty()) {
            Vertex v = ts.pollFirst();
            for (Edge e : g[v.v]) {
                if (e.flow >= e.cap) {
                    continue;
                }
                if (d[e.to] == Long.MAX_VALUE
                        || d[e.to] > d[e.from] + e.cost + h[e.from]
                        - h[e.to]) {
                    if (e.cost + h[e.from] - h[e.to] < 0) {
                        throw new AssertionError();
                    }
                    if (ts.contains(vertices[e.to])) {
                        ts.remove(vertices[e.to]);
                    }
                    d[e.to] = d[e.from] + e.cost + h[e.from] - h[e.to];
                    vertices[e.to].d = d[e.to];
                    vertices[e.to].e = e;
                    ts.add(vertices[e.to]);
                }
            }
        }
    }

}
