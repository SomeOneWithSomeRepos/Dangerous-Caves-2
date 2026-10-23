package me.imdanix.caves.util;

import me.imdanix.caves.util.random.WeightedPool;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WeightedPoolTest {

    @Test
    void emptyPoolIsEmpty() {
        assertTrue(new WeightedPool<String>().isEmpty());
    }

    @Test
    void addRespectsWeights() {
        WeightedPool<String> pool = new WeightedPool<>();
        pool.add("a", 3);
        pool.add("b", 1);
        int a = count(pool, "a", 10_000);
        assertEquals(7500, a, 500);
    }

    @Test
    void constructorWeighsCollection() {
        WeightedPool<String> pool = new WeightedPool<>(List.of("a", "b"), s -> s.equals("a") ? 2 : 1);
        int a = count(pool, "a", 10_000);
        assertEquals(6666, a, 500);
    }

    @Test
    void zeroWeightElementNeverAppears() {
        WeightedPool<String> pool = new WeightedPool<>();
        pool.add("a", 2);
        pool.add("b", 0);
        assertEquals(10_000, count(pool, "a", 10_000));
        assertEquals(0, count(pool, "b", 10_000));
    }

    private static int count(WeightedPool<String> pool, String element, int draws) {
        int n = 0;
        for (int i = 0; i < draws; i++) {
            if (pool.next().equals(element)) n++;
        }
        return n;
    }
}
