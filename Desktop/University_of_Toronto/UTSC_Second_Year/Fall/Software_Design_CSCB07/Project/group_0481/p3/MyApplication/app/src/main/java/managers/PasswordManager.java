package managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by sebastianalvarado on 15-12-02.
 * This will save new passwords to their designated clients
 */
public class PasswordManager extends FileManager implements Serializable {

  private HashSet<String> newClients;

  /**
   * Creates a new password manager to read and save all files regarding passwords.
   * @param file The main client password file
   * @throws IOException Throws if the main client password file doesn't exist
   */
  public PasswordManager(File file) throws IOException {
    this.information = new HashMap<String,String>();
    // Populates the HashMap with information if it exists
    filePath = (String)file.getPath();
    if (file.exists()) {
      readExistingFile(filePath);
    } else {
      file.createNewFile();
    }
  }

  /**
   * Returns the HashMap containing all the passwords as values with their keys being the emails
   * of the corresponding client.
   * @return The HashMap containing all passwords
   */
  public HashMap<String, String> getInformation() {
    return information;
  }

  /**
   * Reads from a text file in the csv format and saves the new passwords to the HashMap of
   * passwords.
   * @param filePath The csv formatted text file that will be read
   * @throws IOException Throws when the file to be read does not exist
   */
  public void readFromCsvFile(String filePath) throws IOException {
    String nextLine;
    Scanner sc = new Scanner(new FileInputStream(filePath));
    while (sc.hasNext()) {
      nextLine = sc.nextLine();
      String[] parameters = nextLine.split(",");
      String email = parameters[1];
      String password = parameters[2];
      information.put(email, password);
    }
    sc.close();
  }

}
