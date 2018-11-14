package Templates;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FacebookParallel {
    FastScanner in;
    PrintWriter out;

    class Solver implements Runnable {
        int test;
        Random rnd = new Random();

        Solver(int test) {
            this.test = test;
        }

        Solver readInput() {
            x = in.nextInt();
            y = in.nextInt();
            System.err.println("TEST " + test + ", read input");
            return this;
        }

        int x, y;
        int sum;

        @Override
        public void run() {
            System.err.println("TEST " + test + ", start computing");
            sum = x + y;
            try {
                Thread.sleep(1000 + rnd.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("TEST " + test + ", stop computing");
        }
    }

    void solve() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        int tc = in.nextInt();
        Solver[] workers = new Solver[tc];
        for (int t = 0; t < tc; t++) {
            workers[t] = new Solver(t).readInput();
            executor.execute(workers[t]);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int t = 0; t < tc; t++) {
            System.err.println("Case #" + (t + 1) + ": " + workers[t].sum);
        }

    }

    void run() {
        try {
            in = new FastScanner(new File("Test.in"));
            out = new PrintWriter(new File("Test.out"));

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

        double nextDouble() {
            return Double.parseDouble(next());
        }
    }

    public static void main(String[] args) {
        new FacebookParallel().run();
    }
}