package cs.b07.drflights;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.Activity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DisplayItineraryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DisplayItineraryFragment#} factory method to
 * create an instance of this fragment.
 */
public class DisplayItineraryFragment extends Fragment {
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<String> currentFlights;


    private OnFragmentInteractionListener mListener;


    public DisplayItineraryFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> retrievedInfo;
        final ArrayAdapter<String> InfoAdapter;
        InfoAdapter = new ArrayAdapter<String>(
                getActivity(),//The current context(this activity)
                R.layout.list_item_flights,//The name of the layout.ID
                R.id.list_item_flights_textview, // The ID of the textview to populate.
                currentFlights);
        View rootView = inflater.inflate(R.layout.fragment_display_flights, container, false);

        //Get a reference to the ListView and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_flights);

        listView.setAdapter(InfoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String notify = "Please tap and hold to book this BookedItineraryActivity";
                String bookedItinerary = currentFlights.get(position);

                //DisplayItineraries.bookThisItinerary();
                Toast.makeText(getActivity(), notify, Toast.LENGTH_SHORT).show();

            }
        });




        return rootView;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



}

