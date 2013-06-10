package me.kstep.ucalc.units;
import java.lang.reflect.Field;

/**
 * This is a convinient wrapper around `LinearUnit` class.
 * It determines standard SI prefixes to define unit power, so it allows
 * very easy definition of derived unit with some SI prefix, like *kilometer*
 * or *milliliter*.
 */
public class UnitPrefix extends LinearUnit {
    /**
     * Here go prefixes, which define positive powers of 10.
     */
    final static public double da = 10.0;
    final static public double h = 100.0;
    final static public double k = 1000;
    final static public double M = 1e6;
    final static public double G = 1e9;
    final static public double T = 1e12;
    final static public double P = 1e15;
    final static public double E = 1e18;
    final static public double Z = 1e21;
    final static public double Y = 1e24;

    /**
     * These are relatively new prefixes for positive powers of 2,
     * usually used in computer science.
     */
    final static public double Ki = 1024.0;
    final static public double Mi = 1024.0*1024;
    final static public double Gi = 1024.0*1024*1024;
    final static public double Ti = 1024.0*1024*1024*1024;
    final static public double Pi = 1024.0*1024*1024*1024*1024;
    final static public double Ei = 1024.0*1024*1024*1024*1024*1024;
    final static public double Zi = 1024.0*1024*1024*1024*1024*1024*1024;
    final static public double Yi = 1024.0*1024*1024*1024*1024*1024*1024*1024;

    /**
     * Now to the prefixes for negative powers of 10.
     */
    final static public double d = 0.1;
    final static public double c = 0.01;
    final static public double m = 0.001;
    final static public double µ = 1e-6;
    final static public double n = 1e-9;
    final static public double p = 1e-12;
    final static public double f = 1e-15;
    final static public double a = 1e-18;
    final static public double z = 1e-21;
    final static public double y = 1e-24;

    /**
     * We override constructor to define prefixed units conviniently.
     */
    UnitPrefix(String prefix, Unit targetUnit) {
        super(prefix + targetUnit, prefixToScale(prefix), targetUnit);
    }

    UnitPrefix(String name, String prefix, Unit targetUnit) {
        super(name, prefixToScale(prefix), targetUnit);
    }

    /**
     * This method is a little magical. It uses introspection to lookup
     * prefix value in one of class constants above.
     */
    public static double prefixToScale(String prefix) {
        try {
            Field property = UnitPrefix.class.getDeclaredField(prefix);
            return property.getDouble(null);

        } catch (IllegalAccessException e) {
            return 0.0;

        } catch (NoSuchFieldException e) {
            return 0.0;
        }
    }
	
	public static String[] getPrefixes() {
		Field[] fields = UnitPrefix.class.getDeclaredFields();
		String[] result = new String[fields.length];

		for (int i = 0; i < fields.length; i++) {
			try {
				double v = fields[i].getDouble(null);
				String n = fields[i].getName();

				int j = (int) Math.round(Math.log10(v));
				if (-3 > j || j > 3) {
					j = j / 3 + (j < 0? -2: 2);
				}
				j += j < 0? 10: (n.length() == 2 && n.charAt(1) == 'i'? 17: 9);

				//android.util.Log.d("unitprefix", fields[i].getName()+" goes to "+j+" ("+((long)v%1024)+")");
			    result[j] = n;

			} catch (IllegalAccessException e) {
			} catch (IllegalArgumentException e) {}
		}

		return result;
	}
}

