/**
 * <h1>Find Flights</h1>
 * A small Java program FindFlights that accepts database credentials like JdbcCheckup,
 * and then accepts two cities (Los-Angeles and Chicago for example) and finds the flights
 * from the first to the second in the flights table. For the selected flights, it prints out
 * the flight number, distance, and duration of the flight, computed by time difference
 * (and displayed in hours:min:sec).
 * <p>
 * <b>Note:</b> Only SQL.
 *
 * @author  Wajeeh Anwar
 * @version 1.0
 * @since   2018-11-20
 */


SELECT flno, distance, departs, arrives
FROM flights
WHERE origin = 'Los-Angeles' AND destination = 'Chicago';
