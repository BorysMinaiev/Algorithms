package SegmentTrees;

public class SegmentTreePush {
    // += on segment,
    // min on segment

    int[] pushAdd;
    int[] min;
    final int NEUTRAL_ELEMENT = Integer.MAX_VALUE;
    final int START_ELEMENT = 0;
    final int NEUTRAL_PUSH = 0;

    int n;

    SegmentTreePush(int n) {
        this.n = n;
        pushAdd = new int[n * 4];
        min = new int[n * 4];
        init(0, 0, n - 1);
    }

    void init(int v, int l, int r) {
        if (l == r) {
            min[v] = START_ELEMENT;
        } else {
            int m = (l + r) >> 1;
            init(v * 2 + 1, l, m);
            init(v * 2 + 2, m + 1, r);
            min[v] = join(min[v * 2 + 1], min[v * 2 + 2]);
        }
    }

    // TODO: change it
    int join(int leftValue, int rightValue) {
        return Math.min(leftValue, rightValue);
    }

    // TODO: change it
    void apply(int v, int l, int r, int addValue) {
        pushAdd[v] += addValue;
        min[v] += addValue;
    }

    void push(int v, int l, int r) {
        if (pushAdd[v] == NEUTRAL_PUSH) {
            return;
        }
        int m = (l + r) >> 1;
        apply(v * 2 + 1, l, m, pushAdd[v]);
        apply(v * 2 + 2, m + 1, r, pushAdd[v]);
        pushAdd[v] = NEUTRAL_PUSH;
    }

    int get(int v, int l, int r, int needL, int needR) {
        if (needL > needR) {
            return NEUTRAL_ELEMENT;
        }
        if (l == needL && r == needR) {
            return min[v];
        }
        int m = (l + r) >> 1;
        push(v, l, r);
        int leftAns = get(v * 2 + 1, l, m, needL, Math.min(needR, m));
        int rightAns = get(v * 2 + 2, m + 1, r, Math.max(needL, m + 1), needR);
        return join(leftAns, rightAns);
    }

    int get(int l, int r) {
        return get(0, 0, n - 1, l, r);
    }

    void add(int v, int l, int r, int needL, int needR, int addValue) {
        if (needL > needR) {
            return;
        }
        if (l == needL && r == needR) {
            apply(v, l, r, addValue);
        } else {
            int m = (l + r) >> 1;
            push(v, l, r);
            add(v * 2 + 1, l, m, needL, Math.min(needR, m), addValue);
            add(v * 2 + 2, m + 1, r, Math.max(needL, m + 1), needR, addValue);
            min[v] = join(min[v * 2 + 1], min[v * 2 + 2]);
        }
    }

    void add(int l, int r, int addValue) {
        add(0, 0, n - 1, l, r, addValue);
    }
}

