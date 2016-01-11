package trip;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;


/**A class representation of a Flight object. Each flight object is serializable and includes
 * departure date, arrival date, cost, origin, destination, flight number and travel time.
 * The whole information is also stored in a set for ease of access.The cost is formatted to
 * two decimal places and the dates are in the format YYYY-MM-DD HH:mm
 * The travel time is formatted to two decimal places.i.e hh:mm
 * The flight object is created by passing it all its parameters as strings.
 * @author Jacob, Adam, Jose, Sebastian.
 */

public class Flight implements Serializable {

  private static final long serialVersionUID = 1L;
  String completeArrDate;
  String completeDepDate;
  private Double  cost;
  private double travelTime;
  private String airline;
  private String origin;
  private String destination;
  private String flightNum;
  private String departureDate;
  private String arrivalDate;
  private Integer numSeats;

  Set<Object> flightInfo = new TreeSet<>();

  /** Flight object constructor which takes flight number, departure date, arrival date,
   *  airline, origin, destination and cost in that order all as string primitive type.
   * @param flightno
   *                the flight number of this flight.
   * @param depDate
   *                the departure date of this flight.
   * @param arrivalDate
   *                  the arrival date of this flight.
   * @param airline
   *                the airline for this flight.
   * @param orig
   *            the origin of this flight.
   * @param destination
   *                   the destination of this flight.
   * @param cost
   *            the cost of this flight.
   */
  public Flight(String flightno, String depDate, String arrivalDate, String airline,
                String orig,String destination, String cost, Integer availableSeats) {

    this.completeArrDate = arrivalDate;
    this.completeDepDate = depDate;
    SimpleDateFormat ourDate = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CANADA);


    try {

      Date deptDate = ourDate.parse(depDate);
      Date arrivaldate = ourDate.parse(arrivalDate);
      this.travelTime = (arrivaldate.getTime() - deptDate.getTime()) / 60000;
    } catch (ParseException e) {
      e.printStackTrace();
    }

    StringBuffer departDate = new StringBuffer(depDate);

    StringBuffer arDate = new StringBuffer(arrivalDate);
    depDate = departDate.substring(0, 10);
    arrivalDate = arDate.substring(0,10);

    this.flightNum = flightno;
    // Put all this info into a set.
    this.departureDate = depDate;
    this.flightInfo.add(this.departureDate);
    this.arrivalDate = arrivalDate;
    this.flightInfo.add(this.arrivalDate);
    this.airline = airline;
    this.flightInfo.add(this.airline);



    this.origin = orig;
    this.flightInfo.add(orig);
    this.destination = destination;
    this.flightInfo.add(destination);

    this.cost = Double.valueOf(cost);
    this.flightInfo.add(cost);


    // Change travelTime to hours
    this.travelTime = travelTime / 60;
    this.flightInfo.add(Double.toString(travelTime));

    this.numSeats = availableSeats;
    //this.flightInfo.add(numSeats);

  }

  /**Returns the arrival date in the format of YYYY-MM-DD HH:MM.
   * @return the completeArrDate
   */
  public String getCompleteArrDate() {
    return completeArrDate;
  }

  /**Sets the arrival date in the format of YYYY-MM-DD HH:MM.
   * @param completeArrDate The arrival date in format YYYY-MM-DD HH:MM
   */
  public void setCompleteArrDate(String completeArrDate) {
    this.completeArrDate = completeArrDate;
  }

  /**Returns the departure date in the format of YYYY-MM-DD HH:MM.
   * @return the completeDepDate
   */
  public String getCompleteDepDate() {
    return completeDepDate;
  }

  /**Sets the departure date in the format of YYYY-MM-DD HH:MM.
   * @param completeDepDate The departure date in format YYYY-MM-DD HH:MM
   */
  public void setCompleteDepDate(String completeDepDate) {
    this.completeDepDate = completeDepDate;
  }


  /**Returns the flight number of this flight.
   * @return the flightNum
   */
  public String getFlightNum() {
    return flightNum;
  }

  /**Sets the flight number of this flight to the given flight number from an administrator.
   * @param flightNum the flightNum to set
   */
  public void setFlightNum(String flightNum) {
    this.flightNum = flightNum;
  }

  /**Returns the departure date of this flight in format YYYY-MM-DD.
   * @return the departureDate
   */
  public String getDepartureDate() {
    return departureDate;
  }

  /**Returns the cost of this flight to two decimal places.
   * @return the cost to two decimal places
   */
  public Double getCost() {
    return cost;
  }

  /**Returns the travel time of this flight.
   * @return the travelTime
   */
  public double getTravelTime() {
    return travelTime;
  }

  /**Returns the airline of this flight.
   * @return the airline
   */
  public String getAirline() {
    return airline;
  }

  /**Allows an administrator to set the airline of this flight.
   * @param airline the airline to set
   */
  public void setAirline(String airline) {
    this.airline = airline;
  }

  /**Returns the origin of this flight.
   * @return the origin
   */
  public String getOrigin() {
    return origin;
  }

  /**Allows an admin to set the origin of this flight.
   * @param origin the origin to set
   */
  public void setOrigin(String origin) {
    this.origin = origin;
  }

  /**Returns the destination of this flight.
   * @return the destination
   */
  public String getDestination() {
    return destination;
  }

  /**Allows an administrator to set the destination of this flight.
   * @param destination the destination to set
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Returns the set containing all the flight information.
   * @return flightInfo The set of all information in the flight
   */
  public Set<Object> getFlightInfo() {
    return flightInfo;
  }

  /**
   * Returns the number of available seats.
   * @return the number of available seats
   */
  public Integer getNumSeats() {
    return numSeats;
  }


  /**
   * Sets the number of available seats.
   * @param numSeats The number of seats to be stored
   */
  public void setNumSeats(String numSeats) {
    this.numSeats = Integer.valueOf(numSeats);
  }

  /**
   * Sets the cost of the flight.
   * @param price The price of the flight
   */
  public void setPrice(String price) {
    this.cost = Double.valueOf(price);
  }

  /** Returns a String representation of a flight object with date in the format YYYY-MM-DD hh:mm
   * and cost with two decimals.The order is departure date, arrival date, airline, origin,
   * destination and cost.
   */
  public String toString() {
    DecimalFormat curFormat = new DecimalFormat("#0.00");

    String thisCost = curFormat.format(cost);
    return (flightNum + "," + completeDepDate + "," + completeArrDate + "," + airline + ","
            + origin + "," + destination + "," + thisCost + "," + numSeats + "\n");
  }

}
