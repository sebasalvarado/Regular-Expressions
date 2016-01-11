package managers;

import user.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ClientManager extends FileManager implements Serializable {
  private ArrayList<String> newClients = new ArrayList<String>();

  /**
   * Creates a new client manager to read and save all files regarding clients.
   * @param file The main file that will be used for saving
   * @throws IOException Throws if the main file used for saving doesn't exist
   */
  public ClientManager(File file) throws IOException {
    this.information = new HashMap<String,Client>();
    // Populates the HashMap with information if it exists
    filePath = (String)file.getPath();
    if (file.exists()) {
      readExistingFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  /**
   * Returns the HashMap containing all the clients as values with their keys being their emails.
   * @return The HashMap containing all clients
   */
  public HashMap<String, Client> getInformation() {
    return information;
  }

  /**
   * Reads from a text file in the csv format and saves the new clients to the HashMap of clients.
   * @param filePath The csv formatted text file that will be read
   * @throws IOException Throws when the file to be read does not exist
   */
  public void readFromCsvFile(String filePath) throws IOException {
    String nextLine;
    Scanner sc = new Scanner(new FileInputStream(filePath));
    while (sc.hasNext()) {
      nextLine = sc.nextLine();
      String[] parameters = nextLine.split(",");
      String lastName = parameters[0];
      String firstNames = parameters[1];
      String email = parameters[2];
      String address = parameters[3];
      String creditCard = parameters[4];
      String expiryDate = parameters[5];
      Client client = new Client(lastName, firstNames, email, address, creditCard, expiryDate);
      information.put(email, client);
      newClients.add(client.getEmail());
    }
    sc.close();
  }

  /**
   * Adds a singular client to the HashMap which will only be used during a self-register.
   * @param client New client to be added to the Client HashMap
   */
  public void add(Client client) {
    information.put(client.getEmail(), client);
  }

  /**
   * Returns the ArrayList containing all new clients that have been added in a csv formatted text
   * file that do not yet have passwords.
   * @return Returns the ArrayList of new clients with currently uncreated passwords
   */
  public ArrayList<String> getNewClients() {
    return newClients;
  }

  /**
   * Empties the ArrayList of new clients, marking them as having passwords created.
   */
  public void emptyNewClients() {
    this.newClients = new ArrayList<String>();
  }

  /**
   * Returns the main clients file path.
   * @return the path to the main clients file
   */
  public String getFilePath() {
    return filePath;
  }
}
