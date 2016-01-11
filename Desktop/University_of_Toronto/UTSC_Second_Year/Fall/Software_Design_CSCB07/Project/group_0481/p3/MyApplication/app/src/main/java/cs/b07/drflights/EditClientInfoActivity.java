package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import driver.Driver;
import managers.ClientManager;
import managers.ItineraryManager;
import managers.PasswordManager;
import trip.Itinerary;
import user.Client;

import java.io.IOException;
import java.util.ArrayList;



/**
 * An activity that allows a client to edit all information that belongs to them minus the password
 * as that is a separate activity.
 * @author Adam
 */
public class EditClientInfoActivity extends AppCompatActivity implements View.OnClickListener {
  public static final String CLIENT_INFO = "com.b07.drflights.ClientInfo";
  private EditText etFirstName;
  private EditText etLastName;
  private EditText etAddress;
  private EditText etCreditCardNum;
  private EditText etExpiryDate;
  private EditText etEmail;
  Button buttonSaveChanges;
  private Client receivedClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_client_info);
    Intent intent = getIntent();
    receivedClient = (Client) intent.getSerializableExtra(ClientInfoActivity.SENT_CLIENT_EDIT);
    etFirstName = (EditText) findViewById(R.id.first_Name);
    etLastName = (EditText) findViewById(R.id.last_name);
    etAddress = (EditText) findViewById(R.id.address);
    etCreditCardNum = (EditText) findViewById(R.id.credit_card_number);
    etExpiryDate = (EditText) findViewById(R.id.expiry_date);
    etEmail = (EditText) findViewById(R.id.client_email);
    buttonSaveChanges = (Button) findViewById(R.id.bClientInfoChanged);
    buttonSaveChanges.setOnClickListener(this);
  }

  private void saveEditedClientInfo(View view) {
    ClientManager clientManager = Driver.currDatabase.getClientWriter();
    Client editedClient = clientManager.getInformation().get(receivedClient.getEmail());

    String editedFirstName = etFirstName.getText().toString();
    if (!editedFirstName.equals(receivedClient.getFirstNames()) && !editedFirstName.equals("")) {
      receivedClient.setFirstNames(editedFirstName);
      editedClient.setFirstNames(editedFirstName);
    }

    String editedLastName = etLastName.getText().toString();
    if (!editedLastName.equals(receivedClient.getLastName()) && !editedLastName.equals("")) {
      receivedClient.setLastName(editedLastName);
      editedClient.setLastName(editedLastName);
    }

    String editedAddress = etAddress.getText().toString();
    if (!editedAddress.equals(receivedClient.getAddress()) && !editedAddress.equals("")) {
      receivedClient.setAddress(editedAddress);
      editedClient.setAddress(editedAddress);
    }

    String editedCardNum = etCreditCardNum.getText().toString();
    if (!editedCardNum.equals(receivedClient.getCreditCardNumber()) && !editedCardNum.equals("")) {
      receivedClient.setCreditCardNumber(editedCardNum);
      editedClient.setCreditCardNumber(editedCardNum);
    }

    String editedExpiry = etExpiryDate.getText().toString();
    if (!editedExpiry.equals(receivedClient.getExpiryDate()) && !editedExpiry.equals("")) {
      receivedClient.setExpiryDate(editedExpiry);
      editedClient.setExpiryDate(editedExpiry);
    }

    String editedEmail = etEmail.getText().toString();
    if (!editedEmail.equals(receivedClient.getEmail()) && !editedEmail.equals("")) {
      String oldEmail = receivedClient.getEmail();
      receivedClient.setEmail(editedEmail);
      editedClient.setEmail(editedEmail);
      clientManager.getInformation().remove(oldEmail);
      clientManager.getInformation().put(editedEmail, receivedClient);
      ItineraryManager itineraryManager = Driver.currDatabase.getItineraryWriter();
      ArrayList<Itinerary> bookedIts = itineraryManager.getInformation().remove(oldEmail);
      if (!(bookedIts == null)) {
        itineraryManager.getInformation().put(editedEmail, bookedIts);
        itineraryManager.saveToFile();
      }
      PasswordManager passwordManager = Driver.currDatabase.getClientPasswordWriter();
      String password = passwordManager.getInformation().remove(oldEmail);
      passwordManager.getInformation().put(editedEmail, password);
      passwordManager.saveToFile();
    }
    clientManager.saveToFile();
    try {
      Driver.currDatabase.uploadClientInfo(clientManager.getFilePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
    Toast.makeText(getApplicationContext(), "Client Successfully edited.",
            Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.bClientInfoChanged:
        saveEditedClientInfo(view);
        break;
      default:
        break;
    }
  }

  /**
  * OnBackPressed Method.
  */
  public void onBackPressed() {
    Intent intent = new Intent(this, ClientInfoActivity.class);
    intent.putExtra(EditClientInfoActivity.CLIENT_INFO,receivedClient);
    startActivity(intent);

  }
}
