package android.example.quakereports;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static URL createUrl(String stringUrl){
        //?returns new url object form the given string url
        URL url=null;
        try{
            url=new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"PROBLEM BUILDING THE URL",e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url)throws IOException{
        String jsonResponse="";
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try{
            urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);//? these are in milliseconds
            urlConnection.setConnectTimeout(15000);//? these are in milliseconds
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200)
            {
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }
            else
            {
                Log.e(LOG_TAG,"ERROR RESPONSE CODE"+urlConnection.getResponseCode());
            }
        }catch (IOException e)
        {
            Log.e(LOG_TAG,"problem retrieving the earthquake json result",e);
        }
        finally {
            if(urlConnection!=null)
            {
                urlConnection.disconnect();
            }
            if(inputStream!=null)
            {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream)throws IOException{
        StringBuilder output = new StringBuilder(); // output is of the type stringBuilder.
        if(inputStream!=null)
        {
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line!=null)
            {
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();//? need to change stringBuilder to string
    }
    private static List<Earthquake> extractFeatureFromJson(String earthQuakeJSON)
    {
        if(TextUtils.isEmpty(earthQuakeJSON))
        {
            return null;
        }
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject baseJsonResponse=new JSONObject(earthQuakeJSON);
            JSONArray earthQuakeArray=baseJsonResponse.getJSONArray("features");
            for(int i=0;i<earthQuakeArray.length();i++)
            {
                JSONObject currentEarthquake=earthQuakeArray.getJSONObject(i);
                JSONObject properties=currentEarthquake.getJSONObject("properties");
                String magnitude=properties.getString("mag");
                String location=properties.getString("place");

                long timeInMilliseconds=properties.getLong("time");
                Date dateObject=new Date(timeInMilliseconds);
                SimpleDateFormat dateFormatter=new SimpleDateFormat("MMM dd, yyyy");
                String dateToDisplay=dateFormatter.format(dateObject);
                SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
                String timeToDisplay=timeFormat.format(dateObject);

                String url=properties.getString("url");
                Earthquake earthquake=new Earthquake(magnitude,location,dateToDisplay,timeToDisplay,url);
                earthquakes.add(earthquake);
            }
        }
        catch (JSONException e)
        {
            Log.e("QerryUrtills","problem parsing the json result",e);
        }
        return earthquakes;
    }
    public static List<Earthquake> fetchEarthquakeData(String requestUrl)
    {
        URL url= createUrl(requestUrl);
        String jsonResponse=null;
        try
        {
            jsonResponse=makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.e(LOG_TAG,"problem making the Http request.",e);
        }
        List<Earthquake> earthquakes=extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }
}