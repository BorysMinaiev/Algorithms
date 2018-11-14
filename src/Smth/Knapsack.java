package Smth;

import java.util.Arrays;

class Knapsack {
    int sz;
    int[] cost;
    int n;
    int[] qPos, qCost;

    Knapsack(int n) {
        this.n = n;
        cost = new int[n + 1];
        Arrays.fill(cost, Integer.MAX_VALUE / 2);
        qPos = new int[n + 1];
        qCost = new int[n + 1];
        cost[0] = 0;
    }

    void addNumber(int count, int value) {
        for (int startPos = 0; startPos < value; startPos++) {
            if (startPos > n)
                break;
            int qIt = 0, qSz = 0;
            int qAddAll = 0;
            for (int pos = startPos, rPos = 0; pos <= n; pos += value, rPos++) {
                if (cost[pos] != Integer.MAX_VALUE / 2) {
                    int nCost = -qAddAll + cost[pos];
                    while (qIt < qSz && qCost[qSz - 1] > nCost)
                        qSz--;
                    qCost[qSz] = nCost;
                    qPos[qSz++] = rPos;
                }
                while (qIt < qSz && rPos - qPos[qIt] > count)
                    qIt++;
                if (qIt < qSz) {
                    int realCost = qAddAll + qCost[qIt];
                    cost[pos] = Math.min(cost[pos], realCost);
                }
                qAddAll++;
            }
        }
    }
}