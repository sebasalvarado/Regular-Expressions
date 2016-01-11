package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import driver.Driver;
import user.Client;

import java.util.HashMap;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
  EditText etOldPassword;
  EditText etNewPassword;
  Button bsaveChanges;
  String clientEmail;
  HashMap<String, String> clientCredentials;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_change_password);

    // TODO: Make sure you make this onCreate correctly
    Intent intent = getIntent();
    Client receivedClient = (Client) intent.getSerializableExtra(
            ClientInfoActivity.SENT_CLIENT_PASSWORD);
    clientEmail = receivedClient.getEmail();

    etOldPassword = (EditText) findViewById(R.id.old_password);
    etNewPassword = (EditText) findViewById(R.id.new_password);
    bsaveChanges = (Button) findViewById(R.id.bPasswordChanged);
    bsaveChanges.setOnClickListener(this);

  }

  private void saveNewPassword(View view) {

    clientCredentials = Driver.currDatabase.getClientPasswordWriter().getInformation();
    String clientsPassword = clientCredentials.get(clientEmail);
    if (etOldPassword.getText().toString().equals(clientsPassword)) {
      // find out how to snag passwords hashmap
      String newPassword = etNewPassword.getText().toString();
      Driver.currDatabase.getClientPasswordWriter().getInformation().put(clientEmail,
            newPassword);
      Driver.currDatabase.getClientPasswordWriter().saveToFile();
    } else {
      Toast.makeText(getApplicationContext(), "Incorrect Password.",
                Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.bPasswordChanged:
        saveNewPassword(view);
        break;
      default:
        break;
    }
  }

}
