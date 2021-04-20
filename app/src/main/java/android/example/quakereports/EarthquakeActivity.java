package android.example.quakereports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private quakeAdapter mAdapter;

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";
    private static final int EarthQuake_Id=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView earthquakeListView=(ListView)findViewById(R.id.list);
        mAdapter= new quakeAdapter(this,new ArrayList<Earthquake>());
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Earthquake currentquake=(Earthquake) mAdapter.getItem(position);
                Uri earthQuakeUri=Uri.parse(currentquake.getUrl());
                Intent i=new Intent(Intent.ACTION_VIEW,earthQuakeUri);
                startActivity(i);
            }
        });
        android.app.LoaderManager loaderManager=getLoaderManager();
        loaderManager.initLoader(EarthQuake_Id,null,this);
    }

    @NonNull
    @Override
    public EarthQuakeLoader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        return new EarthQuakeLoader(this,USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        mAdapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty())
        {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Earthquake>> loader) {
        mAdapter.clear();
    }
}