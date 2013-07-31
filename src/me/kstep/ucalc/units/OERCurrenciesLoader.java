package me.kstep.ucalc.units;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class OERCurrenciesLoader extends UnitCurrenciesLoader {

    public OERCurrenciesLoader(Context ctx, int timeout, boolean wifiOnly) {
        super(ctx, timeout, wifiOnly);
    }

    @Override
    protected Unit getBaseUnit() {
        return Unit.NONE;
    }

    @Override
    protected String getStreamURL() {
        return "http://openexchangerates.org/api/latest.json?app_id=1265e4b3998d4cda947569159b829ed6";
    }

    @Override
    protected List<Unit> readStream(XmlPullParser parser) throws XmlPullParserException, IOException {
        return null;
    }

    @Override
    protected List<Unit> loadFromNetwork() {
        List<Unit> units = null;

        try {
            InputStream in = openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder input = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                input.append(line);
            }

            JSONObject data = new JSONObject(input.toString());

            Unit base = new BaseUnit(data.getString("base"));
            base.category = Unit.Category.MISCELLANEOUS;
            base.fullname = getCurrencyFullname(base.name);

            JSONObject rates = data.getJSONObject("rates");

            units = new ArrayList<Unit>(rates.length() + 1);
            units.add(base);

            @SuppressWarnings("unchecked")
            Iterator<String> currencies = rates.keys();

            while (currencies.hasNext()) {
                String name = currencies.next();
                if (name.equals(base.name)) {
                    continue;
                }

                Unit unit = new LinearUnit(name, 1 / rates.getDouble(name), base);
                unit.category = Unit.Category.MISCELLANEOUS;
                unit.fullname = getCurrencyFullname(name);

                units.add(unit);
            }

        } catch (IOException e) {
        } catch (IllegalStateException e) {
        } catch (JSONException e) {
        }

        return units;
    }
}
