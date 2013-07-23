package me.kstep.ucalc.units;

import android.test.AndroidTestCase;
import me.kstep.ucalc.numbers.UNumber;

//import org.junit.Test;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import static org.junit.Assert.*;

//@RunWith(JUnit4.class)
public class ConversionTest extends AndroidTestCase {
    private UnitsManager uman;

    public ConversionTest() {
        super();
    }

    //@Before
    protected void setUp() {
        uman = UnitsLoader.load();
        uman.simplifyAll();
    }

    //@Test
    public void testDimensionlessConversion() {
        Unit mole = uman.get("mole");
        assertEquals(6.0221412927e23, mole.to(1, Unit.NONE).doubleValue(), 0.00000001);
        assertEquals(6.0221412927e23, Unit.NONE.from(1, mole).doubleValue(), 0.00000001);
    }

    //@Test
    public void testPowerConversion() {
        Unit m3 = uman.get("m³");
        Unit l = uman.get("l");

        assertEquals(1000, m3.to(1, l).doubleValue(), 0.0001);
        assertEquals(1000, l.from(1, m3).doubleValue(), 0.0001);

        assertEquals(0.001, m3.from(1, l).doubleValue(), 0.0001);
        assertEquals(0.001, l.to(1, m3).doubleValue(), 0.0001);
    }

    //@Test
    public void testOffsetConversion() {
        Unit C = uman.get("°C");
        Unit F = uman.get("°F");

        assertEquals(32, C.to(0, F).doubleValue(), 0.001);
        assertEquals(0, F.to(32, C).doubleValue(), 0.001);

        assertEquals(212.0, F.from(100, C).doubleValue(), 0.001);
        assertEquals(212.0, C.to(100, F).doubleValue(), 0.001);
    }

    //@Test
    public void testReciprocalConversion() {
        Unit ohm = uman.get("ohm");
        Unit S = uman.get("S");

        assertEquals(0.1, ohm.to(10.0, S).doubleValue(), 0.0001);
        assertEquals(10.0, ohm.from(0.1, S).doubleValue(), 0.0001);
    }

    //@Test
    public void testMultiplication() {
        Unit N = uman.get("N");
        Unit ml = uman.get("ml");
        Unit _ = ml.mul(N).div(N);

        assertEquals(ml, _);
        assertEquals(ml.toString(), _.toString());
    }

    //@Test
    public void testUnbalancedProductConversion() {
        Unit lbf = uman.get("lbf");
        Unit N = uman.get("N");

        assertEquals(22.4808943, N.to(100, lbf).floatValue(), 0.00001);
    }

    //@Test
    public void testIndirectConversion() {
        Unit eV = uman.get("eV");
        Unit J = uman.get("J");

        UNumber _ = eV.to(100, J);
        assertEquals(16.021765e-18, _.floatValue(), 0.00001);
    }

    //@Test
    public void testDeepUnbalancedConversion() {
        Unit hp = uman.get("hp");
        Unit W = uman.get("W");

        UNumber _ = hp.to(1, W);

        assertEquals(745.699872, _.doubleValue(), 0.00001);
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

