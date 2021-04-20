package android.example.quakereports;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class quakeAdapter extends ArrayAdapter {
    public int getMagnitudeColor(String Magnitude)
    {
        int magnitudeColorResourceId;
        int magnitudeFloor=(int)Double.parseDouble(Magnitude);
        switch(magnitudeFloor)
        {
            case 0:
            case 1:
                magnitudeColorResourceId=R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId=R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId=R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId=R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId=R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId=R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId=R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId=R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId=R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magnitudeColorResourceId);
    }
    public quakeAdapter(Context context, List<Earthquake> quakes)
    {
        super(context,0,quakes);
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View listView=convertView;
        if(listView==null)
        {
            listView= LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_item,parent,false);
        }
        Earthquake currentEarthquake=(Earthquake) getItem(position);
        TextView magnitudeView=(TextView)listView.findViewById(R.id.magnitude);
        magnitudeView.setText(currentEarthquake.getMagnitude());

        String raw=currentEarthquake.getLocation();
        TextView offsetLocationView=(TextView)listView.findViewById(R.id.location_offset);
        if(raw.contains(" of"))
        {
            String a,b;
            TextView primaryLocation=(TextView)listView.findViewById(R.id.primary_location);
            int i=raw.indexOf(" of");
            a=raw.substring(0,i);
            a=a+" OF";
            b=raw.substring(i+4,raw.length());//? 3 =1+1 2 for "of" other one for the space left after "of"
            b=" "+b;
            offsetLocationView.setText(a);
            primaryLocation.setText(b);
        }
        else
        {
            TextView primaryLocation=(TextView)listView.findViewById(R.id.primary_location);
            primaryLocation.setText(raw);
        }
        TextView dateView=(TextView)listView.findViewById(R.id.date);
        dateView.setText(currentEarthquake.getDate());
        TextView timeView=(TextView)listView.findViewById(R.id.time);
        timeView.setText(currentEarthquake.getTime());
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listView;
    }
}
