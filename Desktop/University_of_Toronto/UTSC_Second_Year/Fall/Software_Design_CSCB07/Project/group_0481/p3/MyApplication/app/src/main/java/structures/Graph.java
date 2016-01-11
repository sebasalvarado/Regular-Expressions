package structures;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

import trip.Flight;
import trip.Itinerary;


/** Graph would sort the available flights and create a connection between each one of them.
 * @author Sebastian, Adam, Jose, Jacob
 */
public class Graph {

    // every flight holds a set of flights that go out the same city
    public HashMap<Flight, LinkedHashSet<Flight>> graph;

    private Set<Flight> allFlghts;

    private ArrayList<LinkedList<Flight>> listOfFlights;
    /**
     * Class Representing a Directed Graph where Every Key is a Flight mapping to the
     * List of Flights that start where the key landed.
     */
    public Graph() {
        this.graph = new HashMap<Flight, LinkedHashSet<Flight>>();
        this.listOfFlights = new ArrayList<LinkedList<Flight>>();
    }



    /** Return the ArrayList of all flights.
     * @return the listOfFlights
     */
    public ArrayList<LinkedList<Flight>> getListOfFlights() {
        return listOfFlights;
    }

    /** Set the list of all flights.
     * @param listOfFlights the listOfFlights to set
     */
    public void setListOfFlights(ArrayList<LinkedList<Flight>> listOfFlights) {
        this.listOfFlights = listOfFlights;
    }

    /** Calculates all possible itineraries for an origin and destination.
     * @param origin String indicating which City the Trip must Start
     * @param destination String indicating which City the Trip must end.
     * @return ArrayList of Itineraries for this given trip.
     */
    public ArrayList<Itinerary> findItineraries(String date, String origin, String destination) {
        // FIND ALL THE FLIGHTS THAT START AT ORIGIN AND RETURN A LIST OF THEM
        ArrayList<Flight> flightsOrigin = flightsAtOrigin(date, origin);
        // ADD ONE OF THEM TO THE LIST VISITED, AND CALL BREATH FIRST FUNCTION
        LinkedList<Flight> visited = new LinkedList<Flight>();
        for (Flight nextFlight: flightsOrigin) {
            visited.add(nextFlight);
            breadthSearch(visited, destination);
            visited.removeFirst();
        }
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        for (LinkedList<Flight> nextList: listOfFlights) {
            Flight firstFlight = (Flight) nextList.get(0);
            Flight lastFlight = (Flight) nextList.get(nextList.size() - 1);
            // loop to create an id for itinerary
            String itNumber = generateItNumber();
            Itinerary newItinerary = new Itinerary(nextList, itNumber, firstFlight.getOrigin(),
                    lastFlight.getDestination());
            itineraries.add(newItinerary);
        }
        return itineraries;
    }

    /**
     * Finds all the possible flights that start at given origin
     * @param date The Date where the trip is going to start in the form yyyy-mm-dd
     * @param origin City where the Flight must start
     * @return ArrayList of all the Flights that follow this conditions.
     */
    private ArrayList<Flight> flightsAtOrigin(String date, String origin) {
        ArrayList<Flight> finalList = new ArrayList<Flight>();
        // Loop through every Flight to determine if it starts at origin
        for (Flight nextFlight: graph.keySet()) {
            if ((nextFlight.getOrigin().equals(origin)) && (nextFlight.getDepartureDate().equals(date))) {
                finalList.add(nextFlight);
            }
        }
        return finalList;
    }

    /** A private method that creates a random number to identify an BookedItineraryActivity.
     * @return A random Generated String consisting of four numbers that identify the itinerary
     */
    private String generateItNumber() {
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String itNumber = String.valueOf(randomNumber);
        return itNumber;
    }

    /**
     * Recursive Algorithm to Traverse the Graph and include all Flights that have been visited.
     * Credits For this Algorithm: Casey Watson
     * Link:http://stackoverflow.com/questions/58306
     * /graph-algorithm-to-find-all-connections-between-two-arbitrary-vertices
     * @param visited List of flights previously used.
     * @param destination Where client will land
     */
    @SuppressWarnings("unchecked")
    private void breadthSearch(LinkedList<Flight> visited, String destination) {
        LinkedList<Flight> allFlights = adjacentFlights(visited.getLast());
        LinkedList<Flight> allFlightsClone = (LinkedList<Flight>) allFlights.clone();
        // check in the nodes adjacent to it, we finish when we reach a flight that ends
        // at destination
        if (visited.getLast().getDestination().equals(destination)) {
            //means we have got the flight that ends at destination
            this.listOfFlights.add((LinkedList<Flight>) visited.clone());
        }
        for (Flight nextFlight: allFlights) {
            if (visited.contains(nextFlight)) {
                continue;
            }
            if (nextFlight.getDestination().equals(destination)) {
                // here we find where the given flight ends at our destination
                visited.add(nextFlight);
                this.listOfFlights.add((LinkedList<Flight>) visited.clone());
                visited.removeLast();
                // we remove because if we are in destination there is no more ways to get there.
                allFlightsClone.remove(nextFlight);
            }
        }
        for (Flight nextFlight: allFlightsClone) {
            if (visited.contains(nextFlight) || nextFlight.getDestination().equals(destination)) {
                continue;
            }
            visited.addLast(nextFlight);
            // recursive call to look into that flight's corresponding connections
            breadthSearch(visited, destination);
            visited.removeLast();
        }
    }

    /**
     *
     * @param flight The flight for which we want to know all the Flights that start where it ended.
     * @return LinkedList of Flights departing at flight destination.
     */
    private LinkedList<Flight> adjacentFlights(Flight flight) {
        LinkedHashSet<Flight> adjacent = graph.get(flight);
        return new LinkedList<Flight>(adjacent);
    }


    @Override
    public String toString() {
        String finalString = new String();
        Set<Flight> allFlights = graph.keySet();
        for (Flight nextFlight: allFlights) {
            finalString += nextFlight.getFlightNum();
            finalString += " ->";
            for (Flight innerFlight: graph.get(nextFlight)) {
                finalString += innerFlight.getFlightNum() + " ,";
            }
            finalString += "\n";
        }
        return finalString;
    }

    /** Method that adds a flight to the Set.
     * @param flight the Flight that would be added.
     */

    public void addFlight(Flight flight) {
        // Adds the flight into the graph
        graph.put(flight, new LinkedHashSet<Flight>());
        Set<Flight> allFlights = graph.keySet();
        for (Flight currflight:allFlights) {
            LinkedHashSet<Flight> nextFlights = new LinkedHashSet<Flight>();
            for (Flight otherFlight:allFlights) {
                // get their differences in time
                long hourDifference = getTime(currflight,otherFlight);
                // filtering so that the connections are between 30 minutes and 6 hours
                // get six hours in minutes
                int sixHoursInMin = 360;
                if (currflight.getDestination().equals(otherFlight.getOrigin())
                        && (hourDifference < sixHoursInMin && hourDifference > 30)) {
                    nextFlights.add(otherFlight);
                }
                graph.put(currflight, nextFlights);
            }
        }
        allFlghts = graph.keySet();
    }

    /**Returns a set of all flights in this itinerary.
     * @return all the flights in this itinerary.
     */
    public Set<Flight> getFlights() {
        return (allFlghts);
    }



    /** Get The Travel time for a Trip.
     * @param firstFlight The first flight of the Trip.
     * @param secondFlight The Second flight of the Trip
     * @return Return the time for the difference of these flights in Minutes
     */
    public Long getTime(Flight firstFlight, Flight secondFlight) {
        String depDate = firstFlight.getCompleteArrDate();
        String arrivalDate = secondFlight.getCompleteDepDate();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.CANADA);

        Long travelTime;
        travelTime = (long) 0;
        // Calculates time for this flight in minutes then store it in a variable.
        try {
            Date deptDate = date.parse(depDate);
            Date arrivaldate = date.parse(arrivalDate);
            travelTime = (arrivaldate.getTime() - deptDate.getTime()) / 60000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (travelTime);
    }

    public void emptyGraphSearch(){
        this.listOfFlights = new ArrayList<LinkedList<Flight>>();
    }
    public void prepareGraph(){
        this.graph = new HashMap<Flight, LinkedHashSet<Flight>>();

    }
}
