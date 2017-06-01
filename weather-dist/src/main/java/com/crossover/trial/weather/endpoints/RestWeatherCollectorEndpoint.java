package com.crossover.trial.weather.endpoints;

import java.util.Optional;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.crossover.trial.weather.exceptions.WeatherException;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.model.DataPointType;
import com.crossover.trial.weather.repository.AirportDataRepository;
import com.google.gson.Gson;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path("/collect")
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    
	private final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
	
	private static AirportDataRepository airportRepository = AirportDataRepository.getInstance();

    /** shared gson json to object factory */
    private final static Gson gson = new Gson();

    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }
    

    @Override
    public Response updateWeather(String iataCode, String pointType, String datapointJson) {
        try {
            addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
        	return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(e.getMessage())).build();
        }
        return Response.ok().build();
    }


    @Override
    public Response getAirports() {
        return Response.status(Response.Status.OK).entity(gson.toJson(airportRepository.getAll())).build();
    }


    @Override
    public Response getAirport(String iata) {
    	Optional<Airport> airport = airportRepository.get(iata);
    	
    	if (airport.isPresent()) {
    		return Response.status(Response.Status.OK).entity(gson.toJson(airport.get())).build();
    	} else {
			return Response.status(Response.Status.NOT_FOUND).entity(gson.toJson("Airpot not found with IATA: " + iata)).build();
    	}
    }
    

    @Override
    public Response addAirport(String iata, String latString, String longString) {    	
    	
		try {
			airportRepository.add(Airport.of(iata, Double.valueOf(latString), Double.valueOf(longString)));
			return Response.ok().build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(gson.toJson(e.getMessage())).build();
		} catch (WeatherException e) {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(gson.toJson(e.getMessage())).build();
		}
    }

    @Override
    public Response deleteAirport(String iata) {
    	try {
    		airportRepository.remove(iata);
    		return Response.noContent().build();
    	} catch (WeatherException e) {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(gson.toJson(e.getMessage())).build();
		}
    }

    @Override
    public Response exit() {
        System.exit(0);
        return Response.noContent().build();
    }
    //
    // Internal support methods
    //

    /**
     * Update the airports weather data with the collected data.
     *
     * @param iataCode the 3 letter IATA code
     * @param pointType the point type {@link DataPointType}
     * @param dp a datapoint object holding pointType data
     *
     * @throws WeatherException if the update can not be completed
     */
    private void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
       
    	Optional<AtmosphericInformation> ai = airportRepository.get(iataCode)
        										.map((airport) -> airport.getAtmosphericInformation());
    	
    	if (!ai.isPresent()) {
    		throw new WeatherException("couldn't update atmospheric data");
    	}
    	DataPointType dataPointType;
    	try {
    		dataPointType = DataPointType.valueOf(pointType.toUpperCase());
    	} catch (IllegalArgumentException e) {
        	throw new WeatherException("couldn't update atmospheric data. Cause: " + e.getMessage());
        }
        
        ai.get().updateAtmosphericInformation(dp, dataPointType);
    }

}
