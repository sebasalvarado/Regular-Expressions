package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs.b07.drflights.admin.AdminInfoActivity;
import driver.Driver;
import user.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

  public static final String CLIENTS_FILE = "clients_main.txt";
  public static final String FLIGHTS_FILE = "flights_main.txt";
  public static final String USER_DATA_DIR = "records_data";
  public static final String PASSWORDS_FILE = "passwords.txt";
  public static final String ITINERARIES_STORING = "itineraries_booked.txt";
  public static final String CLIENT_PASSWORDS_FILE = "client_passwords.txt";



  /**
  * Id to identity READ_CONTACTS permission request.
  */
  public static final String CLIENT_INFO = "com.b07.drflights.ClientInfo";
  private HashMap<String, Client> clientDatabase;
  private static Map<String, String> clientCredentials = new HashMap<>();
  private static Map<String, String> adminCredentials = new HashMap<>();
  // UI references.
  EditText emailView;
  EditText passwordView;
  Button signInButton;
  Button registerButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    // Getting references for UI elements.
    emailView = (EditText) findViewById(R.id.email);
    passwordView = (EditText) findViewById(R.id.password);
    signInButton = (Button) findViewById(R.id.email_sign_in_button);
    registerButton = (Button) findViewById(R.id.register_button);
    signInButton.setOnClickListener(this);
    registerButton.setOnClickListener(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    File informationData = this.getApplicationContext().getDir(USER_DATA_DIR, MODE_PRIVATE);
    // Reading clients.txt
    File clientsFile = new File(informationData, CLIENTS_FILE);
    // Driver uploads all the information from txt file
    String clientFilePath = clientsFile.getPath();
    try {
      // this ensures creating the instance of Client Manager
      Driver.uploadClientInfo(clientFilePath);
    } catch (IOException clientFile) {
      clientFile.printStackTrace();
    }
    // uploading admin (initial) passwords to the system from the main
    File passwordAdminFile = new File(informationData, PASSWORDS_FILE);
    String passwordAdminFilePath = passwordAdminFile.getPath();
    System.out.println(passwordAdminFile);
    try {
      String nextLine;
      Scanner sc = new Scanner(new FileInputStream(passwordAdminFilePath));
      while (sc.hasNext()) {
        nextLine = sc.nextLine();
        String[] parameters = nextLine.split(",");
        String email = parameters[1];
        String password = parameters[2];
        adminCredentials.put(email, password);
      }
      sc.close();
    } catch (IOException passwords) {
      passwords.printStackTrace();
    }
    // uploading client passwords to the system from the main
    File passwordClientFile = new File(informationData, CLIENT_PASSWORDS_FILE);
    String passwordClientFilePath = passwordClientFile.getPath();
    try {
      Driver.currDatabase.uploadClientPasswords(passwordClientFilePath);
      clientCredentials = Driver.currDatabase.getClientPasswordWriter().getInformation();
    } catch (IOException passwords) {
      passwords.printStackTrace();
    }
    // uploading flights to the system
    File flightFile = new File(informationData, FLIGHTS_FILE);
    String flightFilePath = flightFile.getPath();
    try {
      // this ensures creating instance of Flight Manager
      Driver.uploadFlightInfo(flightFilePath);
    } catch (IOException flights) {
      flights.printStackTrace();
    }
    // uploading the already booked itineraries
    File itineraryFile = new File(informationData,ITINERARIES_STORING);
    String itinerariesFilePath = itineraryFile.getPath();
    try {
      Driver.currDatabase.uploadItineraryInfo(itinerariesFilePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Save the Clients map internally for authentication (existence)
    this.clientDatabase = Driver.currDatabase.getClients();
    System.out.println(clientCredentials);

  }

  /** Called when the user presses the Sign In button.
  */
  public void userHomePage(View view) {
    clientCredentials = Driver.currDatabase.getClientPasswordWriter().getInformation();
    Set adminEmails = adminCredentials.keySet();
    String email = emailView.getText().toString();
    String password = passwordView.getText().toString();
    // find the client in our database
    if (clientDatabase.containsKey(email)) {
      Client thisClient = clientDatabase.get(email);
      String expectedPassword = clientCredentials.get(email);
      if (expectedPassword.equals(password)) {
        // setting up the intent
        Intent intent = new Intent(this,ClientInfoActivity.class);
        intent.putExtra(CLIENT_INFO, thisClient);
        // start the activity with the intent
        startActivity(intent);
      } else {
        Toast.makeText(getApplicationContext(), "Password Invalid, please try again.",
                Toast.LENGTH_SHORT).show();
      }
    } else if (adminEmails.contains(email)) { // case where the client not yet registered
      String expectedPassword = adminCredentials.get(email);
      if (expectedPassword.equals(password)) {
        // set up the intent and open Admin Info Activity
        Intent intent = new Intent(this, AdminInfoActivity.class);
        startActivity(intent);
      } else {
        Toast.makeText(getApplicationContext(), "Password Invalid, please try again.",
                Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast messageError = Toast.makeText(getApplicationContext(), " Please Register First",
              Toast.LENGTH_LONG);
      messageError.show();
    }
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.email_sign_in_button:
        userHomePage(view);
        break;
      case R.id.register_button:
        startActivity(new Intent(this,RegisterActivity.class));
        break;
      default:
        break;
    }
  }

}



