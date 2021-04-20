package android.example.quakereports;

import androidx.core.content.ContextCompat;


public class Earthquake {
    private String mMagnitude;
    private String mLocation;
    private String mDate;
    private String mTime;
    private String mUrl;

    public Earthquake(String Magnitude, String Location, String Date,String Time,String url) {
        mMagnitude = Magnitude;
        mLocation = Location;
        mDate = Date;
        mTime=Time;
        mUrl=url;
    }

    public String getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getDate()
    {
        return mDate;
    }

    public String getTime(){return mTime;}

    public String getUrl(){return mUrl;}
}
