package Math;

import java.util.Random;

public class Karatsuba {
    final int mod = (int) 1e9 + 7;
    final long mod2 = mod * 1L * mod;
    final int C = 10;


    long[] mult(long[] a, long[] b) {
        long[] res = new long[a.length + b.length - 1];
        int n = a.length;
        if (b.length != n) {
            throw new AssertionError();
        }
        if (n <= C) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    res[i + j] += a[i] * 1L * b[j];
                    if (res[i + j] >= mod2) {
                        res[i + j] -= mod2;
                    }
                }
            }
        } else {
            int mid = n / 2;
            long[] aLeft = new long[mid];
            long[] aRight = new long[n - mid];
            long[] bLeft = new long[mid];
            long[] bRight = new long[n - mid];
            for (int i = 0; i < mid; i++) {
                aLeft[i] = a[i];
                bLeft[i] = b[i];
            }
            for (int i = 0; i < n - mid; i++) {
                aRight[i] = a[i + mid];
                bRight[i] = b[i + mid];
            }
            long[] Q = mult(aLeft, bLeft);
            long[] W = mult(aRight, bRight);
            long[] tmpA = new long[Math.max(aLeft.length, aRight.length)];
            long[] tmpB = new long[tmpA.length];
            for (int i = 0; i < aLeft.length; i++) {
                tmpA[i] = aLeft[i];
                tmpB[i] = bLeft[i];
            }
            for (int i = 0; i < aRight.length; i++) {
                tmpA[i] += aRight[i];
                tmpB[i] += bRight[i];
                if (tmpA[i] >= mod) {
                    tmpA[i] -= mod;
                }
                if (tmpB[i] >= mod) {
                    tmpB[i] -= mod;
                }
            }
            long[] E = mult(tmpA, tmpB);
            for (int i = 0; i < E.length; i++) {
                if (i < Q.length) {
                    E[i] -= Q[i];
                    if (E[i] < 0) {
                        E[i] += mod;
                    }
                }
                if (i < W.length) {
                    E[i] -= W[i];
                    if (E[i] < 0) {
                        E[i] += mod;
                    }
                }
            }
            for (int i = 0; i < Q.length; i++) {
                res[i] = Q[i];
            }
            for (int i = 0; i < E.length; i++) {
                res[i + mid] += E[i];
                if (res[i + mid] >= mod) {
                    res[i + mid] -= mod;
                }
            }
            for (int i = 0; i < W.length; i++) {
                res[i + mid + mid] += W[i];
                if (res[i + mid + mid] >= mod) {
                    res[i + mid + mid] -= mod;
                }
            }
        }
        for (int i = 0; i < res.length; i++) {
            res[i] %= mod;
        }
        return res;
    }

    void start() {
        final int MAX = (int) 5e4;
        long[] a = new long[MAX];
        long[] b = new long[MAX];
        Random rnd = new Random(123);
        for (int i = 0; i < MAX; i++) {
            a[i] = rnd.nextInt(mod);
            b[i] = rnd.nextInt(mod);
        }
        long START = System.currentTimeMillis();
        mult(a, b);
        System.err.println(System.currentTimeMillis() - START);

    }

    public static void main(String[] args) {
        new Karatsuba().start();
    }
}
