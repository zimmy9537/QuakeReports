package android.example.quakereports;


import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

public class myAsyncTaskLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrl=null;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl==null)
        {
            return null;
        }
        return QueryUtils.fetchEarthquakeData(mUrl);
    }

    public myAsyncTaskLoader(Context context, String url)
    {
        super(context);
        mUrl=url;
    }
}
