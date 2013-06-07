package me.kstep.ucalc.units.tests;

import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.Units;
import me.kstep.ucalc.units.UnitsManager;

class ConversionTest extends TestCase {
    private UnitsManager uman;
	
    void setUp() {
		uman = Units.load();
    }
	 
	void testReciprocalConversion() {
		Unit ohm = uman.get("ohm");
		Unit S = uman.get("S");
		
		assertTrue(ohm.to(10.0, S) == 0.1);
		assertTrue(ohm.from(0.1, S) == 10.0);
	}
}

