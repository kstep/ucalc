package me.kstep.ucalc.units;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.util.List;
import java.util.LinkedList;
import android.content.Context;

import me.kstep.ucalc.numbers.UNumber;

public class NBRBCurrenciesLoader extends UnitCurrenciesLoader {

    public NBRBCurrenciesLoader(Context ctx, int timeout, boolean wifiOnly) {
        super(ctx, timeout, wifiOnly);
    }

    @Override
    protected Unit getBaseUnit() {
        Unit unit = new BaseUnit("BYR");
        unit.category = Unit.Category.MISCELLANEOUS;
        unit.fullname = "Belarussian ruble";
        return unit;
    }

    @Override
    protected String getStreamURL() {
        return "http://nbrb.by/Services/XmlExRates.aspx";
    }

    @Override
    protected List<Unit> readStream(XmlPullParser parser) throws XmlPullParserException, IOException {
        LinkedList<Unit> result = new LinkedList<Unit>();

        parser.require(XmlPullParser.START_TAG, null, "DailyExRates");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }

            if (parser.getName().equals("Currency")) {
                result.add(readUnit(parser));
            } else {
                skip(parser);
            }
        }

        return result;
    }

    protected Unit readUnit(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Currency");

        String cName = "";
        String cFullname = "";
        long cScale = 1;
        double cRate = 1;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }

            String name = parser.getName();

            if (name.equals("CharCode")) {
                cName = readString(parser);
                parser.require(XmlPullParser.END_TAG, null, "CharCode");

            } else if (name.equals("Scale")) {
                cScale = readLong(parser);
                parser.require(XmlPullParser.END_TAG, null, "Scale");

            } else if (name.equals("Rate")) {
                cRate = readDouble(parser);
                parser.require(XmlPullParser.END_TAG, null, "Rate");

            } else if (name.equals("Name")) {
                cFullname = readString(parser);
                parser.require(XmlPullParser.END_TAG, null, "Name");

            } else {
                skip(parser);
            }
        }

        Unit unit = new LinearUnit(cName, cRate / cScale, baseCurrency);
        unit.fullname = cFullname;
        unit.category = Unit.Category.MISCELLANEOUS;
        return unit;
    }
}
