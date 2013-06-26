package me.kstep.ucalc.tests;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.Units;
import me.kstep.ucalc.units.UnitsManager;
import junit.framework.TestCase;
import org.junit.Test;

public class ConversionTest extends TestCase {
    private UnitsManager uman;

    public ConversionTest() {
        super();
    }

    protected void setUp() {
        uman = Units.load();
        uman.simplifyAll();
    }

    @Test
    public void testReciprocalConversion() {
        Unit ohm = uman.get("ohm");
        Unit S = uman.get("S");

        assertEquals(0.1, ohm.to(10.0, S).doubleValue());
        assertEquals(10.0, ohm.from(0.1, S).doubleValue());
    }

    @Test
    public void testMultiplication() {
        Unit N = uman.get("N");
        Unit ml = uman.get("ml");
        Unit _ = ml.mul(N).div(N);

        assertEquals(ml, _);
        assertEquals(ml.toString(), _.toString());
    }
}

