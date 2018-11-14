package SegmentTrees;

import java.util.Random;

public class SegTreeTest {
    void start() {
        Random rnd = new Random(123);
        final int MAX = 1000;
        for (int it = 0; it < 1231; it++) {
            System.err.println("it = " + it);
            int n = 1 + rnd.nextInt(MAX);
            SegmentTreePush st = new SegmentTreePush(n);
            int[] vals = new int[n];
            int m = 1 + rnd.nextInt(MAX);
            for (int i = 0; i < m; i++) {
                int l = rnd.nextInt(n);
                int r = rnd.nextInt(n);
                if (l > r) {
                    int tmp = l;
                    l = r;
                    r = tmp;
                }
                if (rnd.nextBoolean()) {
                    // add
                    int value = rnd.nextInt(MAX * 2) - MAX;
                    for (int j = l; j <= r; j++) {
                        vals[j] += value;
                    }
                    st.add(l, r, value);
                } else {
                    int min = Integer.MAX_VALUE;
                    for (int j = l; j <= r; j++) {
                        min = Math.min(min, vals[j]);
                    }
                    int stMin = st.get(l, r);
                    if (min != stMin) {
                        throw new AssertionError();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new SegTreeTest().start();
    }
}
