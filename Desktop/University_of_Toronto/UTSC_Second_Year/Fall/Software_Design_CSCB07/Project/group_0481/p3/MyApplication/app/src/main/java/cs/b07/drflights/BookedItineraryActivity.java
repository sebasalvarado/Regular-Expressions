package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import driver.Driver;
import managers.ItineraryManager;
import trip.Itinerary;
import user.Client;

import java.util.ArrayList;

public class BookedItineraryActivity extends AppCompatActivity {

  ListView listview;
  Client receivedClient;
  ArrayAdapter<String> infoAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_booked_itinerary);
    listview = (ListView)findViewById(R.id.lv_booked_itineraries);
    Intent intent = getIntent();
    receivedClient = (Client)intent.getSerializableExtra(ClientInfoActivity.SENT_CLIENT_SEARCH);
    // find itineraries
    ItineraryManager manager = Driver.currDatabase.getItineraryWriter();
    ArrayList<Itinerary> itineraries = manager.getInformation().get(receivedClient.getEmail());
    if (itineraries != null) {
      ArrayList<String> stringItineraries = Search.listItinerariesToString(itineraries);
      infoAdapter = new ArrayAdapter<>(
            this,
            R.layout.fragment_item,
            stringItineraries
      );
      listview.setAdapter(infoAdapter);
    }
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
  }
}
