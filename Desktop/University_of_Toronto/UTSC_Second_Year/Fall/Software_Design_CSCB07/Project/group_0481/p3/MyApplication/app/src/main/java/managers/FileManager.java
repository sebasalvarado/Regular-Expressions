package managers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by sebastianalvarado on 15-12-02.
 * This abstract manager makes the task of creating new managers much easier
 */
public abstract class FileManager<T> implements Serializable {
  protected String filePath;
  protected HashMap<String,T> information;

  public abstract void readFromCsvFile(String filePath) throws IOException;

  /**
   * Saves the given information HashMap to the filePath given depending on the child instance.
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
   * Reads from a main file that should be storing all the information previously loaded serialized
   * and puts it in the form of a HashMap.
   * @param path The path to the main file that will have its serialized content read
   */
  protected void readExistingFile(String path) {
    try {
      InputStream file = new FileInputStream(path);
      InputStream buffer = new BufferedInputStream(file);
      ObjectInput input = new ObjectInputStream(buffer);
      //deserialize the Map
      information = (HashMap<String, T>)input.readObject();
    } catch (ClassNotFoundException ex) {
      FileManager.fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
    } catch (IOException ex) {
      fLogger.log(Level.SEVERE, "Cannot perform input. I/O errors.", ex);
    }
  }


  protected static final Logger fLogger =
            Logger.getLogger(FlightManager.class.getPackage().getName());

}
