package android.example.quakereports;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthQuakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    @Nullable
    private static final String LOG_TAG=EarthQuakeLoader.class.getName();
    private String mUrl;
    public EarthQuakeLoader(Context context,String url)
    {
        super(context);
        mUrl=url;
    }
    protected void onStartLoading(){
        forceLoad();
    }
    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl==null)
        {
            return null;
        }
        List<Earthquake> earthquakes=QueryUtils.fetchEarthquakeData(mUrl);
        return earthquakes;
    }
}
