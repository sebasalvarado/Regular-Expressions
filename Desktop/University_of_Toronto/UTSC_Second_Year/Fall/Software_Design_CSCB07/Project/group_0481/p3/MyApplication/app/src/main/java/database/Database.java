package database;

import managers.ClientManager;
import managers.FlightManager;
import managers.ItineraryManager;
import managers.PasswordManager;
import structures.Graph;
import trip.Flight;
import trip.Itinerary;
import user.Client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**A class representation of a database that stores client and flight information in HashMaps
 * and can perform operations to sort them into itineraries.To help make finding itineraries
 * easy, all flights are stored in a graph with each flight pointing to connecting flights.
 * @author Adam, Jacob, Sebastian, Jose
 */

public class Database {

  private static ClientManager clientWriter;
  private static FlightManager flightWriter;
  private static PasswordManager clientPasswordWriter;
  private static PasswordManager adminPasswordWriter;
  private static ItineraryManager itineraryWriter;

  private static HashMap<String, Flight> flights;
  private static HashMap<String, Client> clients;
  private static HashMap<String, ArrayList<Itinerary>> bookedItineraries;
  private static Graph graph = new Graph();

  /**
   * Creates a new database with the flights and clients HashMaps initialized as empty
   * to be filled upon some data uploads.
   */
  public Database() {

    flights = new HashMap<String, Flight>();
    clients = new HashMap<String, Client>();
    bookedItineraries = new HashMap<String, ArrayList<Itinerary>>();
  }

  /**
   * Returns the HashMap of flights with the flight number as the key
   * and the Flight object as the value being mapped to.
   * @return the flights stored in the database
   */
  public HashMap<String, Flight> getFlights() {
    return flights;
  }

  /**
   * Returns the HashMap of clients with the client email as the key
   * and the Client object as the value being mapped to.
   * @return the clients stored in the database
   */
  public HashMap<String, Client> getClients() {
    return clients;
  }

  /**
   * Returns the Client object stored in the clients HashMap with the email as
   * the key.
   * @param email The email of the client that will be retrieved
   * @return a client stored in the database
   */
  public Client getClient(String email) {
    return clients.get(email);
  }

  /**
   * Adds a new client directly to the database.
   * @param client The new client to be added
   */
  public void addClient(Client client) {
    clients.put(client.getEmail(), client);
  }

  /**
   * Loads client information into the clients HashMap from a given text file.
   * @param path The path to the needed client text file
   * @throws IOException Throws if there is no file at the given path
   */
  public  void uploadClientInfo(String path) throws IOException {
    File clientFile = new File(path);
    clientWriter = new ClientManager(clientFile);
    clients = clientWriter.getInformation();
  }

  /**
   * Loads flight information into the flights HashMap from a given text file.
   * @param path The path to the needed flights text file
   * @throws IOException Throws if there is no file at the given path
   */
  public void uploadFlightInfo(String path) throws IOException {

    File flightFile = new File(path);
    flightWriter = new FlightManager(flightFile);
    flights = flightWriter.getInformation();
    // add each flight into the graph
    System.out.println(flights.keySet());
    graph.prepareGraph();
    for (Flight nextFlight: flights.values()) {
      graph.addFlight(nextFlight);
    }
  }

  /**
   * Loads Booked Itineraries information in the Itineraries HashMap from a preset file.
   * @param path The path to the specified itineraries file
   * @throws IOException Throws if there is no file at the given path
   */
  public void uploadItineraryInfo(String path) throws IOException {
    File itineraryFile = new File(path);
    itineraryWriter = new ItineraryManager(itineraryFile);
    bookedItineraries = itineraryWriter.getInformation();
  }

  /**
   * Loads client passwords into the clients passwords manager from a preset file.
   * @param filePath The path to the specified passwords file
   * @throws IOException Throws if there is no file at the given path
   */
  public void uploadClientPasswords(String filePath) throws IOException {
    File passwordFile = new File(filePath);
    clientPasswordWriter = new PasswordManager(passwordFile);
  }

  /**
   * Loads admin passwords into the admins passwords manager from a preset file.
   * @param filePath The path to the specified passwords file
   * @throws IOException Throws if there is no file at the given path
   */
  public void uploadAdminPasswords(String filePath) throws IOException {
    File passwordFile = new File(filePath);
    adminPasswordWriter = new PasswordManager(passwordFile);
  }

  /**
   * Returns a string representation of all flights in the database that have the indicated
   * departure date, origin and destination.
   * @param depDate The departure date and time for the flight to search for
   * @param origin The origin of the flight
   * @param destination The final destination for the flight
   * @return a String of all flights in this category
   */
  public String searchFlights(String depDate, String origin, String destination) {
    String flightStr = "";
    for (String id:flights.keySet()) {
      Flight curFlght = flights.get(id);
      Set<Object> curSet = curFlght.getFlightInfo();
      String currDate = curFlght.getDepartureDate();
      StringBuffer currDateBuff = new StringBuffer(currDate);

      if (( currDateBuff.substring(0,10).toString().equals(depDate))
                    && curSet.contains(origin) && curSet.contains(destination)) {
        flightStr += curFlght.toString();
      }
    }
    return (flightStr);
  }

  /**
   * Returns a list of itineraries based on a clients intended departure date,
   * origin, and destination.
   * @param date The departure date of the client
   * @param origin The original location of the client
   * @param destination The destination of the client
   * @return the list of itineraries satisfying our client's requirements
   */
  public ArrayList<Itinerary> getItineraries(String date, String origin, String destination) {
    ArrayList<Itinerary> allItineraries = graph.findItineraries(date,origin,destination);
    graph.emptyGraphSearch();
    return allItineraries;
  }

  /**
   * Returns the graph data where flights are stored as nodes.
   * @return the graph
   */
  public static Graph getGraph() {
    return graph;
  }


  /**
   * Sets a Hashmap of clients.
   * @param clients The clients to set
   */
  public static void setClients(HashMap<String, Client> clients) {
    Database.clients = clients;
  }

  /**
   * Return a list of itineraries sorted in terms of total travel time.
   * @param itineraries The list of itineraries that match the requirements of the client
   * @return the list of valid itineraries sorted in terms of total time
   */
  public static ArrayList<Itinerary> totalTravelTimeSort(ArrayList<Itinerary> itineraries) {
    if (itineraries.size() > 1) {
      for (int itinerary1 = 0; itinerary1 < itineraries.size(); itinerary1++) {
        for (int itinerary2 = 0; itinerary2 < itineraries.size() - itinerary1 - 1; itinerary2++) {
          if (itineraries.get(itinerary2).compareTo(itineraries.get(itinerary2 + 1)) > 0) {
            Itinerary temp = itineraries.get(itinerary2);
            itineraries.set(itinerary2,itineraries.get(itinerary2 + 1) );
            itineraries.set(itinerary2 + 1, temp);
          }
        }
      }
    }
    return itineraries;
  }

  /**
   * Return a list of itineraries sorted in terms of total cost.
   * @param itineraries The list of itineraries that match the requirements of the client
   * @return the list of valid itineraries sorted in terms of total cost
   */
  public ArrayList<Itinerary> totalCostSort(ArrayList<Itinerary> itineraries) {
    if (itineraries.size() > 1) {
      for (int itinerary1 = 0; itinerary1 < itineraries.size(); itinerary1++) {
        for (int itinerary2 = 0; itinerary2 < itineraries.size() - itinerary1 - 1; itinerary2++) {
          if (itineraries.get(itinerary2).getTotalCost()
                            >= itineraries.get(itinerary2 + 1).getTotalCost()) {
            Itinerary temp = itineraries.get(itinerary2);
            itineraries.set(itinerary2,itineraries.get(itinerary2 + 1) );
            itineraries.set(itinerary2 + 1, temp);
          }
        }
      }
    }
    return itineraries;
  }

  /**
   * Returns a given array list of itineraries in a viable format for output
   * as a String.
   * @param itineraries The list of itineraries that need a good String representation
   * @return the String representing the list of itineraries
   */
  public String printItineraries(ArrayList<Itinerary> itineraries) {
    String finalString = new String();
    for (Itinerary nextItinerary: itineraries) {
      String itString = nextItinerary.toString();
      finalString += itString;
    }
    return finalString;
  }

  /**
   * Return a singular flight when given it's flight number.
   * @param flightNumber The flight number of the flight to be returned
   * @return The flight that was searched for
   */
  public Flight getFlight(String flightNumber) {
    Flight foundFlight = flights.get(flightNumber);
    return foundFlight;
  }

  /**
   * Returns the manager responsible for reading and writing client files.
   * @return The client manager
   */
  public ClientManager getClientWriter() {
    return clientWriter;
  }

  /**
   * Returns the manager responsible for reading and writing flight files.
   * @return The flight manager
   */
  public FlightManager getFlightWriter() {
    return flightWriter;
  }

  /**
   * Returns the manager responsible for reading and writing admin password files.
   * @return The admin password manager
   */
  public PasswordManager getAdminPasswordWriter() {
    return adminPasswordWriter;
  }

  /**
   * Returns the manager responsible for reading and writing client password files.
   * @return The client password manager
   */
  public PasswordManager getClientPasswordWriter() {
    return clientPasswordWriter;
  }

  /**
   * Returns the manager responsible for reading and writing itinerary files.
   * @return The itinerary manager
   */
  public ItineraryManager getItineraryWriter() {
    return itineraryWriter;
  }

  /**
   * Pre-Condition: The BookedItineraryActivity must have at least one flight
   * Post-Condition: The number of seats of each Flight gets reduced by one and the information
   * is saved to the file. NEED TO BE REUPLOADED AFTER USING THIS METHOD
   */
  public void bookedItinerary(LinkedList<Flight> listOfFlights) {
    // loop through the flights of this itinerary
    for (Flight nextFlight: listOfFlights) {
      // find them in the Database
      Integer newSeats = nextFlight.getNumSeats() - 1;
      nextFlight.setNumSeats(newSeats.toString());
      // adding them to the Flight Manager
      flightWriter.add(nextFlight);
    }
    // re run the whole Flight information
    flightWriter.saveToFile();
  }

}