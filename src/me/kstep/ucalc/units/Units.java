package me.kstep.ucalc.units;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Xml;
import android.util.AttributeSet;

import android.content.Context;
import android.content.res.XmlResourceParser;

import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UInteger;

public class Units {
    public static void load() {
        load(UnitsManager.getInstance());
    }

    public static void inflate(Context context, int resource) {
        inflate(context, resource, UnitsManager.getInstance());
    }

    public static void inflate(XmlPullParser parser) {
        inflate(parser, UnitsManager.getInstance());
    }

    public static void inflate(Context context, int resource, UnitsManager uman) {
        XmlResourceParser parser = context.getResources().getXml(resource);
        inflate(parser, uman);
    }

    public static void inflate(XmlPullParser parser, UnitsManager uman) {
        uman.clear();

        try {

            int evt = parser.getEventType();

            while (evt != XmlPullParser.END_DOCUMENT && evt != XmlPullParser.START_TAG) {
                evt = parser.next();
            }

            if (evt != XmlPullParser.START_TAG || !parser.getName().equals("units")) {
                return;
            }

            rInflate(parser, uman, Unit.Category.MISCELLANEOUS);

        } catch (IOException e) {
        } catch (XmlPullParserException e) {
        }

        System.gc();
    }

    private static void rInflate(XmlPullParser parser, UnitsManager uman, Unit.Category category) throws IOException, XmlPullParserException {
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

    private static Unit inflateUnit(XmlPullParser parser, UnitsManager uman) throws IOException, XmlPullParserException {
        final int depth = parser.getDepth();
        int evt;

        final String className = parser.getAttributeValue(null, "class");
        final String unitName = parser.getAttributeValue(null, "name");
        final String fullName = parser.getAttributeValue(null, "fullname");

        android.util.Log.d("UCalc", "Inflating " + String.valueOf(className) + " " + String.valueOf(unitName));

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
                        description = parser.getText();
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
            android.util.Log.d("UCalc", "Base unit");
            unit = new BaseUnit(unitName);

        } else if (className.equals("linear")) {
            android.util.Log.d("UCalc", "Linear unit");

            if (scale != null || offset != null) {
                unit = new LinearUnit(unitName, scale == null? 1: scale, deriveUnits.get(0), offset == null? 0: offset);
            } else if (rscale != null || roffset != null) {
                unit = new LinearUnit(unitName, rscale == null? 1: rscale, roffset == null? 0: roffset, deriveUnits.get(0));
            }

        } else if (className.equals("power")) {
            android.util.Log.d("UCalc", "Power unit");

            if (unitName != null) {
                unit = new PowerUnit(unitName, deriveUnits.get(0), power);
            } else {
                unit = new PowerUnit(deriveUnits.get(0), power);
            }

        } else if (className.equals("product")) {
            android.util.Log.d("UCalc", "Product unit");

            if (unitName != null) {
                unit = new ProductUnit(unitName, deriveUnits.toArray(new Unit[deriveUnits.size()]));
            } else {
                unit = new ProductUnit(deriveUnits.toArray(new Unit[deriveUnits.size()]));
            }
        } else if (className.equals("prefix")) {
            android.util.Log.d("UCalc", "Prefix unit");

            if (unitName != null) {
                unit = new UnitPrefix(unitName, prefix, deriveUnits.get(0));
            } else {
                unit = new UnitPrefix(prefix, deriveUnits.get(0));
            }

        } else {
            android.util.Log.d("UCalc", "Unknown unit class");
        }

        if (unit != Unit.NONE) {
            unit.fullname = fullName;
            unit.description = description;
        }
        android.util.Log.d("UCalc", "Inflated unit " + unit.toString() + " (" + unit.getDescription() + ")");

        return unit;
    }

    public static void load(UnitsManager uman) {
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
    }
}
