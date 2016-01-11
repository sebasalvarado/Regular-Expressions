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
import managers.PasswordManager;
import user.Client;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;



public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

  public static final String CLIENT_INFO = "com.b07.drflights.ClientInfo";

  Button buttonRegister;
  EditText etFirstName;
  EditText etLastName;
  EditText etEmail;
  EditText etAddress;
  EditText etPassword;
  EditText etcreditCNumber;
  EditText etExpiryDate;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    etFirstName = (EditText) findViewById(R.id.etFirstName);
    etLastName = (EditText) findViewById(R.id.etLastName);
    etEmail = (EditText) findViewById(R.id.etEmail);
    etAddress = (EditText) findViewById(R.id.etAddress);
    etPassword = (EditText) findViewById(R.id.etPassword);
    etcreditCNumber = (EditText) findViewById(R.id.etCCNumber);
    etExpiryDate = (EditText) findViewById(R.id.etExpiryDate);
    buttonRegister = (Button) findViewById(R.id.bregister);
    buttonRegister.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {

    if (!Driver.currDatabase.getClients().containsKey(etEmail.getText().toString())) {
      Client newClient = new Client(etLastName.getText().toString(),
            etFirstName.getText().toString(), etEmail.getText().toString(),
            etAddress.getText().toString(), etcreditCNumber.getText().toString(),
            etExpiryDate.getText().toString());

      ClientManager manager = Driver.currDatabase.getClientWriter();
      manager.add(newClient);
      manager.saveToFile();

      // now make sure to regenerate the clients and save them in Database
      try {
        File mainClientData = this.getApplicationContext().getDir(LoginActivity.USER_DATA_DIR,
                MODE_PRIVATE);
        File mainClientsFile = new File(mainClientData, LoginActivity.CLIENTS_FILE);
        Driver.uploadClientInfo(mainClientsFile.getPath());
        PasswordManager clientPasswords = Driver.currDatabase.getClientPasswordWriter();

        // saving the password of the new client
        HashMap<String,String> passwords = clientPasswords.getInformation();
        passwords.put(newClient.getEmail(), etPassword.getText().toString());
        clientPasswords.saveToFile();

        Intent intent = new Intent(this, ClientInfoActivity.class);
        intent.putExtra(CLIENT_INFO, newClient);
        startActivity(intent);
      } catch (IOException e) {
        Toast messageError = Toast.makeText(getApplicationContext(),
                "This client was not registered correctly", Toast.LENGTH_LONG);
        messageError.show();

        e.printStackTrace();
      }

    } else {
      Toast messageError = Toast.makeText(getApplicationContext(),
            "This email is already registered", Toast.LENGTH_LONG);
      messageError.show();
    }

  }
}
