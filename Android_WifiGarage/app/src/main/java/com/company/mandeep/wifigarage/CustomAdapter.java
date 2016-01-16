package com.company.mandeep.wifigarage;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mandeep on 1/14/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";
    private List<ItemsFragment.ItemDataSet> mDataSet;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    //This is the clas which acts as the 'view' for each item in the list
    public static class ViewHolder extends RecyclerView.ViewHolder { //This is the view which is returned

        //The items which will show up as content for each item
        private final TextView deviceName;

        //private final Switch switchView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            deviceName = (TextView) v.findViewById(R.id.deviceName);
        }

        public TextView getTextView() {
            return deviceName;
        }
        //public Switch getSwitch() { return switchView;}
    }

    public CustomAdapter(List<ItemsFragment.ItemDataSet> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_template, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position).itemName);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
       if(mDataSet==null)
           return 0;
        else
           return mDataSet.size();
    }
}
