package com.company.mandeep.wifigarage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.Device;
import com.company.mandeep.wifigarage.com.company.mandeep.ParseObjects.SparkDevice;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsFragment extends Fragment {

    public class ItemDataSet{
        public String itemName;
        public boolean itemStatus;
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ItemsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final String ARG_PAGE = "ARG_PAGE"; //This is how we will know what tab we are on
    private int mPage;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    //protected String[] myDataSet; //This is the data set which will update the items in the rescyler view
    protected ArrayList<ItemDataSet> myDataSet;

    public ItemsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page); //assign the tab number to this new fragment

        //Create a new fragment (Tab)
        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE); //When we create this fragment, we want to return the page number (tab number)

        //Update the data set here

        //Get the number of items for the user


        /*myDataSet = new String[10];
        for (int i = 0; i < 10; i++) {
            myDataSet[i] = "This is element #" + i;
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_tab_items, container, false);

        //Find the recycler view
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        //This is a customer adapter to bind the items to the recycler view
        myDataSet=new ArrayList<ItemDataSet>();
        mAdapter = new CustomAdapter(myDataSet);
        mRecyclerView.setAdapter(mAdapter);

        //Update the adapter to indicate we have added devices to the view
        ParseQuery<Device> query = ParseQuery.getQuery(Device.class);
        query.whereEqualTo("name","Wifi Garage");
        query.findInBackground(new FindCallback<Device>() {
            @Override
            public void done(List<Device> list, ParseException e) {
                for(Device d : list) {
                    ItemDataSet item = new ItemDataSet();
                    item.itemName = d.getDisplayName();
                    myDataSet.add(item);

                    mAdapter.notifyDataSetChanged();
                }

                Log.d(TAG, "done: " + list.size());
            }
        });

        return rootView;
    }
}
