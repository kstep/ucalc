package me.kstep.ucalc.units;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import me.kstep.ucalc.R;
import me.kstep.ucalc.views.UToast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.util.Set;
import android.content.res.Resources;

public abstract class UnitCurrenciesLoader extends AsyncTask<UnitsManager, Void, List<Unit>> {
    final protected Unit baseCurrency;
    final protected Context context;
    final protected boolean useWifiOnly;

    public Context getContext() {
        return context;
    }

    public String getCurrencyFullname(String name, String def) {
        Resources res = getContext().getResources();
        String[] codes = res.getStringArray(R.array.currency_codes);

        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(name)) {
                return res.getStringArray(R.array.currency_names)[i];
            }
        }

        return def;
    }

    public String getCurrencyFullname(String name) {
        return getCurrencyFullname(name, "");
    }

    protected abstract List<Unit> readStream(XmlPullParser parser) throws XmlPullParserException, IOException;
    protected abstract String getStreamURL();
    protected abstract Unit getBaseUnit();

    protected URL getURL() {
        try {
            return new URL(getStreamURL());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public UnitCurrenciesLoader(Context ctx, int timeout, boolean wifiOnly) {
        context = ctx;
        baseCurrency = getBaseUnit();
        cacheTimeout = timeout;
        useWifiOnly = wifiOnly;
    }

    final public UnitsManager load() {
        return load(UnitsManager.getInstance(), null);
    }

    final public UnitsManager load(UnitsManager uman) {
        return load(uman, null);
    }

    final public UnitsManager load(Set<String> filter) {
        return load(UnitsManager.getInstance(), filter);
    }

    private Set<String> unitsFilter;
    public UnitsManager load(UnitsManager uman, Set<String> filter) {
        List<Unit> units;

        // Try to load from cache with default timeout
        units = loadFromCache();

        // Cache failed, try to load from network
        if (units == null) {
            unitsFilter = filter;
            execute(uman);
        } else {
            addAllUnits(uman, units, filter);
        }

        return uman;
    }

    private void addAllUnits(UnitsManager uman, List<Unit> units, Set<String> filter) {
        for (Unit unit : units) {
            if (filter == null || filter.size() == 0 || filter.contains(unit.toString())) {
                uman.add(unit);
            }
        }
    }

    private static final String CACHE_FILENAME = "currencies.cache";
    private int cacheTimeout = 86400;

    protected void saveToCache(List<Unit> units) {
        if (units == null) {
            return;
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getCacheFilename()));
            out.writeObject(units);

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    protected List<Unit> loadFromCache() {
        return loadFromCache(cacheTimeout);
    }

    public void clearCache() {
        new File(getCacheFilename()).delete();
    }

    protected String getCacheFilename() {
        return getContext().getFilesDir().getAbsolutePath() + "/" + getClass().getSimpleName() + "-" + CACHE_FILENAME;
    }

    protected List<Unit> loadFromCache(int timeout) {
        File file = new File(getCacheFilename());
        long fileAgeSec = (System.currentTimeMillis() - file.lastModified()) / 1000;
        if (timeout > 0 && fileAgeSec > timeout) {
            return null;
        }

        return loadFromFile(file);
    }

    protected List<Unit> loadFromFile(String file) {
        return loadFromFile(new File(file));
    }

    protected List<Unit> loadFromFile(File file) {
        try {
            return loadFromStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
        }

        return null;
    }

    protected List<Unit> loadFromStream(InputStream in) {
        try {
            ObjectInputStream io = new ObjectInputStream(in);
            return (List<Unit>) io.readObject();

        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }

        return null;
    }

    protected XmlPullParser getXmlParser() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        XmlPullParser parser = factory.newPullParser();
        return parser;
    }

    protected List<Unit> loadFromNetwork() {
        List<Unit> units = null;

        try {
            InputStream in = openStream();

            XmlPullParser parser = getXmlParser();
            parser.setInput(in, null);
            parser.nextTag();

            units = readStream(parser);

        } catch (XmlPullParserException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        if (units != null) {
            units.add(0, baseCurrency);
        }

        return units;
    }

    protected InputStream openStream() throws IOException {
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = connMgr.getActiveNetworkInfo();

        if (netinfo == null || !netinfo.isConnected()) {
            throw new IllegalStateException();
        }

        if (useWifiOnly) {
            switch (netinfo.getType()) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_ETHERNET:
            break;

            default:
            throw new IllegalStateException();
            }
        }

        HttpURLConnection conn = (HttpURLConnection) getURL().openConnection();
        conn.setReadTimeout(10000 /*ms*/);
        conn.setConnectTimeout(15000 /*ms*/);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    protected String readString(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    protected long readLong(XmlPullParser parser) throws XmlPullParserException, IOException {
        return Long.parseLong(readString(parser), 10);
    }

    protected double readDouble(XmlPullParser parser) throws XmlPullParserException, IOException {
        return Double.parseDouble(readString(parser));
    }

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        for (int depth = 1; depth > 0;) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG: depth--; break;
            case XmlPullParser.START_TAG: depth++; break;
            }
        }
    }

    protected List<Unit> doInBackground(UnitsManager... params) {
        List<Unit> units = loadFromNetwork();
        if (units == null) {
            units = loadFromCache(0);
            cancel(false);
        } else {
            saveToCache(units);
        }

        if (units != null) {
            addAllUnits(params[0], units, unitsFilter);
        }

        return units;
    }

    @Override
    protected void onPreExecute() {
        UToast.show(getContext(), R.string.info_currencies_loading_started, UToast.LENGTH_LONG);
    }

    @Override
    protected void onPostExecute(List<Unit> units) {
        String message = getContext().getResources().getString(R.string.info_currencies_loading_succeed, units == null? 0: units.size());
        UToast.show(getContext(), message, UToast.LENGTH_LONG);
    }

    @Override
    protected void onCancelled(List<Unit> units) {
        String message;

        if (units == null) {
            message = getContext().getResources().getString(R.string.err_currencies_loading_failed);
        } else {
            message = getContext().getResources().getString(R.string.err_currencies_loaded_from_cache, units.size());
        }

        UToast.show(getContext(), message, UToast.LENGTH_LONG);
    }
}
