package trip;

import interfaces.Id;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;



/**A class representation of an itinerary object.This object consists of all possible
 * flights from a particular origin to destination.It implements the interface Id and
 * comparable.An object of type BookedItineraryActivity stores all its flights in a list, stores
 * its total cost from origin to destination.It also stores its id, origin and destination
 *  as well as total travel time.
 * @author Sebastian,Jacob, Adam, Jose
 */
public class Itinerary implements Id, Serializable, Comparable<Itinerary> {

  private LinkedList<Flight> listOfFlights;

  private double totalCost;
  private double totalTravelTime;

  private String id;
  private String origin;
  private String destination;

  private int availableSeats = Integer.MAX_VALUE;

  /**
   Returns the identification number of this itinerary as a string.
   * @return id The identification number as a String of this BookedItineraryActivity
   */
  public String getId() {
    return id;
  }

  /** Sets the identification number of this itinerary.
   * Sets the Id of this given BookedItineraryActivity
   */
  public void setId(String id) {
    this.id = id;
  }


  /**Returns the total travel time for this itinerary as a string.
   * @return the total travel time as a string in the format hh:mm.
   */
  public double getTotalTravelTime() {
    return totalTravelTime;
  }

  /**Returns the total cost of this itinerary in the format $$.$$
   * @return the total cost.
   */
  public double getTotalCost() {
    return totalCost;
  }


  /**Creates an itinerary object given list of flights, identification number as a string,
   * origin as a string and destination as a string.
   * @param listOfFlights
   *                   the list of valid flights for this itinerary.
   * @param id
   *                  the identification number of this itinerary.
   * @param origin
   *                  the origin for this itinerary.
   * @param destination
   *                  the destination for this itinerary.
   */
  public Itinerary(LinkedList<Flight> listOfFlights, String id, String origin, String destination) {
    this.listOfFlights = listOfFlights;
    setOrigin(origin);
    setDestination(destination);
    setTotalCost(listOfFlights);
    setTotalTravelTime(listOfFlights);
    setId(id);

    for (Flight curFlight:listOfFlights) {
      if (availableSeats > curFlight.getNumSeats()) {
        this.availableSeats = curFlight.getNumSeats();
      }
    }
  }


  /** Sets the total cost of this itinerary.
   *
   * @param flights the list of flights to set
   */
  private void setTotalCost(LinkedList<Flight> flights) {
    for (Flight nextFlight:flights) {
      this.totalCost += nextFlight.getCost();
    }
  }

  /**
   * Returns the number of available seats for the whole itinerary.
   * @return The number of available seats
   */
  public int getAvailableSeats() {
    return availableSeats;
  }

  /**
   * Sets the number of available seats for the whole itinerary.
   * @param curSeats The current amount of available seats to be saved
   */
  public void setAvailableSeats(int curSeats) {
    this.availableSeats = curSeats;
  }

  /** Sets the total travel time for this itinerary to a new value.
   * @param flights the flights to calculate total travel time from
   */
  private void setTotalTravelTime(LinkedList<Flight> flights) {
    if (this.listOfFlights.size() == 1) {
      Flight flight = listOfFlights.getFirst();
      this.totalTravelTime = flight.getTravelTime();
    } else {
      String departureTime = this.listOfFlights.get(0).completeDepDate;
      String arrivalTime = this.listOfFlights.getLast().completeArrDate;
      SimpleDateFormat date1 = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CANADA);
      SimpleDateFormat date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CANADA);

      try {
        Date deptDate = date1.parse(departureTime);
        Date arrivaldate = date2.parse(arrivalTime);
        double minutes = (arrivaldate.getTime() - deptDate.getTime()) / 60000;
        this.totalTravelTime = (minutes / 60);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
  }

  /**Returns the origin of this itinerary as a string.
   * @return the origin of this itinerary.
   */
  public String getOrigin() {
    return origin;
  }

  /**Sets the origin of this itinerary to a new value.
   *  * @param origin the origin to set
   */
  private void setOrigin(String origin) {
    this.origin = origin;
  }

  /**Returns the destination of this itinerary as a string.
   * @return the destination of this itinerary.
   */
  public String getDestination() {
    return destination;
  }

  /**Sets the destination of this itinerary to a new value.
   * @param destination the destination to set
   */
  private void setDestination(String destination) {
    this.destination = destination;
  }

  /**Returns the list of all flights in this itinerary as a linked list.
   * @return all the flights in this itinerary.
   */
  public LinkedList<Flight> getListOfFlights() {
    return listOfFlights;
  }

  /** Returns true if this itinerary is equal to the passed in itinerary,
   *  false otherwise.
   * @param itinerary
   *                the itinerary to compare with.
   * @return true if the two itineraries are equal otherwise, false.
   */
  public boolean equals(Itinerary itinerary) {
    // Compares the number of flights.
    /*if (getListOfFlights().size() != itinerary.getListOfFlights().size()) {
      return false;
    } else {
      boolean result = true;
      for (int i = 0; i < this.listOfFlights.size(); i++) {
        if (this.listOfFlights.get(i) != itinerary.getListOfFlights().get(i)) {
          result = false;
        }
      }
      return result;*/
    return this.toString().equals(itinerary.toString());
  }


  @Override
  /**Returns a number greater than zero if the passed in itinerary takes a longer travel
   * time than the current itinerary, zero if they are the same and a negative number otherwise.
   * @param itinerary
   *                 the itinerary to compare to.
   */
  public int compareTo(Itinerary itinerary) {

    double itTravel = itinerary.getTotalTravelTime();
    return (int) (this.totalTravelTime - itTravel);
  }


  /**Returns the string representation of this itinerary with each flight on its
   * own line.
   */
  public String toString() {

    String flightsReturn = "";

    for (int i = 0; i < listOfFlights.size(); i++) {
      String flight = "";
      flight += listOfFlights.get(i);
      int lastComma = flight.lastIndexOf(",");
      flight = flight.substring(0, lastComma);

      flightsReturn += flight + "\n";
    }

    // Creates an object of DecimalFormat for conversion.
    DecimalFormat formmat = new DecimalFormat("#0.00");
    String thisCost = formmat.format(totalCost);

    return flightsReturn + thisCost + "\n" + changeToString(this.totalTravelTime) + "\n";
  }



  /** Returns the travel time as a string in the format hh:mm
   *
   * @param travelTime
   *                 the total travel time to change
   * @return the string representation of travel time.
   */
  private String changeToString(double travelTime) {

    String finalString;
    int minutes = (int) (travelTime * 60);
    int remainder = (minutes % 60);
    int hours = minutes / 60;


    if ((hours < 10) && (remainder < 10)) {
      finalString = String.format("0%d:0%d", hours, remainder);
    } else if (hours < 10) {
      finalString = String.format("0%d:%d", hours,remainder);
    } else if (remainder < 10) {
      finalString = String.format("%d:0%d", hours,remainder);
    } else {
      finalString =  String.format("%d:%d", hours,remainder);
    }
    return finalString;
  }

}
