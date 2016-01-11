package cs.b07.drflights.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs.b07.drflights.ClientInfoActivity;
import cs.b07.drflights.EditFlightInfoActivity;
import cs.b07.drflights.LoginActivity;
import cs.b07.drflights.R;
import cs.b07.drflights.Search;
import driver.Driver;
import managers.PasswordManager;
import trip.Flight;
import user.Administrator;
import user.Client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class AdminInfoActivity extends AppCompatActivity implements View.OnClickListener {
    
  public static final String CLIENT_INFO = "com.b07.drflights.ClientInfo";
  public static final String FLIGHT_INFO = "cs.b07.drflights.AdminInfoActivity.FlightInfo";
  EditText clientEmail;
  EditText flightNum;
  EditText flightFileView;
  EditText clientFileView;
  Button uploadFlights;
  Button uploadClient;
  Button editClient;
  Button editFlight;
  Button search;
  Administrator admin;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_info);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    admin = new Administrator();
    // Finding the reference to each one of the layout objects.
    flightFileView = (EditText) findViewById(R.id.upload_flight_text);
    clientFileView = (EditText) findViewById(R.id.upload_client_text);
    clientEmail = (EditText) findViewById(R.id.client_email);
    flightNum = (EditText) findViewById(R.id.flight_num);
    uploadFlights = (Button)findViewById(R.id.bupdateFlights);
    uploadClient = (Button) findViewById(R.id.bupdateClient);
    editClient = (Button) findViewById(R.id.edit_client_info);
    editFlight = (Button) findViewById(R.id.edit_flight_info);
    search = (Button) findViewById(R.id.search_itineraries);
    // set the onClick listener
    uploadFlights.setOnClickListener(this);
    uploadClient.setOnClickListener(this);
    editClient.setOnClickListener(this);
    editFlight.setOnClickListener(this);
    search.setOnClickListener(this);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        }
      });
  }

  /** On Click method.
  * @param view View that the view would take you.
  */
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.bupdateFlights:
        uploadFlightInfo();
        break;
      case R.id.bupdateClient:
        uploadClientInfo();
        break;
      case R.id.edit_client_info:
        goClientPage(view);
        break;
      case R.id.edit_flight_info:
        editFlight();
        break;
      case R.id.search_itineraries:
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }

  //Upload flight info from a csv file.
  private void uploadFlightInfo() {
    //FlightManager manager = Driver.currDatabase.getFlightWriter();
    String flightFileName = flightFileView.getText().toString();
    File flightData = this.getApplicationContext().getDir(LoginActivity.USER_DATA_DIR,
        MODE_PRIVATE);

    File flightsFile = new File(flightData, flightFileName);
    String flightFilePath = flightsFile.getPath();
    try {
      // this will add to the current manager what there is in the other
      // manager.readFromCsvFile(flightFilePath);
      //manager.saveToFile();
      //Toast to indicate file uploaded successfully
      admin.updateFlightManager(flightFilePath);
      Toast successfulUpload = Toast.makeText(getApplicationContext(),
            "This flight file uploaded correctly", Toast.LENGTH_LONG);
      successfulUpload.show();
    } catch (IOException e) {
      Toast messageError = Toast.makeText(getApplicationContext(),
            "This flight file did not uploaded correctly", Toast.LENGTH_LONG);
      messageError.show();

      e.printStackTrace();
    }
    // now make sure to regenerate the itineraries and save them in Database
    try {
      File mainFlightData = this.getApplicationContext().getDir(LoginActivity.USER_DATA_DIR,
            MODE_PRIVATE);
      File mainFlightsFile = new File(mainFlightData, LoginActivity.FLIGHTS_FILE);
      admin.uploadFlightInformation(mainFlightsFile.getPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //This is an onClick
  private void uploadClientInfo() {
    //ClientManager manager = Driver.currDatabase.getClientWriter();
    String clientFileName = clientFileView.getText().toString();
    File clientData = this.getApplicationContext().getDir(LoginActivity.USER_DATA_DIR,
        MODE_PRIVATE);
    File clientsFile = new File(clientData, clientFileName);
    String clientFilePath = clientsFile.getPath();
    try {
      // this will add to the current manager what there is in the other
      //manager.readFromCsvFile(clientFilePath);
      //manager.saveToFile();
      admin.updateClientManager(clientFilePath);
      // now give a password to every new client created and save it into the textfile
      ArrayList<String> newClients = admin.getNewClients();
      PasswordManager passManager = Driver.currDatabase.getClientPasswordWriter();
      // writes the newly created clients to password file
      writeNewClientsPassword(newClients, passManager);
      //Toast to indicate file uploaded successfully
      Toast successfulUpload = Toast.makeText(getApplicationContext(),
            "This client file uploaded correctly", Toast.LENGTH_LONG);
      successfulUpload.show();
    } catch (IOException e) {
      Toast messageError = Toast.makeText(getApplicationContext(),
            "This client file did not uploaded correctly", Toast.LENGTH_LONG);
      messageError.show();
      e.printStackTrace();
    }
    // now make sure to regenerate the clients and save them in Database
    try {
      File mainClientData = this.getApplicationContext().getDir(LoginActivity.USER_DATA_DIR,
            MODE_PRIVATE);
      File mainClientsFile = new File(mainClientData, LoginActivity.CLIENTS_FILE);
      admin.uploadClientInformation(mainClientsFile.getPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void editFlight() {
    String flightNumber = flightNum.getText().toString();
    Flight foundFlight = Driver.currDatabase.getFlight(flightNumber);
    if (foundFlight == null) {
      Toast successfulUpload = Toast.makeText(getApplicationContext(),
            "Flight not found", Toast.LENGTH_LONG);
      successfulUpload.show();
    } else {
      Intent intent = new Intent(this, EditFlightInfoActivity.class);
      intent.putExtra(FLIGHT_INFO,foundFlight);
      startActivity(intent);
    }
  }

  /** Called when the going to edit a Client.
  */
  public void goClientPage(View view) {
    String emailClient = clientEmail.getText().toString();
    Client thisClient = Driver.currDatabase.getClient(emailClient);
    if (thisClient == null) {
      Toast.makeText(getApplicationContext(), "Client Not yet Registered",
            Toast.LENGTH_SHORT).show();
    } else {
      // create the intent and pass it to the ClientInfoActivity
      Intent intent = new Intent(this, ClientInfoActivity.class);
      //intent.putExtra(EDIT_FLIGHT,"Client");
      intent.putExtra(CLIENT_INFO,thisClient);
      startActivity(intent);
    }
  }

  /** Method that writes the password for a list of Clients
  * @param newClients List of clients
  * @param passwordManager Password Manager.
  */
  private void writeNewClientsPassword(ArrayList<String> newClients,
                                       PasswordManager passwordManager) {
    HashMap<String, String> passwords = passwordManager.getInformation();
    // add each one of the clients to the password manager
    for (String nextEmail: newClients) {
      Client nextClient = Driver.currDatabase.getClient(nextEmail);
      if (nextClient != null) {
        // MAP EMAIL TO ITS NAME AS DEFAULT PASSWORD
        passwords.put(nextClient.getEmail(),nextClient.getFirstNames());
      }
    }
    passwordManager.saveToFile();
  }
}
