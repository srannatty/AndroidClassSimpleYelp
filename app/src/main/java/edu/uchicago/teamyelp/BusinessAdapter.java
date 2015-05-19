package edu.uchicago.teamyelp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yelp.v2.Business;

import java.util.List;

/**
 * Created by jennifer1 on 5/18/15.
 */
public class BusinessAdapter extends ArrayAdapter<Business> {


    public BusinessAdapter(Context context, List<Business> businesses) {
        super(context, 0, businesses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Business business = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.yelp_row, parent, false);
        }
        // Lookup view for data population
        TextView BusinessName = (TextView) convertView.findViewById(R.id.row_text);

        // Populate the data into the template view using the data object
        BusinessName.setText(business.getName());

        // Return the completed view to render on screen
        return convertView;
    }


}
