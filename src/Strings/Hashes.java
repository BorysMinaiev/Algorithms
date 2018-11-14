package Strings;

import java.math.BigInteger;
import java.util.Random;

public class Hashes {
    private class StringHash {
        private int[] val1, val2;
        private HashHelper helper;
        String s;

        public StringHash(int[] val1, int[] val2, HashHelper helper, String s) {
            super();
            this.val1 = val1;
            this.val2 = val2;
            this.helper = helper;
            this.s = s;
        }

        public int getIntHash(int l, int r) {
            if (r < l) {
                return 0;
            }
            long result = val1[r + 1] - val1[l] * 1L * helper.pow1[r - l + 1];
            result %= helper.MOD1;
            if (result < 0) {
                result += helper.MOD1;
            }
            return (int) result;
        }

        public long getHashWithLength(int l, int r) {
            if (r < l) {
                return 0;
            }
            return ((long) getIntHash(l, r) << 32) ^ (r - l + 1);
        }

        public long getLongHash(int l, int r) {
            if (r < l) {
                return 0;
            }
            long res;
            {
                long result = val1[r + 1] - val1[l] * 1L
                        * helper.pow1[r - l + 1];
                result %= helper.MOD1;
                if (result < 0) {
                    result += helper.MOD1;
                }
                res = result;
            }
            {
                long result = val2[r + 1] - val2[l] * 1L
                        * helper.pow2[r - l + 1];
                result %= helper.MOD2;
                if (result < 0) {
                    result += helper.MOD2;
                }
                res = (res << 32) ^ result;
            }
            return res;
        }
    }

    private class HashHelper {
        final Random rnd = new Random();
        final int BILLION = (int) 1e9;
        final int MUL = 239;
        final int MOD1 = BigInteger
                .valueOf(BILLION + rnd.nextInt(BILLION / 10))
                .nextProbablePrime().intValue();
        final int MOD2 = BigInteger
                .valueOf(BILLION + rnd.nextInt(BILLION / 10))
                .nextProbablePrime().intValue();
        int[] pow1, pow2;

        public HashHelper(final int n) {
            pow1 = new int[n];
            pow2 = new int[n];
            pow1[0] = pow2[0] = 1;
            for (int i = 1; i < n; i++) {
                pow1[i] = (int) (pow1[i - 1] * 1L * MUL % MOD1);
                pow2[i] = (int) (pow2[i - 1] * 1L * MUL % MOD2);
            }
        }

        StringHash generateHash(String s) {
            return new StringHash(generateIntHash(s, MOD1), generateIntHash(s,
                    MOD2), this, s);
        }

        public int compareInt(StringHash s1, int from1, int to1, StringHash s2,
                              int from2, int to2) {
            int len = Math.min(to1 - from1, to2 - from2) + 1;
            int l = 0, r = len + 1;
            while (r - l > 1) {
                int mid = (l + r) >>> 1;
                if (s1.getIntHash(from1, from1 + mid - 1) == s2.getIntHash(
                        from2, from2 + mid - 1)) {
                    l = mid;
                } else {
                    r = mid;
                }
            }
            if (l == len) {
                if (to1 - from1 == to2 - from2) {
                    return 0;
                }
                return (to1 - from1) - (to2 - from2);
            }
            return s1.s.charAt(from1 + l) - s2.s.charAt(from2 + l);
        }

        long addHashWithLengths(long hash1, long hash2) {
            int h1 = getHashFromHashWithLength(hash1), h2 = getHashFromHashWithLength(hash2);
            int l1 = getLengthFromHashWithLength(hash1), l2 = getLengthFromHashWithLength(hash2);
            int newHash = (int) ((h1 * 1L * pow1[l2] + h2) % MOD1);
            return ((long) newHash << 32) ^ (l1 + l2);
        }

        int getLengthFromHashWithLength(long hash) {
            return (int) (hash & -1);
        }

        int getHashFromHashWithLength(long hash) {
            return (int) (hash >> 32);
        }

        public int compareLong(StringHash s1, int from1, int to1,
                               StringHash s2, int from2, int to2) {
            int len = Math.min(to1 - from1, to2 - from2) + 1;
            int l = 0, r = len + 1;
            while (r - l > 1) {
                int mid = (l + r) >>> 1;
                if (s1.getLongHash(from1, from1 + mid - 1) == s2.getLongHash(
                        from2, from2 + mid - 1)) {
                    l = mid;
                } else {
                    r = mid;
                }
            }
            if (l == len) {
                if (to1 - from1 == to2 - from2) {
                    return 0;
                }
                return (to1 - from1) - (to2 - from2);
            }
            return s1.s.charAt(from1 + l) - s2.s.charAt(from2 + l);
        }

        private int[] generateIntHash(String s, int MOD) {
            int[] result = new int[s.length() + 1];
            for (int i = 0; i < s.length(); i++) {
                result[i + 1] = (int) ((result[i] * 1L * MUL + s.charAt(i)) % MOD);
            }
            return result;
        }
    }
}


