package Math;

public class Gauss {
    final double eps = 1e-9;

    void doGauss(double[][] a) {
        int n = a.length, m = a[0].length;
        int cur = 0;
        for (int i = 0; i + 1 < m; i++) {
            if (cur == n) {
                break;
            }
            int swap = cur;
            for (int j = cur + 1; j < n; j++) {
                if (Math.abs(a[j][i]) > Math.abs(a[swap][j])) {
                    swap = j;
                }
            }
            double[] tmp = a[cur];
            a[cur] = a[swap];
            a[swap] = tmp;
            if (Math.abs(a[i][cur]) < eps) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (j != cur) {
                    double mul = a[j][i] / a[cur][i];
                    for (int k = 0; k < m; k++) {
                        a[j][k] -= mul * a[cur][k];
                    }
                }
            }
            double div = a[cur][i];
            for (int j = 0; j < m; j++) {
                a[cur][j] /= div;
            }
            cur++;
        }
    }
}
