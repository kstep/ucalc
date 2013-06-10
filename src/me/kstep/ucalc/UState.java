package me.kstep.ucalc;

import java.util.ArrayList;
import me.kstep.ucalc.units.Unit;
import me.kstep.ucalc.units.UnitsManager;

public class UState {

    UState() {
		setAngleUnit(UnitsManager.getInstance().get("deg"));
		setRadix(0);
	}

	private Unit angleUnit = null;
	
	public Unit getAngleUnit() {
		return angleUnit;
	}
	
	public void setAngleUnit(Unit angleUnit) {
		if (this.angleUnit != angleUnit) {
			this.angleUnit = angleUnit;
		}
	}
	
	private int radix = 0;
	
	public int getRadix() {
		return radix;
	}
	
	public String getRadixName() {
		return radix == 0? "float":
            radix == 16? "hex":
            radix == 10? "dec":
            radix == 8? "oct":
            radix == 2? "bin":
            "rad" + String.valueOf(radix);
	}
	
	public void setRadix(int radix) {
		this.radix = radix;
	}
}