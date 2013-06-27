package me.kstep.ucalc.units;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.content.res.XmlResourceParser;

import android.util.Log;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UInteger;
import java.lang.reflect.Field;

public class Units {
    public static UnitsManager loadPrefixes() {
        return loadPrefixes(UnitsManager.getInstance());
    }

    public static UnitsManager loadPrefixes(UnitsManager uman) {
        Unit[] units = UnitPrefix.getPrefixes();
        for (Unit unit : units) {
            uman.add(unit, Unit.Category.PREFIX);
        }
        return uman;
    }

    public static UnitsManager load() {
        return load(UnitsManager.getInstance());
    }

    public static UnitsManager inflate(InputStream in) {
        return inflate(in, UnitsManager.getInstance());
    }

    public static UnitsManager inflate(InputStream in, UnitsManager uman) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in, null);
            return inflate(parser, uman);

        } catch (XmlPullParserException e) {
            return uman;
        }
    }

    public static UnitsManager inflate(String filename) {
        return inflate(filename, UnitsManager.getInstance());
    }

    public static UnitsManager inflate(String filename, UnitsManager uman) {
        try {
            return inflate(new FileInputStream(filename), uman);
        } catch (FileNotFoundException e) {
            return uman;
        }
    }

    public static UnitsManager inflate(Context context, int resource) {
        return inflate(context, resource, UnitsManager.getInstance());
    }

    public static UnitsManager inflate(Context context, int resource, UnitsManager uman) {
        XmlResourceParser parser = context.getResources().getXml(resource);
        return inflate(parser, uman);
    }

    public static UnitsManager inflate(XmlPullParser parser) {
        return inflate(parser, UnitsManager.getInstance());
    }

    public static UnitsManager inflate(XmlPullParser parser, UnitsManager uman) {
        uman.clear();

        try {

            int evt = parser.getEventType();

            while (evt != XmlPullParser.END_DOCUMENT && evt != XmlPullParser.START_TAG) {
                evt = parser.next();
            }

            if (evt != XmlPullParser.START_TAG || !parser.getName().equals("units")) {
                return uman;
            }

            rInflate(parser, uman, Unit.Category.MISCELLANEOUS);

        } catch (IOException e) {
            Log.wtf("UCalc.Units", e);
        } catch (XmlPullParserException e) {
            Log.wtf("UCalc.Units", e);
        } catch (NullPointerException e) {
            Log.wtf("UCalc.Units", e);
        } catch (IllegalArgumentException e) {
            Log.wtf("UCalc.Units", e);
        } catch (ClassNotFoundException e) {
            Log.wtf("UCalc.Units", e);
        }

        System.gc();

        return uman;
    }

    private static void rInflate(XmlPullParser parser, UnitsManager uman, Unit.Category category) throws IOException, XmlPullParserException, ClassNotFoundException {
        final int depth = parser.getDepth();
        int evt;

        while (((evt = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && evt != XmlPullParser.END_DOCUMENT) {
            if (evt != XmlPullParser.START_TAG) { continue; }

            if (parser.getName().equals("unit")) {
                uman.add(inflateUnit(parser, uman)).category = category;

            } else if (parser.getName().equals("category")) {
                String categoryName = parser.getAttributeValue(null, "name");
                rInflate(parser, uman,
                        categoryName.equals("time")? Unit.Category.TIME:
                        categoryName.equals("vol")? Unit.Category.VOLUME:
                        categoryName.equals("dist")? Unit.Category.DISTANCE:
                        categoryName.equals("weight")? Unit.Category.WEIGHT:
                        categoryName.equals("elec")? Unit.Category.ELECTRIC:
                        Unit.Category.MISCELLANEOUS);
            }
        }
    }

    private static Unit inflateUnit(XmlPullParser parser, UnitsManager uman) throws IOException, XmlPullParserException, ClassNotFoundException {
        final int depth = parser.getDepth();
        int evt;

        final String className = parser.getAttributeValue(null, "class");
        final String unitName = parser.getAttributeValue(null, "name");
        final String fullName = parser.getAttributeValue(null, "fullname");

        if (className == null) {
            throw new NullPointerException("Unit class missing");
        }

        String description = "";
        String prefix = null;
        UNumber scale = null;
        UNumber rscale = null;
        UNumber offset = null;
        UNumber roffset = null;
        int power = 1;

        ArrayList<Unit> deriveUnits = new ArrayList<Unit>();

        if (!parser.isEmptyElementTag()) {

            while (((evt = parser.next()) != XmlPullParser.END_TAG || parser.getDepth() > depth) && evt != XmlPullParser.END_DOCUMENT) {
                if (evt != XmlPullParser.START_TAG) { continue; }

                final String name = parser.getName();

                if (name.equals("derive")) {
                    evt = parser.next();
                    if (evt == XmlPullParser.TEXT) {
                        String derivedName = parser.getText();
                        deriveUnits.add(derivedName.equals("@none")? Unit.NONE: uman.get(derivedName));

                    } else if (evt == XmlPullParser.START_TAG && parser.getName().equals("unit")) {
                        deriveUnits.add(inflateUnit(parser, uman));
                    }

                } else if (name.equals("description")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        description = parser.getText().trim().replaceAll("\n *", " ");
                    }

                } else if (name.equals("scale")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        scale = UNumber.valueOf(parser.getText());
                    }
                } else if (name.equals("rscale")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        rscale = UNumber.valueOf(parser.getText());
                    }

                } else if (name.equals("offset")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        offset = UNumber.valueOf(parser.getText());
                    }
                } else if (name.equals("roffset")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        roffset = UNumber.valueOf(parser.getText());
                    }
                } else if (name.equals("power")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        power = Integer.valueOf(parser.getText());
                    }
                } else if (name.equals("prefix")) {
                    if (parser.next() == XmlPullParser.TEXT) {
                        prefix = parser.getText();
                    }
                }
            }
        }

        Unit unit = Unit.NONE;

        if (className.equals("base")) {

            if (unitName == null) {
                throw new IllegalArgumentException("Base units must have names");
            }

            unit = new BaseUnit(unitName);

        } else if (className.equals("linear")) {

            if (deriveUnits.size() != 1) {
                throw new IllegalArgumentException("Linear units require single <derive> element");
            }

            if (scale != null || offset != null) {
                unit = new LinearUnit(unitName, scale == null? 1: scale, deriveUnits.get(0), offset == null? 0: offset);
            } else if (rscale != null || roffset != null) {
                unit = new LinearUnit(unitName, rscale == null? 1: rscale, roffset == null? 0: roffset, deriveUnits.get(0));
            } else {
                throw new IllegalArgumentException("Linear units require either <scale>/<offset> or <rscale>/<roffset> elements");
            }

        } else if (className.equals("power")) {

            if (deriveUnits.size() != 1) {
                throw new IllegalArgumentException("Power units require single <derive> element");
            }

            if (unitName != null) {
                unit = new PowerUnit(unitName, deriveUnits.get(0), power);
            } else {
                unit = new PowerUnit(deriveUnits.get(0), power);
            }

        } else if (className.equals("product")) {

            if (deriveUnits.size() == 1) {
                throw new IllegalArgumentException("Product units require at least one <derive> element");
            }

            if (unitName != null) {
                unit = new ProductUnit(unitName, deriveUnits.toArray(new Unit[deriveUnits.size()]));
            } else {
                unit = new ProductUnit(deriveUnits.toArray(new Unit[deriveUnits.size()]));
            }

        } else if (className.equals("prefix")) {

            if (deriveUnits.size() != 1) {
                throw new IllegalArgumentException("Prefix units require single <derive> element");
            }

            if (prefix == null) {
                throw new IllegalArgumentException("Prefix units require <prefix> element");
            }

            if (unitName != null) {
                unit = new UnitPrefix(unitName, prefix, deriveUnits.get(0));
            } else {
                unit = new UnitPrefix(prefix, deriveUnits.get(0));
            }

        } else {
            throw new ClassNotFoundException("Unit class " + className + " is unknown");
        }

        if (unit != Unit.NONE) {
            unit.fullname = fullName;
            unit.description = description;
        }

        return unit.simplify();
    }

    public static UnitsManager load(UnitsManager uman) {
        try {
            uman.clear();

            // Time
            uman.add(new BaseUnit("s"), "Seconds", Unit.Category.TIME);
            uman.add(new LinearUnit("min", 60, uman.get("s")), "Minutes", Unit.Category.TIME);
            uman.add(new LinearUnit("hr", 60, uman.get("min")), "Hours",  Unit.Category.TIME);
            uman.add(new LinearUnit("day", 24, uman.get("hr")), "Days", Unit.Category.TIME);
            uman.add(new LinearUnit("yr", 365.24219, uman.get("day")), "Years", Unit.Category.TIME);
            uman.add(new LinearUnit("wk", 7, uman.get("day")), "Weeks", Unit.Category.TIME);
            uman.add(new LinearUnit("ftnt", 2, uman.get("wk")), "Fortnights", Unit.Category.TIME);
            uman.add(new LinearUnit("sday", 23.934469591898, uman.get("hr")), "Solar days", Unit.Category.TIME);
            uman.add(new LinearUnit("syr", 366.256401862834, uman.get("sday")), "Solar years", Unit.Category.TIME);
            uman.add(new LinearUnit("jyr", 365.25, uman.get("day")), "Julian years", Unit.Category.TIME);

            // Distance
            uman.add(new BaseUnit("m"), "Metric meter", Unit.Category.DISTANCE);
            uman.add(new UnitPrefix("m", uman.get("m")), "Millimeter", Unit.Category.DISTANCE);
            uman.add(new UnitPrefix("c", uman.get("m")), "Centimeter",  Unit.Category.DISTANCE);
            uman.add(new UnitPrefix("k", uman.get("m")), "Kilometer", Unit.Category.DISTANCE);
            uman.add(new ProductUnit("km/hr", uman.get("km"), new PowerUnit(uman.get("hr"), -1)), "Kilometers per hour", Unit.Category.DISTANCE);
            uman.add(new LinearUnit("in", 25.4, uman.get("mm")), "Imperial inch", Unit.Category.DISTANCE);
            uman.add(new LinearUnit("ft", 12, uman.get("in")), "Imperial foot", Unit.Category.DISTANCE);
            uman.add(new LinearUnit("yd", 3, uman.get("ft")), "Imperial yard", Unit.Category.DISTANCE);
            uman.add(new LinearUnit("mi", 1760, uman.get("yd")), "Imperial mile", Unit.Category.DISTANCE);
            uman.add(new ProductUnit("mi/hr", uman.get("mi"), new PowerUnit(uman.get("hr"), -1)), "Miles per hour", Unit.Category.DISTANCE);
            // TODO

            // Volume
            uman.add(new PowerUnit(uman.get("m"), 3), "Cubic meters", Unit.Category.VOLUME);
            uman.add(new LinearUnit("l", 0.001, uman.get("m³")), "Liters", Unit.Category.VOLUME);
            uman.add(new UnitPrefix("m", uman.get("l")), "Milliliters", Unit.Category.VOLUME);
            // TODO

            // Weight
            uman.add(new BaseUnit("kg"), "Metric kilogram", Unit.Category.WEIGHT);
            uman.add(new LinearUnit("g", 0.001, uman.get("kg")), "Gram", Unit.Category.WEIGHT);
            uman.add(new UnitPrefix("m", uman.get("g")), "Milligram", Unit.Category.WEIGHT);
            uman.add(new LinearUnit("t", 1000, uman.get("kg")), "Metric tons", Unit.Category.WEIGHT);
            uman.add(new LinearUnit("st", 907.185, uman.get("kg")), "Short tons", Unit.Category.WEIGHT);
            uman.add(new LinearUnit("lt", 1016.05, uman.get("kg")), "Long tons", Unit.Category.WEIGHT);
            uman.add(new ProductUnit("N", uman.get("kg"), uman.get("m"), new PowerUnit(uman.get("s"), -2)), "Newton",
                    "Force to accelerate 1 kg of mass by 1 m/s per second", Unit.Category.WEIGHT);

            uman.add(new LinearUnit("lb", 0.45359237, uman.get("kg")), Unit.Category.WEIGHT);
            uman.add(new LinearUnit("gf", 9.80665, new ProductUnit(uman.get("m"), new PowerUnit(uman.get("s"), -2))), Unit.Category.WEIGHT);
            uman.add(new ProductUnit("lbf", uman.get("gf"), uman.get("lb")), Unit.Category.WEIGHT);
            // TODO

            // Miscellaneous
            uman.add(new ProductUnit("J", uman.get("N"), uman.get("m")), "Joule",
                    "A work applied by force of 1 N through a distance of 1 meter");
            uman.add(new LinearUnit("mol", 6.0221412927e23, Unit.NONE), "Mole",
                    "Amount of substance which contains as many elementary entities as 12 grams of pure ¹²C");
            uman.add(new BaseUnit("bit"), "Bit", "Binary digit (0 or 1)");
            uman.add(new LinearUnit("byte", 8, uman.get("bit")), "Byte, a set of 8 bits");
            uman.add(new LinearUnit("cal", 4.1868, uman.get("J")), "Calorie",
                    "Amount of energy to raise temperator of 1 gram of water by 1°C");
            uman.add(new LinearUnit("dyn", 0.01, new ProductUnit(uman.get("g"), uman.get("cm"), new PowerUnit(uman.get("s"), -2))), "Dyne",
                    "A CGS force unit, force to accelerate 1 g of mass by 1 cm/s per second");
            uman.add(new LinearUnit("erg", 1e-7, uman.get("J")), "Erg", "Mechanical work unit in CGS, a work of force of 1 dyne through distance of 1 cm");

            uman.add(new BaseUnit("rad"), "Radian", "Angle cutting an arc from a circle with length equal to circle's radius");
            uman.add(new LinearUnit("deg", Math.PI / 180.0, uman.get("rad")), "Angle degree", "1/360 of full circle");
            uman.add(new BaseUnit("°K"), "Kelvin", "Termodynamic temperature");
            uman.add(new LinearUnit("°C", uman.get("°K"), +273.15), "Celsius",
                    "A 1/100 of scale between water's freezing and boiling points");
            uman.add(new LinearUnit("°F", 9.0/5.0, +32, uman.get("°C")), "Fahrenheit",
                    "A 1/96 of scale between brine's freezing point and normal human body temperature");
            // TODO

            // Electric
            uman.add(new BaseUnit("A"), "Ampere", Unit.Category.ELECTRIC);
            uman.add(new ProductUnit("W", uman.get("J"), new PowerUnit(uman.get("s"), -1)), "Watt",
                    "A work of 1 Joule per 1 second", Unit.Category.ELECTRIC);
            uman.add(new ProductUnit("V", uman.get("W"), new PowerUnit(uman.get("A"), -1)), "Volt",
                    "An electric potential when current of 1 A dissipates energy of 1 Watt", Unit.Category.ELECTRIC);
            uman.add(new ProductUnit("ohm", uman.get("V"), new PowerUnit(uman.get("A"), -1)),
                    "Ohm, a resistance of a conductor if potential of 1 V applied to it produces current of 1 A", Unit.Category.ELECTRIC);
            uman.add(new ProductUnit("S", uman.get("A"), new PowerUnit(uman.get("V"), -1)),
                    "Siemens, a conductance of a conductor if being under current of 1 A it has potential diffirence of 1 V on its ends", Unit.Category.ELECTRIC);
            uman.add(new PowerUnit("Hz", uman.get("s"), -1),
                    "Herz, number of occilations per second", Unit.Category.ELECTRIC);
            // TODO

        } catch (UnitsManager.UnitExistsException e) {
        }

        return uman;
    }
}
