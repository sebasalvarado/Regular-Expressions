package managers;

import trip.Flight;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;


public class FlightManager extends FileManager implements Serializable {

  /**
   * Creates a new flight manager to read and save all files regarding flights.
   * @param file The main file that will be used for saving
   * @throws IOException Throws if the main file used for saving doesn't exist
   */
  public FlightManager(File file) throws IOException {
    this.information = new HashMap<String,Flight>();
    // Populates the HashMap with information if it exists
    filePath = (String)file.getPath();
    if (file.exists()) {
      readExistingFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  /**
   * Returns the HashMap containing all the flights as values with their keys being their flight
   * numbers.
   * @return The HashMap containing all flights
   */
  public HashMap<String, Flight> getInformation() {
    return information;
  }

  /**
   * Reads from a text file in the csv format and saves the new flights to the HashMap of flights.
   * @param filePath The csv formatted text file that will be read
   * @throws IOException Throws when the file to be read does not exist
   */
  public void readFromCsvFile(String filePath) throws IOException {
    String nextLine;
    Scanner sc = new Scanner(new FileInputStream(filePath));
    while (sc.hasNext()) {
      nextLine = sc.nextLine();
      String[] parameters = nextLine.split(",");
      String flightNo = parameters[0];
      String departureDate = parameters[1];
      String arrivalDate = parameters[2];
      String airline = parameters[3];
      String origin = parameters[4];
      String destination = parameters[5];
      String price = parameters[6];
      String availableSeats = parameters[7];
      Integer numSeats = new Integer(availableSeats);
      Flight flight = new Flight(flightNo, departureDate, arrivalDate, airline,
                 origin, destination, price, numSeats);
      information.put(flightNo, flight);
    }
    sc.close();
  }

  /**
   * Adds a new flight to the HashMap of flights.
   * @param newFlight The new flight to be added to the HashMap
   */
  public void add(Flight newFlight) {
    // method to add to the map a new flight that could be edited.
    information.put(newFlight.getFlightNum(),newFlight);
  }

  /**
   * Returns the main flights file path.
   * @return the path to the main flights file
   */
  public String getFilePath() {
    return filePath;
  }

}

