package me.imdanix.caves.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void getDoubleParsesValidNumbers() {
        assertEquals(3.5d, Utils.getDouble("3.5", 0));
        assertEquals(-12d, Utils.getDouble("-12", 0));
        assertEquals(0d, Utils.getDouble("0", -1));
    }

    @Test
    void getDoubleReturnsDefaultOnInvalidInput() {
        assertEquals(0.75d, Utils.getDouble("abc", 0.75));
        assertEquals(1d, Utils.getDouble("", 1));
        assertEquals(2d, Utils.getDouble("3..5", 2));
        assertEquals(2d, Utils.getDouble("3.5.5", 2));
    }

    @Test
    void getDoubleRejectsMalformedButPrefixValidStrings() {
        assertEquals(-1d, Utils.getDouble("3.5abc", -1));
        assertEquals(-1d, Utils.getDouble("--3", -1));
    }

    @Test
    void capitalizeCapitalizesEveryWord() {
        assertEquals("Hello World", Utils.capitalize("hello world"));
        assertEquals("Single", Utils.capitalize("single"));
    }

    @Test
    void globMatchesStarWildcard() {
        var pattern = Utils.globToPattern("world-*");
        assertEquals(true, pattern.matcher("world-nether").matches());
        assertEquals(true, pattern.matcher("world-").matches());
        assertEquals(false, pattern.matcher("world").matches());
        assertEquals(false, pattern.matcher("my-world-1").matches());
    }

    @Test
    void globMatchesQuestionMarkWildcard() {
        var pattern = Utils.globToPattern("planet_?_*");
        assertEquals(true, pattern.matcher("planet_1_a").matches());
        assertEquals(false, pattern.matcher("planet_12_a").matches());
        assertEquals(false, pattern.matcher("planet__a").matches());
    }

    @Test
    void globEscapesRegexCharacters() {
        var pattern = Utils.globToPattern("world.1+*");
        assertEquals(true, pattern.matcher("world.1+abc").matches());
        assertEquals(false, pattern.matcher("worldX1abc").matches());
        assertEquals(false, pattern.matcher("world.1").matches());
    }

    @Test
    void globMatchesExactNames() {
        var pattern = Utils.globToPattern("world");
        assertEquals(true, pattern.matcher("world").matches());
        assertEquals(false, pattern.matcher("world2").matches());
    }

    @Test
    void matchesWorldSupportsGlobsAndPlainNames() {
        var entries = java.util.Arrays.asList("world", "planet_*_nether", "flat?");
        assertEquals(true, Utils.matchesWorld("world", entries));
        assertEquals(true, Utils.matchesWorld("planet_03_a_nether", entries));
        assertEquals(true, Utils.matchesWorld("flat1", entries));
        assertEquals(false, Utils.matchesWorld("world_nether", entries));
        assertEquals(false, Utils.matchesWorld("planet_03_a", entries));
        assertEquals(false, Utils.matchesWorld("other", entries));
    }

    @Test
    void getEnumResolvesAndDefaults() {
        assertEquals(TickStub.B, Utils.getEnum(TickStub.class, "B"));
        assertEquals(null, Utils.getEnum(TickStub.class, "NOT_EXISTING"));
        assertEquals(TickStub.C, Utils.getEnum(TickStub.class, "NOT_EXISTING", TickStub.C));
    }

    @Test
    void getEnumSetIgnoresInvalidValues() {
        var set = Utils.getEnumSet(TickStub.class, java.util.Arrays.asList("a", "b", "nonsense"));
        assertEquals(2, set.size());
        assertEquals(java.util.EnumSet.of(TickStub.A, TickStub.B), set);
    }

    enum TickStub {A, B, C}
}
