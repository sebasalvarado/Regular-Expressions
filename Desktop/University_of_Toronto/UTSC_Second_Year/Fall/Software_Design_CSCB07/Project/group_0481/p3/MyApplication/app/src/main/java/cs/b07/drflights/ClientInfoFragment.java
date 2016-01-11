package cs.b07.drflights;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClientInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ClientInfoFragment extends Fragment {
  private ArrayList<String> receivedInformation;
  ArrayAdapter<String> infoAdapter;
  OnFragmentInteractionListener myListener;

  public ClientInfoFragment() {
  }

  public ClientInfoFragment(ArrayList<String> receivedInformation) {
    // Required empty public constructor
    this.receivedInformation = receivedInformation;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    infoAdapter = new ArrayAdapter<>(
            getActivity(),
            R.layout.fragment_item,
            R.id.list_item_information,
            receivedInformation);





    View rootView = inflater.inflate(R.layout.fragment_client_info,container,false);
    ListView listView = (ListView)rootView.findViewById(R.id.info_listview);
    System.out.println(listView.toString() + "Empty");
    listView.setAdapter(infoAdapter);
    return rootView;
  }


  @Override
  public void onDetach() {
    super.onDetach();
    myListener = null;
  }

  /**
  * This interface must be implemented by activities that contain this
  * fragment to allow an interaction in this fragment to be communicated
  * to the activity and potentially other fragments contained in that
  * activity.
   *
  * <p>See the Android Training lesson <a href=
  * "http://developer.android.com/training/basics/fragments/communicating.html"
  * >Communicating with Other Fragments</a> for more information.
  */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
