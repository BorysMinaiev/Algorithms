public class SegmentTreeIntMinMaxSet {
    // = on segment,
    // {min, max} on segment

    long joinToLong(int min, int max) {
        return ((long) min << 32) | (max & 0xffffffffL);
    }

    long joinToLong(int value) {
        return joinToLong(value, value);
    }

    int getMinFromLong(long joined) {
        return (int) (joined >> 32);
    }

    int getMaxFromLong(long joined) {
        return (int) (joined);
    }

    int[] pushSet;
    long[] minmax;
    final int NEUTRAL_ELEMENT = Integer.MAX_VALUE;
    final int START_ELEMENT = 0;
    final int NEUTRAL_PUSH = Integer.MIN_VALUE;

    int n;

    SegmentTreeIntMinMaxSet(int n) {
        this.n = n;
        pushSet = new int[n * 4];
        minmax = new long[n * 4];
        init(0, 0, n - 1);
    }

    void init(int v, int l, int r) {
        if (l == r) {
            minmax[v] = joinToLong(START_ELEMENT);
        } else {
            int m = (l + r) >> 1;
            init(v * 2 + 1, l, m);
            init(v * 2 + 2, m + 1, r);
            minmax[v] = join(minmax[v * 2 + 1], minmax[v * 2 + 2]);
        }
    }

    long join(long leftValue, long rightValue) {
        return joinToLong(Math.min(getMinFromLong(leftValue), getMinFromLong(rightValue)), Math.max(getMaxFromLong(leftValue), getMaxFromLong(rightValue)));
    }

    void apply(int v, int setValue) {
        pushSet[v] = setValue;
        minmax[v] = joinToLong(setValue);
    }

    void push(int v) {
        if (pushSet[v] == NEUTRAL_PUSH) {
            return;
        }
        apply(v * 2 + 1, pushSet[v]);
        apply(v * 2 + 2, pushSet[v]);
        pushSet[v] = NEUTRAL_PUSH;
    }

    long get(int v, int l, int r) {
        if (needL > r || needR < l) {
            return joinToLong(NEUTRAL_ELEMENT, -NEUTRAL_ELEMENT);
        }
        if (l >= needL && r <= needR) {
            return minmax[v];
        }
        int m = (l + r) >> 1;
        push(v);
        long leftAns = get(v * 2 + 1, l, m);
        long rightAns = get(v * 2 + 2, m + 1, r);
        return join(leftAns, rightAns);
    }

    long get(int l, int r) {
        needL = l;
        needR = r;
        return get(0, 0, n - 1);
    }

    void set(int v, int l, int r, int setValue) {
        if (needL > r || needR < l) {
            return;
        }
        if (l >= needL && r <= needR) {
            apply(v, setValue);
        } else {
            int m = (l + r) >> 1;
            push(v);
            set(v * 2 + 1, l, m, setValue);
            set(v * 2 + 2, m + 1, r, setValue);
            minmax[v] = join(minmax[v * 2 + 1], minmax[v * 2 + 2]);
        }
    }

    int needL, needR;

    void set(int l, int r, int setValue) {
        needL = l;
        needR = r;
        set(0, 0, n - 1, setValue);
    }
}