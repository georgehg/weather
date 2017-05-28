package com.crossover.trial.weather.endpoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericData;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.crossover.trial.weather.repository.RequestFrequencyRepository;
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

	public final static Logger LOGGER = Logger.getLogger("WeatherQuery");

	/** shared gson json to object factory */
	public static final Gson gson = new Gson();

	/** all known airports */
	private static AirportDataRepository airportRepository = new AirportDataRepository();

	/**
	 * Internal performance counter to better understand most requested
	 * information, this map can be improved but for now provides the basis for
	 * future performance optimizations. Due to the stateless deployment
	 * architecture we don't want to write this to disk, but will pull it off
	 * using a REST request and aggregate with other performance metrics
	 * {@link #ping()}
	 */
	private static RequestFrequencyRepository rfRepository = new RequestFrequencyRepository();

	static {
		init();
	}

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
		for (String iataCode : airportRepository.getKeySet()) {

			AtmosphericData ai = airportRepository.get(iataCode).getAtmosphericInformation();

			if (ai.getCloudCover() != null
					|| ai.getHumidity() != null
					|| ai.getPressure() != null
					|| ai.getPrecipitation() != null
					|| ai.getTemperature() != null
					|| ai.getWind() != null) {
					// updated in the last day
					if (ai.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
						datasize++;
					}
			}
		}

		retval.put("datasize", datasize);

		Map<String, Double> freq = new HashMap<>();
		// fraction of queries
		for (Airport airport : airportRepository.getAll() ) {
			double frac = (double) rfRepository.get(airport.getIata()).getFrequency()
						/ rfRepository.getCount();
			freq.put(airport.getIata(), frac);
		}

		retval.put("iata_freq", freq);

		int maxRadius = rfRepository.getMaxRadius();

		int[] hist = new int[maxRadius];
		for (Double key : rfRepository.getRadiusSet()) {
			int i = key.intValue() % 10;
			hist[i] += rfRepository.getRadiusVal(key);
		}
		retval.put("radius_freq", hist);

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
		
		rfRepository.updateFrequency(iata, radius);
		
		Airport airPort = airportRepository.getData(iata);

		List<AtmosphericData> retval = new ArrayList<>();
		
		if (radius == 0) {
			retval.add(airPort.getAtmosphericInformation());
		} else {
			for (Airport otherAirPort : airportRepository.getAll()) {
				if (airPort.calculateDistanceTo(otherAirPort) <= radius) {
					AtmosphericData ai = otherAirPort.getAtmosphericInformation();
					if (ai.getCloudCover() != null || ai.getHumidity() != null || ai.getPrecipitation() != null
							|| ai.getPressure() != null || ai.getTemperature() != null || ai.getWind() != null) {
						retval.add(ai);
					}
				}
			}
		}
		return Response.status(Response.Status.OK).entity(retval).build();
	}



	/**
	 * A dummy init method that loads hard coded data
	 */
	public static void init() {
		airportRepository.removeAll();
		rfRepository.removeAll();

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
    public static void addAirport(String iataCode, double latitude, double longitude) {        
    	Airport airport = Airport.of(iataCode, latitude, longitude, new AtmosphericData());
    	airportRepository.add(airport);        
    }

}
