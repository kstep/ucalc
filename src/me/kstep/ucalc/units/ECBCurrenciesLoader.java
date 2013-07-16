package me.kstep.ucalc.units;

import android.content.Context;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import me.kstep.ucalc.numbers.UNumber;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ECBCurrenciesLoader extends UnitCurrenciesLoader {

    public ECBCurrenciesLoader(Context ctx, int timeout, boolean wifiOnly) {
        super(ctx, timeout, wifiOnly);
    }

    @Override
    protected Unit getBaseUnit() {
        Unit unit = new BaseUnit("EUR");
        unit.category = Unit.Category.MISCELLANEOUS;
        unit.fullname = "Euro";
        return unit;
    }

    @Override
    protected String getStreamURL() {
        return "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    }

    @Override
    protected List<Unit> readStream(XmlPullParser parser) throws XmlPullParserException, IOException {
        LinkedList<Unit> result = new LinkedList<Unit>();

        parser.require(XmlPullParser.START_TAG, null, "gesmes:Envelope");

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }

            if (parser.getName().equals("Cube")) {
                Unit unit = readUnit(parser);
                if (unit != null) {
                    result.add(unit);
                }
            }
        }

        return result;
    }

    protected Unit readUnit(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Cube");

        String cName = null;
        double cRate = 1;

        for (int i = 0, n = parser.getAttributeCount(); i < n; i++) {
            String attrName = parser.getAttributeName(i);
            if (attrName.equals("currency")) {
                cName = parser.getAttributeValue(i);
            } else if (attrName.equals("rate")) {
                cRate = Double.parseDouble(parser.getAttributeValue(i));
            }
        }

        if (cName == null) {
            return null;
        }

        Unit unit = new LinearUnit(cName, 1 / cRate, baseCurrency);
        unit.fullname = cName;
        unit.category = Unit.Category.MISCELLANEOUS;
        return unit;
    }
}
