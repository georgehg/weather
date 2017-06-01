package com.crossover.trial.weather.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.exceptions.WeatherException;
import com.crossover.trial.weather.metrics.RequestFrequency;
import com.crossover.trial.weather.metrics.RequestType;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.google.gson.Gson;

/**
 * The Weather App REST endpoint allows clients to query, update and check
 * health stats. Currently, all data is held in memory. The end point deploys to
 * a single container
 *
 * @author code test administrator
 */
@Path("/query")
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

	private final static Logger LOGGER = Logger.getLogger("WeatherQuery");

	/** shared gson json to object factory */
	private static final Gson gson = new Gson();

	private static final long MILLISECONDS_OF_DAY = 86400000;

	/** all known airports */
	private static AirportDataRepository airportRepository = AirportDataRepository.getInstance();

	/**
	 * Internal performance counter to better understand most requested
	 * information, this map can be improved but for now provides the basis for
	 * future performance optimizations. Due to the stateless deployment
	 * architecture we don't want to write this to disk, but will pull it off
	 * using a REST request and aggregate with other performance metrics
	 * {@link #ping()}
	 */
	private static RequestFrequency requestFrequency = RequestFrequency.getInstance();

	/**
	 * Retrieve service health including total size of valid data points and
	 * request frequency information.
	 *
	 * @return health stats for the service as a string
	 */
	@Override
	public String ping() {
		Map<String, Object> retval = new HashMap<>();

		int datasize = 0;
		Map<String, Double> freq = new HashMap<>();
		
		for (Airport airport : airportRepository.getAll()) {

			AtmosphericInformation ai = airport.getAtmosphericInformation();

			// updated in the last day
			if (ai.getLastUpdateTime() > System.currentTimeMillis() - MILLISECONDS_OF_DAY) {
				// count if any datapoint is present
				datasize += ai.getSize()>0 ? 1 : 0;
			}
			
			double frac = requestFrequency.getRequestFrequency(airport.getIata(), RequestType.AIRPORT);
			freq.put(airport.getIata(), frac);
		}

		retval.put("datasize", datasize);
		retval.put("iata_freq", freq);
		retval.put("radius_freq", requestFrequency.getRequestHistogram(RequestType.RADIUS));

		return gson.toJson(retval);
	}

	/**
	 * Given a query in json format {'iata': CODE, 'radius': km} extracts the
	 * requested airport information and return a list of matching atmosphere
	 * information.
	 *
	 * @param iata
	 *            the iataCode
	 * @param radiusString
	 *            the radius in km
	 *
	 * @return a list of atmospheric information
	 */
	@Override
	public Response weather(String iata, String radiusString) {

		double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);

		requestFrequency.updateRequestFrequency(iata, RequestType.AIRPORT);
		requestFrequency.updateRequestFrequency(radiusString, RequestType.RADIUS);
		
		Optional<Airport> airPort = airportRepository.get(iata);
		
		if (!airPort.isPresent()) {
			return Response.status(Response.Status.NOT_FOUND).entity(gson.toJson("Airpot not found with IATA: " + iata)).build();
		} 

		List<AtmosphericInformation> retval = new ArrayList<>();
		
		if (radius == 0) {
			retval.add(airPort.get().getAtmosphericInformation());
		} else {
			for (Airport otherAirPort : airportRepository.getAll()) {
				if (airPort.get().calculateDistanceTo(otherAirPort) <= radius) {
					AtmosphericInformation ai = otherAirPort.getAtmosphericInformation();
					if (ai.getCloudCover() != null || ai.getHumidity() != null || ai.getPrecipitation() != null
							|| ai.getPressure() != null || ai.getTemperature() != null || ai.getWind() != null) {
						retval.add(ai);
					}
				}
			}
		}
		return Response.status(Response.Status.OK).entity(retval).build();
	}
	
    /*static {
        init();
    }*/

	/**
	 * A dummy init method that loads hard coded data
	 */
	public static void init() {
		
		airportRepository.removeAll();
		requestFrequency.clear();

		addAirport("BOS", 42.364347, -71.005181);
		addAirport("EWR", 40.6925, -74.168667);
		addAirport("JFK", 40.639751, -73.778925);
		addAirport("LGA", 40.777245, -73.872608);
		addAirport("MMU", 40.79935, -74.4148747);
	}
	
    /**
     * Add a new known airport to our list.
     *
     * @param iataCode 3 letter code
     * @param latitude in degrees
     * @param longitude in degrees
     *
     * @return the added airport
     */
    private static void addAirport(String iataCode, double latitude, double longitude) {        
    	Airport airport = Airport.of(iataCode, latitude, longitude);
    	try {
			airportRepository.add(airport);
		} catch (WeatherException e) {
			LOGGER.warning(e.getMessage());
		}        
    }
}
