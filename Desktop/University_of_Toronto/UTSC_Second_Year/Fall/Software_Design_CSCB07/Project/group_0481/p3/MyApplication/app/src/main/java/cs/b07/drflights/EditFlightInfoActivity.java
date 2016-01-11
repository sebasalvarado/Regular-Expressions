package cs.b07.drflights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cs.b07.drflights.admin.AdminInfoActivity;
import driver.Driver;
import managers.FlightManager;
import trip.Flight;

import java.io.IOException;




public class EditFlightInfoActivity extends AppCompatActivity implements View.OnClickListener {
  private EditText etFlightNum;
  private EditText etDepartureTime;
  private EditText etArrivalTime;
  private EditText etAirline;
  private EditText etOrigin;
  private EditText etDestination;
  private EditText etPrice;
  private EditText etNumSeats;
  Button buttonSaveChanges;
  private Flight flightReceived;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_flight_info);

    Intent intent = getIntent();
    flightReceived = (Flight) intent.getSerializableExtra(AdminInfoActivity.FLIGHT_INFO);

    etFlightNum = (EditText) findViewById(R.id.flight_number);
    etDepartureTime = (EditText) findViewById(R.id.departure_time);
    etArrivalTime = (EditText) findViewById(R.id.arrival_time);
    etAirline = (EditText) findViewById(R.id.airline);
    etOrigin = (EditText) findViewById(R.id.origin);
    etDestination = (EditText) findViewById(R.id.destination);
    etPrice = (EditText) findViewById(R.id.price);
    etNumSeats = (EditText) findViewById(R.id.num_seats);

    buttonSaveChanges = (Button) findViewById(R.id.bFlightInfoChanged);
    buttonSaveChanges.setOnClickListener(this);
  }

  private void saveEditedFlightInfo(View view) {
    FlightManager flightManager = Driver.currDatabase.getFlightWriter();
    Flight editedFlight = flightManager.getInformation().get(flightReceived.getFlightNum());
    String editedFlightNum = etFlightNum.getText().toString();
    if (!editedFlightNum.equals(flightReceived.getFlightNum()) && !editedFlightNum.equals("")) {
      String oldNum = flightReceived.getFlightNum();
      flightReceived.setFlightNum(editedFlightNum);
      editedFlight.setFlightNum(editedFlightNum);
      flightManager.getInformation().remove(oldNum);
      flightManager.getInformation().put(editedFlightNum, flightReceived);
    }
    String editedDepTime = etDepartureTime.getText().toString();
    if (!editedDepTime.equals(flightReceived.getCompleteDepDate()) && !editedDepTime.equals("")) {
      flightReceived.setCompleteDepDate(editedDepTime);
      editedFlight.setCompleteDepDate(editedDepTime);
    }
    String editedArrTime = etArrivalTime.getText().toString();
    if (!editedArrTime.equals(flightReceived.getCompleteArrDate()) && !editedArrTime.equals("")) {
      flightReceived.setCompleteArrDate(editedArrTime);
      editedFlight.setCompleteArrDate(editedArrTime);
    }
    String editedAirline = etAirline.getText().toString();
    if (!editedAirline.equals(flightReceived.getAirline()) && !editedAirline.equals("")) {
      flightReceived.setAirline(editedAirline);
      editedFlight.setAirline(editedAirline);
    }
    String editedOrigin = etOrigin.getText().toString();
    if (!editedOrigin.equals(flightReceived.getOrigin()) && !editedOrigin.equals("")) {
      flightReceived.setOrigin(editedOrigin);
      editedFlight.setOrigin(editedOrigin);
    }
    String editedDest = etDestination.getText().toString();
    if (!editedDest.equals(flightReceived.getDestination()) && !editedDest.equals("")) {
      flightReceived.setDestination(editedDest);
      editedFlight.setDestination(editedDest);
    }
    String editedPrice = etPrice.getText().toString();
    if (!editedPrice.equals(flightReceived.getCost().toString()) && !editedPrice.equals("")) {
      flightReceived.setPrice(editedPrice);
      editedFlight.setPrice(editedPrice);
    }
    String editedNumSeats = etNumSeats.getText().toString();
    if (!editedNumSeats.equals(
            flightReceived.getNumSeats().toString()) && !editedNumSeats.equals("")) {
      flightReceived.setNumSeats(editedNumSeats);
      editedFlight.setNumSeats(editedNumSeats);
    }
    flightManager.saveToFile();
    try {
      Driver.currDatabase.uploadFlightInfo(flightManager.getFilePath());
    } catch (IOException e) {
      e.printStackTrace();
    }

    Toast.makeText(getApplicationContext(), "Flight Successfully edited.",
        Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onClick(View view) {

    switch (view.getId()) {
      case R.id.bFlightInfoChanged:
        saveEditedFlightInfo(view);
        break;
      default:
        break;
    }
  }
}
