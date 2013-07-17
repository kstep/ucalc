package me.kstep.ucalc.units;

import android.test.ActivityInstrumentationTestCase2;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import me.kstep.ucalc.activities.UCalcActivity;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//import static org.junit.Assert.*;

//@RunWith(JUnit4.class)
public class UnitNetworkLoadTest extends ActivityInstrumentationTestCase2<UCalcActivity> {
    private UnitsManager uman = UnitsManager.getInstance();

    public UnitNetworkLoadTest() {
        super(UCalcActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        uman.clear();
        super.setUp();
    }

    //@Test
    public void testLoadNBRBCurrencies() throws Exception {
        UnitCurrenciesLoader loader = new NBRBCurrenciesLoader(getActivity(), 0, false);
        loader.execute(uman);
        loader.get();

        assertTrue(uman.exists("BYR"));
        assertTrue(uman.exists("RUB"));
        assertTrue(uman.exists("USD"));

        assertEquals(3200.0, uman.get("USD").to(100, uman.get("RUB")).doubleValue(), 100);
    }

    //@Test
    public void testLoadCBRCurrencies() throws Exception {
        UnitCurrenciesLoader loader = new CBRCurrenciesLoader(getActivity(), 0, false);
        loader.execute(uman);
        loader.get();

        assertTrue(uman.exists("RUR"));
        assertTrue(uman.exists("BYR"));
        assertTrue(uman.exists("USD"));

        assertEquals(3200, uman.get("USD").to(100, uman.get("RUR")).doubleValue(), 100);
    }

    //@Test
    public void testLoadECBCurrencies() throws Exception {
        UnitCurrenciesLoader loader = new ECBCurrenciesLoader(getActivity(), 0, false);
        loader.execute(uman);
        loader.get();

        assertTrue(uman.exists("EUR"));
        assertTrue(uman.exists("USD"));
        assertTrue(uman.exists("RUB"));

        assertEquals(1.3, uman.get("EUR").to(1, uman.get("USD")).doubleValue(), 0.1);
    }
}
