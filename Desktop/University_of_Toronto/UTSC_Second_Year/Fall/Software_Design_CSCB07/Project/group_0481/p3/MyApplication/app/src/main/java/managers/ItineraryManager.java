package managers;

import trip.Itinerary;
import user.Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Adam on 12/1/2015.
 * This will save booked itineraries for future use.
 */
public class ItineraryManager implements Serializable {

  private HashMap<String, ArrayList<Itinerary>> information;
  private String filePath;

  /**
   * Creates a new itinerary manager to read and save all files regarding booked itineraries.
   * @param file The main file that will be used for saving
   * @throws IOException Throws if the main file used for saving doesn't exist
   */
  public ItineraryManager(File file) throws IOException {
    this.information = new HashMap<>();
    // Populates the HashMap with information if it exists
    filePath = (String)file.getPath();
    if (file.exists()) {
      readExistingFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  /**
   * Reads from a main file that should be storing all the information previously loaded serialized
   * and puts it in the form of a HashMap.
   * @param path The path to the main file that will have its serialized content read
   */
  private void readExistingFile(String path) {
    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);
      //deserialize the Map
      information = (HashMap<String,ArrayList<Itinerary>>)input.readObject();
    } catch (ClassNotFoundException ex) {
      fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
    } catch (IOException ex) {
      fLogger.log(Level.SEVERE, "Cannot perform input. I/O errors.", ex);
    }
  }

  /**
   * Returns the HashMap containing all the booked itineraries for a client as values with their
   * keys being the client emails.
   * @return The HashMap containing all clients
   */
  public HashMap<String, ArrayList<Itinerary>> getInformation() {
    return information;
  }

  /**
   * Saves the given information HashMap to the filePath given for itineraries.
   */
  public void saveToFile() {
    try {
      OutputStream file = new FileOutputStream(filePath);
      OutputStream buffer = new BufferedOutputStream(file);
      ObjectOutput output = new ObjectOutputStream(buffer);
      output.writeObject(information);
      output.close();
    } catch (IOException ex) {
      fLogger.log(Level.SEVERE, "Cannot perform output.", ex);
    }
  }

  /**
   * Adds a new itinerary to the HashMap with the key as the email of the client who booked it.
   * If the client has never booked an itinerary, they are entered for the first time
   * If the client has booked an itinerary previously, the second itinerary will be added into
   * the ArrayList of booked itineraries alongside the first one
   * @param itinerary The new booked itinerary to add
   * @param client The client that is booking this new itinerary
   */
  public void add(Itinerary itinerary, Client client) {
    int counter = 0;

    if (information.containsKey(client.getEmail())) {

      for (int i = 0; i < information.get(client.getEmail()).size(); i++) {
        Itinerary bookedItinerary = information.get(client.getEmail()).get(i);

        if (!itinerary.equals(bookedItinerary)) {
          counter++;
        }

      }

      if (counter == information.get(client.getEmail()).size()) {
        information.get(client.getEmail()).add(itinerary);
        itinerary.setAvailableSeats(itinerary.getAvailableSeats() - 1);
      }

    } else {
      ArrayList<Itinerary> newItinerary = new ArrayList<>();
      newItinerary.add(itinerary);
      information.put(client.getEmail(), newItinerary);
      itinerary.setAvailableSeats(itinerary.getAvailableSeats() - 1);
    }
  }

  private static final Logger fLogger =
            Logger.getLogger(FlightManager.class.getPackage().getName());

}
