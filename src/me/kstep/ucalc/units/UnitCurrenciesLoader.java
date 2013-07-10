package me.kstep.ucalc.units;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.os.AsyncTask;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import me.kstep.ucalc.R;

public abstract class UnitCurrenciesLoader extends AsyncTask<UnitsManager, Void, UnitsManager> {
    final protected Unit baseCurrency;
    final protected Context context;
    final protected boolean useWifiOnly;

    public Context getContext() {
        return context;
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

    public UnitCurrenciesLoader(Context ctx, boolean wifiOnly) {
        baseCurrency = getBaseUnit();
        context = ctx;
        useWifiOnly = wifiOnly;
    }

    final public UnitsManager load() {
        return load(UnitsManager.getInstance());
    }

    public UnitsManager load(UnitsManager uman) {
        uman.add(baseCurrency);

        List<Unit> units;

        // Try to load from cache with default timeout
        units = loadFromCache();

        // Cache failed, try to load from network
        if (units == null) {
            units = loadFromNetwork();

            // Network failed, try to force load from cache
            if (units == null) {
                units = loadFromCache(0);
            } else {
                saveToCache(units);
            }
        }

        // If loading succeeded, put units to units manager
        if (units != null) {
            uman.addAll(units);
        } else {
            Toast.makeText(getContext(), R.string.err_currencies_loading_failed, Toast.LENGTH_SHORT).show();
        }

        return uman;
    }

    private static final String CACHE_FILENAME = "currencies.cache";
    private static final int CACHE_DEFAULT_TIMEOUT = 86400;

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
        return loadFromCache(CACHE_DEFAULT_TIMEOUT);
    }

    protected String getCacheFilename() {
        return getContext().getFilesDir().getAbsolutePath() + "/" + CACHE_FILENAME;
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
        try {
            InputStream in = openStream();

            XmlPullParser parser = getXmlParser();
            parser.setInput(in, null);
            parser.nextTag();

            return readStream(parser);

        } catch (XmlPullParserException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        return null;
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

    protected UnitsManager doInBackground(UnitsManager... params) {
        return load(params[0]);
    }

}
