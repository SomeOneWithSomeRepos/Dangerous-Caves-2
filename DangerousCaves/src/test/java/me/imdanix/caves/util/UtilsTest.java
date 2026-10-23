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
