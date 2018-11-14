package Geometry;

import java.io.*;
import java.util.*;

public class PolygonIntersect {
    FastScanner in;
    PrintWriter out;

    final double eps = 1e-9;

    double vectMul(Point p1, Point p2, Point p3) {
        return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
    }

    class Point implements Comparable<Point> {
        double x, y;
        Point[] next;
        boolean was;

        @Override
        public String toString() {
            return "Point [x=" + x + ", y=" + y + "]";
        }

        double go() {
            if (was)
                return 0.;
            was = true;
            Point nextP = null;
            if (next[0] == null) {
                nextP = next[1];
            } else {
                if (next[1] == null) {
                    nextP = next[0];
                } else {
                    double v = vectMul(this, next[0], next[1]);
                    if (v > 0) {
                        nextP = next[1];
                    } else {
                        nextP = next[0];
                    }
                }
            }
            double dx = nextP.x - x;
            double dy = nextP.y + y;
            return nextP.go() + dx * dy;
        }

        public Point(double x, double y) {
            super();
            this.x = x;
            this.y = y;
        }

        void addEdge(int type, Point n) {
            if (next == null) {
                next = new Point[2];
            }
            next[type] = n;
        }

        @Override
        public int compareTo(Point o) {
            if (Math.abs(x - o.x) <= eps) {
                if (Math.abs(y - o.y) <= eps)
                    return 0;
                return Double.compare(y, o.y);
            }
            return Double.compare(x, o.x);
        }

    }

    class Line {
        double A, B, C;

        Line(Point a, Point b) {
            A = b.y - a.y;
            B = a.x - b.x;
            C = -(A * a.x + B * a.y);
        }

        @Override
        public String toString() {
            return "Line [A=" + A + ", B=" + B + ", C=" + C + "]";
        }

    }

    Point intersect(Line l1, Line l2) {
        double zn = l1.A * l2.B - l1.B * l2.A;
        if (Math.abs(zn) < eps)
            return null;
        Point res = new Point(0, 0);
        res.x = -(l1.C * l2.B - l1.B * l2.C) / zn;
        res.y = -(l1.A * l2.C - l2.A * l1.C) / zn;
        return res;
    }

    double f(Point A, Point B, Point C) {
        if (Math.abs(A.x - B.x) > eps) {
            return (C.x - A.x) / (B.x - A.x);
        }
        return (C.y - A.y) / (B.y - A.y);
    }

    boolean onSegment(Point A, Point B, Point C) {
        if (C.x < Math.min(A.x, B.x) - eps)
            return false;
        if (C.x > Math.max(A.x, B.x) + eps)
            return false;
        if (C.y < Math.min(A.y, B.y) - eps)
            return false;
        if (C.y > Math.max(A.y, B.y) + eps)
            return false;
        return true;
    }

    double sq(Point[] a) {
        double s = 0.;
        for (int i = 0; i < a.length; i++) {
            double dx = a[i].x - a[(i + 1) % a.length].x;
            double dy = a[i].y + a[(i + 1) % a.length].y;
            s += dx * dy;
        }
        return s;
    }

    boolean inside(Point a, Point[] b) {
        int ins = 0;
        for (int i = 0; i < b.length; i++) {
            Point v = b[i];
            Point u = b[(i + 1) % b.length];
            if (v.y > u.y) {
                Point tmp = v;
                v = u;
                u = tmp;
            }
            if (vectMul(v, u, a) < 0) {
                continue;
            }
            double minY = Math.min(v.y, u.y), maxY = Math.max(v.y, u.y);
            if (minY <= a.y + eps && maxY > a.y + eps) {
                ins = 1 - ins;
            }
        }
        return ins == 1;
    }

    void solve() {
        final int n = in.nextInt();
        final Point[] a = new Point[n];
        for (int i = 0; i < n; i++) {
            a[i] = new Point(in.nextInt(), in.nextInt());
        }
        if (sq(a) < 0) {
            for (int l = 0, r = n - 1; l < r; l++, r--) {
                Point tmp = a[l];
                a[l] = a[r];
                a[r] = tmp;
            }
        }
        final int m = in.nextInt();
        final Point[] b = new Point[m];
        for (int i = 0; i < m; i++) {
            b[i] = new Point(in.nextInt(), in.nextInt());
        }
        if (sq(b) < 0) {
            for (int l = 0, r = m - 1; l < r; l++, r--) {
                Point tmp = b[l];
                b[l] = b[r];
                b[r] = tmp;
            }
        }
        Line[] l1 = new Line[n];
        Line[] l2 = new Line[m];
        for (int i = 0; i < n; i++) {
            l1[i] = new Line(a[i], a[(i + 1) % n]);
        }
        for (int i = 0; i < m; i++) {
            l2[i] = new Line(b[i], b[(i + 1) % m]);
        }
        ArrayList<Point>[] aa = new ArrayList[n];
        for (int i = 0; i < n; i++)
            aa[i] = new ArrayList<>();
        ArrayList<Point>[] bb = new ArrayList[m];
        for (int i = 0; i < m; i++)
            bb[i] = new ArrayList<>();
        int cnt2 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Point inter = intersect(l1[i], l2[j]);
                if (inter != null) {
                    if (onSegment(a[i], a[(i + 1) % n], inter))
                        if (onSegment(b[j], b[(j + 1) % m], inter)) {
                            aa[i].add(inter);
                            bb[j].add(inter);
                            cnt2++;
                        }
                }
            }
        }
        if (cnt2 == 0) {
            if (inside(a[0], b)) {
                out.println(1);
            } else {
                if (inside(b[0], a)) {
                    out.println(1);
                } else {
                    out.println(0);
                }
            }
            return;
        }
        for (int i = 0; i < n; i++) {
            final int j = i;
            aa[i].add(a[i]);
            aa[i].add(a[(i + 1) % n]);
            Collections.sort(aa[i], new Comparator<Point>() {

                @Override
                public int compare(Point o1, Point o2) {
                    return Double.compare(f(a[j], a[(j + 1) % n], o1),
                            f(a[j], a[(j + 1) % n], o2));
                }
            });
            for (int k = 0; k < aa[i].size() - 1; k++) {
                aa[i].get(k).addEdge(0, aa[i].get(k + 1));
            }
        }
        for (int i = 0; i < m; i++) {
            bb[i].add(b[i]);
            bb[i].add(b[(i + 1) % m]);

            final int j = i;
            Collections.sort(bb[i], new Comparator<Point>() {

                @Override
                public int compare(Point o1, Point o2) {
                    return Double.compare(f(b[j], b[(j + 1) % m], o1),
                            f(b[j], b[(j + 1) % m], o2));
                }
            });
            for (int k = 0; k < bb[i].size() - 1; k++) {
                bb[i].get(k).addEdge(1, bb[i].get(k + 1));
            }
        }
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (Point p : aa[i])
                if (p.next[0] != null && p.next[1] != null) {
                    cnt += Math.abs(p.go()) > eps ? 1 : 0;
                }
        }
        out.println(cnt);
    }

    void run() {
        try {
            in = new FastScanner(new File("input.txt"));
            out = new PrintWriter(new File("output.txt"));

            solve();

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    void runIO() {

        in = new FastScanner(System.in);
        out = new PrintWriter(System.out);

        solve();

        out.close();
    }

    class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public FastScanner(InputStream f) {
            br = new BufferedReader(new InputStreamReader(f));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                String s = null;
                try {
                    s = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s == null)
                    return null;
                st = new StringTokenizer(s);
            }
            return st.nextToken();
        }

        boolean hasMoreTokens() {
            while (st == null || !st.hasMoreTokens()) {
                String s = null;
                try {
                    s = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (s == null)
                    return false;
                st = new StringTokenizer(s);
            }
            return true;
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }
    }

    public static void main(String[] args) {
        new PolygonIntersect().run();
    }
}