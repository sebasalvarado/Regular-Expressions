package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;

import user.Client;

import java.util.ArrayList;
import java.util.Arrays;

public class ClientInfoActivity extends AppCompatActivity implements View.OnClickListener {
  public static final String SENT_CLIENT_PASSWORD = "com.b07.drflights.ChangePasswordActivity";
  public static final String SENT_CLIENT_SEARCH = "com.b07.drflights.Search";
  public static final String SENT_CLIENT_EDIT = "com.b07.drflights.EditClientInfoActivity";

  Button buttonItinerarires;
  Button buttonSearch;
  Button buttonEditClient;
  Button buttonChangePassword;
  Button buttonLogout;
  Client receivedClient;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_info);
    Intent intent = getIntent();
    receivedClient = (Client) intent.getSerializableExtra(LoginActivity.CLIENT_INFO);

    ArrayList<String> personalInfo = parseInfo(receivedClient.toString());
    // set the information into the fragment
    //add the fragment to the activity
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ClientInfoFragment(personalInfo))
                .commit();
    }
    // Finding reference to each of the buttons
    buttonItinerarires = (Button) findViewById(R.id.bItineraries);
    buttonSearch = (Button) findViewById(R.id.bSearch);
    buttonEditClient = (Button) findViewById(R.id.bEditClient);
    buttonChangePassword = (Button) findViewById(R.id.bChangePassword);
    buttonLogout = (Button) findViewById(R.id.log_out);

    buttonItinerarires.setOnClickListener(this);
    buttonSearch.setOnClickListener(this);
    buttonEditClient.setOnClickListener(this);
    buttonChangePassword.setOnClickListener(this);
    buttonLogout.setOnClickListener(this);
    Toast.makeText(getBaseContext(), "Welcome " + receivedClient.getFirstNames()
            + " ! Start your journey", Toast.LENGTH_SHORT).show();
  }

  /**
  * OnResume method.
  */
  public void onResume() {
    super.onResume();
    Intent intent = getIntent();
    receivedClient = (Client) intent.getSerializableExtra(Search.CLIENT_BACK);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_client_info,menu);
    return true;
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.bItineraries:
        Intent intent = new Intent(this, BookedItineraryActivity.class);
        intent.putExtra(SENT_CLIENT_SEARCH,receivedClient);
        startActivity(intent);
        break;
      case R.id.bSearch:
        Intent searchIntent = new Intent(this, Search.class);
        searchIntent.putExtra(SENT_CLIENT_SEARCH, receivedClient);
        startActivity(searchIntent);
        break;
      case R.id.bEditClient:
        Intent editIntent = new Intent(this, EditClientInfoActivity.class);
        editIntent.putExtra(SENT_CLIENT_EDIT, receivedClient);
        startActivity(editIntent);
        break;
      case R.id.bChangePassword:
        Intent intent1 = new Intent(this,ChangePasswordActivity.class);
        intent1.putExtra(SENT_CLIENT_PASSWORD,receivedClient);
        startActivity(intent1);
        break;
      case R.id.log_out:
        Intent logoutIntent = new Intent(this, LoginActivity.class);
        startActivity(logoutIntent);
        break;
      default:
        break;
    }
  }

  private ArrayList<String> parseInfo(String clientInfo) {
    String[] parsedInfo = clientInfo.split(",");
    System.out.println(clientInfo);
    String[] prefixes = new String[]{"Last Name: ", "Name: ","Email: ", "Address: ",};
    ArrayList<String> clientInfoList = new ArrayList<>(Arrays.asList(parsedInfo));
    // we will not want to display info about credit card so remove from list
    clientInfoList.remove(clientInfoList.size() - 1);
    clientInfoList.remove(clientInfoList.size() - 1);
    int number = 0;
    ArrayList<String> finalList = new ArrayList<>();
    for (String nextField:clientInfoList) {
      String newField = prefixes[number] + nextField;
      finalList.add(newField);
      number++;
    }
    return finalList;
  }
}
