package me.kstep.ucalc.units;

import android.content.Context;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import me.kstep.ucalc.numbers.UNumber;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CBRCurrenciesLoader  extends UnitCurrenciesLoader {

    public CBRCurrenciesLoader(Context ctx, int timeout, boolean wifiOnly) {
        super(ctx, timeout, wifiOnly);
    }

    @Override
    protected Unit getBaseUnit() {
        Unit unit = new BaseUnit("RUR");
        unit.category = Unit.Category.MISCELLANEOUS;
        unit.fullname = "Russian ruble";
        return unit;
    }

    @Override
    protected String getStreamURL() {
        return "http://www.cbr.ru/scripts/XML_daily.asp";
    }

    @Override
    protected List<Unit> readStream(XmlPullParser parser) throws XmlPullParserException, IOException {
        LinkedList<Unit> result = new LinkedList<Unit>();

        parser.require(XmlPullParser.START_TAG, null, "ValCurs");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }

            if (parser.getName().equals("Valute")) {
                result.add(readUnit(parser));
            } else {
                skip(parser);
            }
        }

        return result;
    }

    protected Unit readUnit(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Valute");

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

            } else if (name.equals("Nominal")) {
                cScale = readLong(parser);
                parser.require(XmlPullParser.END_TAG, null, "Nominal");

            } else if (name.equals("Value")) {
                cRate = readDouble(parser);
                parser.require(XmlPullParser.END_TAG, null, "Value");

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

    @Override
    protected double readDouble(XmlPullParser parser) throws XmlPullParserException, IOException {
        return Double.parseDouble(readString(parser).replace(',', '.'));
    }
}
