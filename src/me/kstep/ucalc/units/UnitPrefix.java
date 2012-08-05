package me.kstep.ucalc.units;
import java.lang.reflect.Field;

class UnitPrefix extends LinearUnit {
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

    final static public double Ki = 1024.0;
    final static public double Mi = 1024*1024.0;
    final static public double Gi = 1024*1024*1024.0;
    final static public double Ti = 1024*1024*1024*1024.0;
    final static public double Pi = 1024*1024*1024*1024*1024.0;
    final static public double Ei = 1024*1024*1024*1024*1024*1024.0;
    final static public double Zi = 1024*1024*1024*1024*1024*1024*1024.0;
    final static public double Yi = 1024*1024*1024*1024*1024*1024*1024*1024.0;

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

    UnitPrefix(String prefix, Unit targetUnit) {
        super(prefix + targetUnit, prefixToScale(prefix), targetUnit);
    }

    public static double prefixToScale(String prefix) {
        try {
            Field property = UnitPrefix.class.getField(prefix);
            return property.getDouble(null);

        } catch (IllegalAccessException e) {
            return 0.0;

        } catch (NoSuchFieldException e) {
            return 0.0;
        }
    }
}

