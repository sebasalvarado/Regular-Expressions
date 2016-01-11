package user;

import driver.Driver;
import managers.ClientManager;
import managers.FlightManager;

import java.io.IOException;

import java.util.ArrayList;


/** Administrator class that would allow the user to have privileges of an admin.
 * @author Adam
 */
public class Administrator extends User {

  /**
   * Updates the flight manager such that it has all the new flights mapped to their flight
   * numbers.
   * @param flightFilePath Path that the flight file is stored at
   * @throws IOException Throws if the main flight file doesn't exist
   */
  public void updateFlightManager(String flightFilePath) throws IOException {
    FlightManager manager = Driver.currDatabase.getFlightWriter();

    try {
      manager.readFromCsvFile(flightFilePath);
      manager.saveToFile();
    } catch (IOException e) {
      throw new IOException();
    }
  }

  /**
   * Uploads the new flight information into the database for use in our application.
   * @param filePath The file path of the file to be uploaded
   * @throws IOException Throws if the file doesn't exist
   */
  public void uploadFlightInformation(String filePath) throws IOException {

    try {
      Driver.uploadFlightInfo(filePath);
    } catch (IOException e) {
      throw new IOException();
    }
  }

  /**
   * Updates the client manager such that it has all the new clients mapped to their emails.
   * @param clientFilePath Path that the client file is stored at
   * @throws IOException Throws if the main client file doesn't exist
   */
  public void updateClientManager(String clientFilePath) throws IOException {
    ClientManager manager = Driver.currDatabase.getClientWriter();

    try {
      manager.readFromCsvFile(clientFilePath);
      manager.saveToFile();
    } catch (IOException e) {
      throw new IOException();
    }
  }

  /**
   * Uploads the new client information into the database for use in our application.
   * @param filePath The file path of the file to be uploaded
   * @throws IOException Throws if the file doesn't exist
   */
  public void uploadClientInformation(String filePath) throws IOException {

    try {
      Driver.uploadClientInfo(filePath);
    } catch (IOException e) {
      throw new IOException();
    }
  }

  /**
   * Returns an ArrayList containing Strings of new Clients that were just added into the system.
   * @return The ArrayList of Strings representing new Clients
   */
  public ArrayList<String> getNewClients() {

    ClientManager manager = Driver.currDatabase.getClientWriter();

    return manager.getNewClients();
  }

}
