package me.imdanix.caves.util.random;

import java.util.random.RandomGenerator;

public class PseudoRandom implements RandomGenerator {
    public static final PseudoRandom ZERO_PSEUDO_RANDOM = new PseudoRandom(null) {
        @Override
        public int nextInt() {
            return 0;
        }
    };

    private final int[] randomValues;
    private int cur = 0;

    public PseudoRandom(int[] values) {
        this.randomValues = values;
    }

    @Override
    public int nextInt() {
        int value = randomValues[cur];
        cur = (cur + 1) % randomValues.length;
        return value;
    }

    @Override
    public long nextLong() {
        return nextInt();
    }

    @Override
    public double nextDouble() {
        return nextInt();
    }
}
