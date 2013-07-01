package me.kstep.ucalc.tests;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsLoader;
import me.kstep.ucalc.units.UnitsManager;
import me.kstep.ucalc.numbers.UNumber;

import junit.framework.TestCase;
import org.junit.Test;

public class ConversionTest extends TestCase {
    private UnitsManager uman;

    public ConversionTest() {
        super();
    }

    protected void setUp() {
        uman = UnitsLoader.load();
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

    @Test
    public void testUnbalancedProductConversion() {
        Unit lbf = uman.get("lbf");
        Unit N = uman.get("N");

        assertTrue(22.4808943 - N.to(100, lbf).floatValue() < 0.00001);
    }

    @Test
    public void testIndirectConversion() {
        Unit eV = uman.get("eV");
        Unit J = uman.get("J");

        UNumber _ = eV.to(100, J);
        assertTrue(16.021765e-18 - _.floatValue() < 0.00001);
    }

    @Test
    public void testDeepUnbalancedConversion() {
        Unit hp = uman.get("hp");
        Unit W = uman.get("W");

        UNumber _ = hp.to(1, W);

        assertTrue(745.699872 - _.doubleValue() < 0.00001);
    }

    private void println(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object obj : objs) {
            sb.append(obj.toString());
            sb.append(' ');
        }

        System.out.println(sb.toString());
    }
}

