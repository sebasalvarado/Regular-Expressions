package user;

import trip.Itinerary;

import java.io.Serializable;
import java.util.ArrayList;


/**A class representation of a client.This Class extends User and has the following
 * stored; last name, first name, email, address, credit card number and credit
 * card expiry date.
 * @author Jose, Sebastian, Jacob, Adam.
 */
public class Client extends User implements Serializable {
  private static final long serialVersionUID = 1L;
  private ArrayList<Itinerary> bookedItineraries = new ArrayList<>();
  private String lastName;
  private String firstNames;
  private String email;
  private String address;
  private String creditCardNumber;
  private String expiryDate;

  /** Creates a Client object that would store all valuable information from a client.
   * @param lastName The last name of the Client
   * @param firstNames The First Name of the Client
   * @param email The email from the client
   * @param address The Address of the client.
   * @param creditCardNumber The Stored Credit Card
   * @param expiryDate The Expiry Date for the Credit Card.
   */
  public Client(String lastName, String firstNames, String email, String address,
                  String creditCardNumber, String expiryDate) {
    this.lastName = lastName;
    this.firstNames = firstNames;
    this.email = email;
    this.address = address;
    this.creditCardNumber = creditCardNumber;
    this.expiryDate = expiryDate;
  }

  /**
   * Returns this client's email.
   * @return The email of this client
   */
  public String getEmail() {
    return this.email;
  }

  @Override
  /**Returns the string representation of a Client object.
   * Every two items are separated by a comma.
   */
  public String toString() {
    return lastName + "," + firstNames + "," + email
                + "," + address + "," + creditCardNumber + ","
                + expiryDate;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Client client = (Client) obj;

    if (!lastName.equals(client.lastName)) {
      return false;
    }
    return email.equals(client.email);

  }

  @Override
  public int hashCode() {
    int result = lastName.hashCode();
    result = 31 * result + email.hashCode();
    return result;
  }


  /**
   * Books the given itinerary to this client.
   * @param thisItinerary The itinerary to be booked
   */
  public void bookItinerary(Itinerary thisItinerary) {
    bookedItineraries.add(thisItinerary);
  }

  /**
   * Returns the last name of this client.
   * @return The last name fo this client
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of this client.
   * @param lastName The last name that will be saved
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the first names of this client.
   * @return Returns the first names of this client
   */
  public String getFirstNames() {
    return firstNames;
  }

  /**
   * Sets the first names of this client.
   * @param firstNames The first names to be saved
   */
  public void setFirstNames(String firstNames) {
    this.firstNames = firstNames;
  }

  /**
   * Sets the email of this client.
   * @param email The email that will be saved to this client
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the address of the given client.
   * @return The address of the client
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of this client.
   * @param address The address to be saved
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Returns the credit card number of this client.
   * @return The credit card number of this client
   */
  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  /**
   * Sets the credit card number of this client.
   * @param creditCardNumber The credit card number to be saved
   */
  public void setCreditCardNumber(String creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  /**
   * Returns the expiry date of this client's credit card.
   * @return The expiry date of this client's credit card
   */
  public String getExpiryDate() {
    return expiryDate;
  }

  /**
   * Sets the expiry date of this client's credit card.
   * @param expiryDate The expiry date to be saved
   */
  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
  }
}
