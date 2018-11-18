package Strings;


import java.util.Arrays;

public class SuffixTree {
    final int root = 0;
    final int inf = Integer.MAX_VALUE / 2;
    final int maxLetter = 26;

    char[] s;
    boolean created;
    int notHasSuflink = -1;
    int n;
    public int countNodes, cur, j;
    int[] suflink, stringDepth, parent, start;
    int[][] to;

    SuffixTree(char[] s) {
        this.s = s;
        n = s.length;
        int maxCountNodes = 2 * n + 1;
        suflink = new int[maxCountNodes];
        stringDepth = new int[maxCountNodes];
        parent = new int[maxCountNodes];
        start = new int[maxCountNodes];
        to = new int[maxCountNodes][maxLetter];

        Arrays.fill(suflink, -1);
        for (int[] i : to) {
            Arrays.fill(i, -1);
        }

        suflink[root] = root;
        countNodes = 1;
        cur = root;

        j = 0;
        for (int i = 0; i < s.length; i++) {
            while (j <= i) {
                addTo(i);
                if (notHasSuflink >= 0) {
                    suflink[notHasSuflink] = parent[cur];
                    notHasSuflink = -1;
                }
                if (!created) {
                    break;
                }

                created = false;
                j++;
                cur = parent[cur];
                if (suflink[cur] < 0) {
                    notHasSuflink = cur;
                    cur = parent[cur];
                }
                cur = suflink[cur];
                while (stringDepth[cur] < i - j) {
                    cur = to[cur][s[j + stringDepth[cur]] - 'a'];
                }
            }
        }
    }

    void addTo(int i) {
        if (stringDepth[cur] < i - j) {
            new AssertionError();
        }
        if (stringDepth[cur] > i - j) {
            int len = i - j - stringDepth[parent[cur]];
            if (s[start[cur] + len] != s[i]) {
                int v = newNode(parent[cur], start[cur], i
                        - j);
                to[v][s[start[cur] + len] - 'a'] = cur;
                start[cur] += len;
                parent[cur] = v;
                cur = v;
                created = true;
            }
        }
        if (stringDepth[cur] == i - j) {
            if (to[cur][s[i] - 'a'] == -1) {
                to[cur][s[i] - 'a'] = newNode(cur, i, inf);
                created = true;
            }
            cur = to[cur][s[i] - 'a'];
        }
    }

    int newNode(int parentOfNode, int startOfEdge, int endOfEdge) {
        parent[countNodes] = parentOfNode;
        start[countNodes] = startOfEdge;
        stringDepth[countNodes] = endOfEdge;
        to[parentOfNode][s[startOfEdge] - 'a'] = countNodes;
        return countNodes++;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(countNodes + " " + (countNodes - 1) + "\n");
        dfs(root, result);
        return result.toString();
    }

    void dfs(int v, StringBuffer result) {
        for (int i = 0; i < maxLetter; i++) {
            if (to[v][i] != -1) {
                int u = to[v][i];
                result.append((v + 1) + " " + (u + 1) + " "
                        + (start[u] + 1) + " "
                        + (stringDepth[u] == inf ? n : start[u] + stringDepth[u] - stringDepth[v])
                        + "\n");
                dfs(u, result);
            }
        }
    }
}