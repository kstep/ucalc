package me.kstep.ucalc.units;

import android.test.ActivityInstrumentationTestCase2;
import java.net.URL;
import me.kstep.ucalc.activities.UCalcActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnitNetworkLoadTest extends ActivityInstrumentationTestCase2<UCalcActivity> {
    private UnitsManager uman = UnitsManager.getInstance();

    public UnitNetworkLoadTest() {
        super(UCalcActivity.class);
    }

    @Before
    public void setUp() {
        uman.clear();
    }

    @Test
    public void loadNBRBCurrencies() {
        UnitCurrenciesLoader loader = new NBRBCurrenciesLoader(getActivity(), 0, false);
        UnitsManager uman = loader.load();

        assertTrue(uman.exists("BYR"));
        assertTrue(uman.exists("USD"));

        assertEquals(3, uman.get("USD").to(100, uman.get("RUB")).doubleValue(), 0.1);
    }

    @Test
    public void loadCBRCurrencies() {
        UnitCurrenciesLoader loader = new CBRCurrenciesLoader(getActivity(), 0, false);
        UnitsManager uman = loader.load();

        assertTrue(uman.exists("BYR"));
        assertTrue(uman.exists("USD"));

        assertEquals(3, uman.get("USD").to(100, uman.get("RUR")).doubleValue(), 0.1);
    }

    @Test
    public void loadECBCurrencies() {
        UnitCurrenciesLoader loader = new ECBCurrenciesLoader(getActivity(), 0, false);
        UnitsManager uman = loader.load();

        assertTrue(uman.exists("EUR"));
        assertTrue(uman.exists("USD"));

        assertEquals(1.3, uman.get("EUR").to(1, uman.get("USD")).doubleValue(), 0.1);
    }
}
