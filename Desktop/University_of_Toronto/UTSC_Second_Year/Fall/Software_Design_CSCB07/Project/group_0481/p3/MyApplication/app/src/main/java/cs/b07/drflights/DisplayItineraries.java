package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import driver.Driver;
import managers.ItineraryManager;
import trip.Flight;
import trip.Itinerary;
import user.Client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class DisplayItineraries extends AppCompatActivity {

  private ArrayList<Itinerary> itineraryObjects  = new ArrayList<>();
  ArrayList<String> searchInformation;
  private ArrayAdapter<String> infoAdapter;
  private ListView listView;
  private Client receivedClient;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_display_itineraries);
    listView = (ListView) findViewById(R.id.lv_display_itineraries);

  }

  @Override
  protected void onResume() {
    super.onResume();
    Intent intent = getIntent();
    searchInformation = intent.getStringArrayListExtra(Search.DISPLAY_ITINERARIES);
    receivedClient = (Client) intent.getSerializableExtra(ClientInfoActivity.SENT_CLIENT_SEARCH);
    itineraryObjects = (ArrayList<Itinerary>)
            intent.getSerializableExtra(Search.ITINERARIES_OBJECTS);
    intent.removeExtra(Search.DISPLAY_ITINERARIES);
    //deleteRepeated(searchInformation);
    infoAdapter = new ArrayAdapter<>(
            this,
            R.layout.fragment_item,
            searchInformation
    );
    listView.setAdapter(infoAdapter);
    listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View curView, int position, long id) {
        if (bookThisItinerary(itineraryObjects.get(position))) {
            Intent intent1 = new Intent(getApplicationContext(),Search.class);
            Toast.makeText(getBaseContext(), "Item "
                    + (parent.getItemIdAtPosition(position) + 1) + " successfully booked.",
                    Toast.LENGTH_SHORT).show();
            intent1.putExtra(ClientInfoActivity.SENT_CLIENT_SEARCH,receivedClient);
            startActivity(intent1);

        } else {
            Toast.makeText(getBaseContext(), "Item "
                    + (parent.getItemIdAtPosition(position) + 1) + " has no space currently!",
                    Toast.LENGTH_SHORT).show();

        }

        }
    });
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    infoAdapter.notifyDataSetChanged();
  }

  @Override
  public void onPause() {
    super.onPause();
    Intent intent = getIntent();
    intent.removeExtra(Search.DISPLAY_ITINERARIES);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_display_itineraries, menu);
    return true;
  }

  /**
  *  Method that books a given itinerary.
  * @param curItinerary The current Itinerary to be booked
  * @return return whether the itinerary was book or not.
  */
  public boolean bookThisItinerary(Itinerary curItinerary) {
    System.out.println(curItinerary.getAvailableSeats());
    if (curItinerary.getAvailableSeats() > 0) {
      ItineraryManager itineraryManager = Driver.currDatabase.getItineraryWriter();
      itineraryManager.add(curItinerary, receivedClient);
      itineraryManager.saveToFile();
      LinkedList<Flight> allFlights = curItinerary.getListOfFlights();
      Driver.currDatabase.bookedItinerary(allFlights);
      File mainFlightData = this.getApplicationContext().getDir(LoginActivity.USER_DATA_DIR,
            MODE_PRIVATE);
      File mainFlightsFile = new File(mainFlightData, LoginActivity.FLIGHTS_FILE);
      try {
        Driver.currDatabase.uploadFlightInfo(mainFlightsFile.getPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
      return true;
    } else {
      return false;

    }

  }

  /**
  * OnBackPresser.
  */
  public void onBackPressed() {
    this.listView.invalidateViews();
    infoAdapter.notifyDataSetChanged();
    super.onBackPressed();
  }
}

