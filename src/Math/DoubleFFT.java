package Math;

import java.io.*;
import java.util.*;

public class DoubleFFT {

    void FFT(double[] re, double[] im, boolean invert) {
        int n = re.length;
        if (im.length != n) {
            throw new AssertionError("Sizes of arrays differ");
        }
        if (Integer.bitCount(n) != 1) {
            throw new AssertionError("N is not power of 2");
        }
        int shift = 32 - Integer.numberOfTrailingZeros(n);
        for (int i = 1; i < n; i++) {
            int j = Integer.reverse(i << shift);
            if (i < j) {
                double temp = re[i];
                re[i] = re[j];
                re[j] = temp;
                temp = im[i];
                im[i] = im[j];
                im[j] = temp;
            }
        }
        for (int len = 2; len <= n; len *= 2) {
            int half = len / 2;
            double alpha = 2 * Math.PI / len;
            double cosAlpha = Math.cos(alpha);
            double sinAlpha = (invert ? -1 : 1) * Math.sin(alpha);
            for (int start = 0; start < n; start += len) {
                double curRe = 1;
                double curIm = 0;
                for (int j = 0; j < half; j++) {
                    double uRe = re[start + j];
                    double uIm = im[start + j];
                    double vRe = re[start + j + half] * curRe
                            - im[start + j + half] * curIm;
                    double vIm = re[start + j + half] * curIm
                            + im[start + j + half] * curRe;
                    re[start + j] = uRe + vRe;
                    im[start + j] = uIm + vIm;
                    re[start + j + half] = uRe - vRe;
                    im[start + j + half] = uIm - vIm;
                    double newRe = curRe * cosAlpha - curIm * sinAlpha;
                    curIm = curRe * sinAlpha + curIm * cosAlpha;
                    curRe = newRe;
                }
            }
        }
        if (invert) {
            for (int i = 0; i < n; i++) {
                re[i] /= n;
                im[i] /= n;
            }
        }
    }

    long[] mul(long[] a, long[] b) {
        int len = Math.max(a.length, b.length) * 2;
        int mLen = 1;
        while (mLen < len)
            mLen *= 2;
        double[] r1 = new double[mLen];
        double[] i1 = new double[mLen];
        for (int i = 0; i < a.length; i++)
            r1[i] = a[i];
        double[] r2 = new double[mLen];
        double[] i2 = new double[mLen];
        for (int i = 0; i < b.length; i++)
            r2[i] = b[i];
        FFT(r1, i1, false);
        FFT(r2, i2, false);
        double[] rNew = new double[mLen];
        double[] iNew = new double[mLen];
        for (int i = 0; i < mLen; i++) {
            rNew[i] = r1[i] * r2[i] - i1[i] * i2[i];
            iNew[i] = r1[i] * i2[i] + r2[i] * i1[i];
        }
        FFT(rNew, iNew, true);
        long[] res = new long[mLen];
        for (int i = 0; i < mLen; i++)
            res[i] = (long) Math.round(rNew[i]);
        return res;
    }

    long[] mulFast(long[] a, long[] b) {
        int len = Math.max(a.length, b.length) * 2;
        int mLen = 1;
        while (mLen < len)
            mLen *= 2;
        double[] r1 = new double[mLen];
        double[] i1 = new double[mLen];
        for (int i = 0; i < a.length; i++) {
            r1[i] = a[i];
            i1[i] = b[i];
        }
        FFT(r1, i1, false);
        double[] rNew = new double[mLen];
        double[] iNew = new double[mLen];
        for (int i = 0; i < mLen; i++) {
            rNew[i] = r1[i] * r1[i] - i1[i] * i1[i];
            iNew[i] = r1[i] * i1[i] + r1[i] * i1[i];
        }
        FFT(rNew, iNew, true);
        long[] res = new long[mLen];
        for (int i = 0; i < mLen; i++)
            res[i] = (long) Math.round(iNew[i] / 2);
        return res;
    }

    long[] slowMul(long[] a, long[] b) {
        long[] res = new long[a.length + b.length + 2];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < b.length; j++)
                res[i + j] += a[i] * b[j];
        return res;
    }

    boolean same(long[] a, long[] b) {
        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            if (i < a.length && i < b.length) {
                if (a[i] != b[i])
                    return false;
            } else {
                if (i < a.length) {
                    if (a[i] != 0)
                        return false;
                } else {
                    if (b[i] != 0)
                        return false;
                }
            }
        }
        return true;
    }

    void ACTest() {
        for (int test = 0;; test++) {
            System.err.println(test);
            int n = 1 + rnd.nextInt(100);
            long[] a = new long[n];
            long[] b = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = rnd.nextInt(100);
                b[i] = rnd.nextInt(100);
            }
            long[] r1 = mulFast(a, b);
            long[] r2 = slowMul(a, b);
            if (!same(r1, r2)) {
                System.err.println(Arrays.toString(r1));
                System.err.println(Arrays.toString(r2));
                throw new AssertionError();
            }
        }
    }

    void TLTest() {
        for (int test = 0;; test++) {
            System.err.println(test);
            long st = System.currentTimeMillis();
            int n = 1000000;
            long[] a = new long[n];
            long[] b = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = rnd.nextInt(10000);
                b[i] = rnd.nextInt(10000);
            }
            long[] r1 = mulFast(a, b);
            System.err.println(System.currentTimeMillis() - st + " ms");
        }
    }

    Random rnd = new Random(77);

    void realSolve() throws IOException {
        TLTest();
    }

    private class InputReader {
        StringTokenizer st;
        BufferedReader br;

        public InputReader(File f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public InputReader(InputStream f) {
            br = new BufferedReader(new InputStreamReader(f));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                String s;
                try {
                    s = br.readLine();
                } catch (IOException e) {
                    return null;
                }
                if (s == null)
                    return null;
                st = new StringTokenizer(s);
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        boolean hasMoreElements() {
            while (st == null || !st.hasMoreElements()) {
                String s;
                try {
                    s = br.readLine();
                } catch (IOException e) {
                    return false;
                }
                st = new StringTokenizer(s);
            }
            return st.hasMoreElements();
        }

        long nextLong() {
            return Long.parseLong(next());
        }
    }

    InputReader in;
    PrintWriter out;

    void solveIO() throws IOException {
        in = new InputReader(System.in);
        out = new PrintWriter(System.out);

        realSolve();

        out.close();

    }

    public static void main(String[] args) throws IOException {
        new DoubleFFT().solveIO();
    }
}