package me.imdanix.caves.util.random;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PseudoRandomTest {

    @Test
    void cyclesThroughAllValuesBeforeRepeating() {
        PseudoRandom random = new PseudoRandom(new int[]{1, 0, -1});
        assertEquals(1, random.nextInt());
        assertEquals(0, random.nextInt());
        assertEquals(-1, random.nextInt());
        assertEquals(1, random.nextInt());
    }

    @Test
    void wrapAroundDistributesValuesEvenly() {
        PseudoRandom random = new PseudoRandom(new int[]{5, 7});
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < 100; i++) counts.merge(random.nextInt(), 1, Integer::sum);
        assertEquals(50, counts.get(5));
        assertEquals(50, counts.get(7));
    }

    @Test
    void zeroPseudoRandomAlwaysReturnsZero() {
        for (int i = 0; i < 10; i++) {
            assertEquals(0, PseudoRandom.ZERO_PSEUDO_RANDOM.nextInt());
        }
    }
}
