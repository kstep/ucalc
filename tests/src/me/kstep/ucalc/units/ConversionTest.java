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

    public void loadUnits() {
        try {
            uman.clear();

            uman.add(new BaseUnit("kg"));
            uman.add(new BaseUnit("m"));
            uman.add(new BaseUnit("s"));
            uman.add(new BaseUnit("A"));

            uman.add(new PowerUnit(uman.get("m"), 3));
            uman.add(new LinearUnit("l", 0.001, uman.get("m³")));
            uman.add(new UnitPrefix("m", uman.get("l")));

            uman.add(new ProductUnit("N", uman.get("kg"), uman.get("m"), new PowerUnit(uman.get("s"), -2)));

            uman.add(new ProductUnit("J", uman.get("N"), uman.get("m")));
            uman.add(new ProductUnit("W", uman.get("J"), new PowerUnit(uman.get("s"), -1)));

            uman.add(new ProductUnit("V", uman.get("W"), new PowerUnit(uman.get("A"), -1)));

            uman.add(new ProductUnit("C", uman.get("A"), uman.get("s")));
            uman.add(new LinearUnit("eV", 1.602176565e-19, new ProductUnit(uman.get("C"), uman.get("V"))));

            uman.add(new LinearUnit("lb", 0.45359237, uman.get("kg")));
            uman.add(new LinearUnit("gf", 9.80665, new ProductUnit(uman.get("m"), new PowerUnit(uman.get("s"), -2))));
            uman.add(new ProductUnit("lbf", uman.get("gf"), uman.get("lb")));

            // TODO: redefine as 550 lbf*ft/s
            uman.add(new LinearUnit("hp", 745.699872, uman.get("W")));


            uman.add(new ProductUnit("ohm", uman.get("V"), new PowerUnit(uman.get("A"), -1)));
            uman.add(new ProductUnit("S", uman.get("A"), new PowerUnit(uman.get("V"), -1)));

            uman.add(new BaseUnit("°K"));
            uman.add(new LinearUnit("°C", uman.get("°K"), 273.16));
            uman.add(new LinearUnit("°F", new URational(9, 5), 32, uman.get("°C")));

            uman.add(new LinearUnit("mole", 6.0221412927e23, Unit.NONE));

        } catch (UnitsManager.UnitExistsException e) {
        }

        uman.simplifyAll();
    }

    public ConversionTest() {
        super();
        uman = UnitsManager.getInstance();
    }

    //@Before
    protected void setUp() {
        loadUnits();
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

