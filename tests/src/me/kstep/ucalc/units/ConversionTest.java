package me.kstep.ucalc.units;

import me.kstep.ucalc.numbers.UNumber;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ConversionTest {
    private UnitsManager uman;

    public ConversionTest() {
        super();
    }

    @Before
    public void setUp() {
        uman = UnitsLoader.load();
        uman.simplifyAll();
    }

    @Test
    public void reciprocalConversion() {
        Unit ohm = uman.get("ohm");
        Unit S = uman.get("S");

        assertEquals(0.1, ohm.to(10.0, S).doubleValue(), 0.0001);
        assertEquals(10.0, ohm.from(0.1, S).doubleValue(), 0.0001);
    }

    @Test
    public void multiplication() {
        Unit N = uman.get("N");
        Unit ml = uman.get("ml");
        Unit _ = ml.mul(N).div(N);

        assertEquals(ml, _);
        assertEquals(ml.toString(), _.toString());
    }

    @Test
    public void unbalancedProductConversion() {
        Unit lbf = uman.get("lbf");
        Unit N = uman.get("N");

        assertEquals(22.4808943, N.to(100, lbf).floatValue(), 0.00001);
    }

    @Test
    public void indirectConversion() {
        Unit eV = uman.get("eV");
        Unit J = uman.get("J");

        UNumber _ = eV.to(100, J);
        assertEquals(16.021765e-18, _.floatValue(), 0.00001);
    }

    @Test
    public void deepUnbalancedConversion() {
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

