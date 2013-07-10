package me.kstep.ucalc.tests;

import me.kstep.ucalc.units.UnitCurrenciesLoader;
import me.kstep.ucalc.units.NBRBCurrenciesLoader;
import me.kstep.ucalc.units.CBRCurrenciesLoader;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;

import java.net.URL;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnitNetworkLoadTest {
    private UnitsManager uman = UnitsManager.getInstance();

    public UnitNetworkLoadTest() {
        super();
    }

    @Before
    public void setUp() {
        uman.clear();
    }

    @Test
    public void loadNBRBCurrencies() {
        UnitCurrenciesLoader loader = new NBRBCurrenciesLoader();
        UnitsManager uman = loader.load();

        assertTrue(uman.exists("BYR"));
        assertTrue(uman.exists("USD"));

        assertEquals(3, uman.get("USD").to(100, uman.get("RUB")).doubleValue(), 0.1);
    }

    @Test
    public void loadCBRCurrencies() {
        UnitCurrenciesLoader loader = new CBRCurrenciesLoader();
        UnitsManager uman = loader.load();

        assertTrue(uman.exists("BYR"));
        assertTrue(uman.exists("USD"));

        assertEquals(3, uman.get("USD").to(100, uman.get("RUR")).doubleValue(), 0.1);
    }
}
