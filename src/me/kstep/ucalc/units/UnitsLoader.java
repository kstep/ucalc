package me.kstep.ucalc.units;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import me.kstep.ucalc.numbers.UInteger;
import me.kstep.ucalc.numbers.UNumber;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import me.kstep.ucalc.numbers.URational;

public class UnitsLoader {
    public static UnitsManager loadPrefixes() {
        return loadPrefixes(UnitsManager.getInstance());
    }

    public static UnitsManager loadPrefixes(UnitsManager uman) {
        Unit[] units = UnitPrefix.getPrefixes();
        for (Unit unit : units) {
            uman.add(unit, unit.fullname, Unit.Category.PREFIX);
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

        } catch (UnitsManager.UnitExistsException e) {
        }

        return uman;
    }
}
