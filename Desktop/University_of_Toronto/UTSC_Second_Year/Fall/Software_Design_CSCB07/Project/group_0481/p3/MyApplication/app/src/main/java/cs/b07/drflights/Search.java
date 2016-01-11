package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import database.Database;
import driver.Driver;
import trip.Itinerary;
import user.Client;

import java.util.ArrayList;




public class Search extends AppCompatActivity implements View.OnClickListener {
  private RadioGroup radioGroup;
  private RadioButton cost;
  private RadioButton time;
  private Database database;
  private EditText date;
  private EditText origin;
  private EditText destination;
  String curDate;
  String curOrigin;
  String curDestination;
  private Button buttonItineraries;
  public static final String DISPLAY_ITINERARIES = "com.b07.drflights.DisplayItineraries";
  public static final String ITINERARIES_OBJECTS =
          "com.b07.drflights.DisplayItineraries.Itineraries";
  public static final String CLIENT_BACK = "com.b07.drflights.ClientInfo";
  Client receivedClient;

  private ArrayList<String> sentItineraries;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    date = (EditText)findViewById(R.id.date);
    origin = (EditText)findViewById(R.id.etOrigin);
    destination = (EditText)findViewById(R.id.etDestination);
    buttonItineraries = (Button) findViewById(R.id.bSearchItineraries);
    radioGroup = (RadioGroup)findViewById(R.id.myRadioGroup);
    cost = (RadioButton)findViewById(R.id.radio_sort_cost);
    time = (RadioButton)findViewById(R.id.radio_sort_time);
    database = Driver.currDatabase;
    Intent intent = getIntent();
    receivedClient = (Client) intent.getSerializableExtra(ClientInfoActivity.SENT_CLIENT_SEARCH);

  }

  @Override
  public void onResume() {
    super.onResume();
    // create an empy list every time you start again
    buttonItineraries.setOnClickListener(this);
    sentItineraries = new ArrayList<>();
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // check which radio BUTTON was selected
            if (checkedId == R.id.radio_sort_cost) {
              Toast.makeText(getApplicationContext(), "Nice, you will save some money!",
                      Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.radio_sort_time) {
              Toast.makeText(getApplicationContext(), "Nice you will get there on time!",
                      Toast.LENGTH_SHORT).show();
            }
        }
    });
  }


  /** Returns an ArrayList of Itineraries unsorted.Each index has one itinerary in its string
  * format.
  * @param date the date for this itinerary.
  * @param origin the origin of first flight in this itinerary.
  * @param destination the destination of the last flight in this itinerary.
  * @return ArrayList of Strings with each index having a single itinerary.
  */
  public ArrayList<String> listViewUnsortedItineraries(String date, String origin,
                                                       String destination) {
    ArrayList<Itinerary> unsorted = database.getItineraries(date,origin,destination);
    ArrayList<String> itinerariesStringList = new ArrayList<>();
    for (trip.Itinerary curItinerary:unsorted) {
      itinerariesStringList.add(curItinerary.toString());
    }
    return itinerariesStringList;
  }


  /**
  * Pre-Condition: list should be sorted by time or cost.
  * Post-Condition: Returns string representation of each itinerary
  * @param list of Itineraries
  * @return ArrayList of Itineraries
  */
  public static  ArrayList<String> listItinerariesToString(ArrayList<Itinerary> list) {
    ArrayList<String> itinerariesStringList = new ArrayList<>();
    for (trip.Itinerary curItinerary:list) {
      itinerariesStringList.add(curItinerary.toString());
    }
    return  itinerariesStringList;

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.bSearchItineraries:
        // check which Radio Button is being checked
        curDate = date.getText().toString();
        curOrigin = origin.getText().toString();
        curDestination = destination.getText().toString();
        sentItineraries = new ArrayList<>();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == cost.getId()) {
          ArrayList<Itinerary> unsortedList = database.getItineraries(
                  curDate, curOrigin, curDestination);
          ArrayList<Itinerary> costSortedList = database.totalCostSort(unsortedList);
          sentItineraries = listItinerariesToString(costSortedList);
          Intent intent1 = new Intent(this, DisplayItineraries.class);
          intent1.putExtra(DISPLAY_ITINERARIES,sentItineraries);
          intent1.putExtra(ITINERARIES_OBJECTS,costSortedList);
          intent1.putExtra(ClientInfoActivity.SENT_CLIENT_SEARCH,receivedClient);
          startActivity(intent1);
        } else if (selectedId == time.getId()) {
          ArrayList<Itinerary> unsortedList = database.getItineraries(
                  curDate, curOrigin, curDestination);
          ArrayList<Itinerary> timeSortedlist = database.totalTravelTimeSort(unsortedList);
          sentItineraries = listItinerariesToString(timeSortedlist);
          Intent intent2 = new Intent(this, DisplayItineraries.class);
          intent2.putExtra(DISPLAY_ITINERARIES,sentItineraries);
          intent2.putExtra(ITINERARIES_OBJECTS,timeSortedlist);
          intent2.putExtra(ClientInfoActivity.SENT_CLIENT_SEARCH,receivedClient);
          startActivity(intent2);
        } else {
          sentItineraries = listViewUnsortedItineraries(curDate, curOrigin, curDestination);
          ArrayList<Itinerary> unsorted = database.getItineraries(curDate,curOrigin,curDestination);
          Intent newIntent = new Intent(this, DisplayItineraries.class);
          newIntent.putExtra(DISPLAY_ITINERARIES,sentItineraries);
          newIntent.putExtra(ITINERARIES_OBJECTS,unsorted);
          newIntent.putExtra(ClientInfoActivity.SENT_CLIENT_SEARCH,receivedClient);
          startActivity(newIntent);
        }
        break;
      default:
        break;
    }
  }


  @Override
  public void onBackPressed() {
    Intent intent = new Intent(this, ClientInfoActivity.class);
    intent.putExtra(Search.CLIENT_BACK,receivedClient);
    startActivity(intent);
  }
}
