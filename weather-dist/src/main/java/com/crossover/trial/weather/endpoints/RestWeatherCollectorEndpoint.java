package com.crossover.trial.weather.endpoints;

import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    
	public final static Logger LOGGER = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());
	
	private static AirportDataRepository airportRepository = new AirportDataRepository();

    /** shared gson json to object factory */
    public final static Gson gson = new Gson();

    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    public Response updateWeather(@PathParam("iata") String iataCode,
                                  @PathParam("pointType") String pointType,
                                  String datapointJson) {
        try {
            addDataPoint(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));
        } catch (WeatherException e) {
        	return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).build();
    }


    @Override
    public Response getAirports() {
        return Response.status(Response.Status.OK).entity(airportRepository.getKeySet()).build();
    }


    @Override
    public Response getAirport(@PathParam("iata") String iata) {
        return Response.status(Response.Status.OK).entity(airportRepository.get(iata)).build();
    }


    @Override
    public Response addAirport(@PathParam("iata") String iata,
                               @PathParam("lat") String latString,
                               @PathParam("long") String longString) {
    	
		try {
			airportRepository.add(Airport.of(iata, Double.valueOf(latString), Double.valueOf(longString)));
			return Response.status(Response.Status.OK).build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (WeatherException e) {
			return Response.status(Response.Status.PRECONDITION_FAILED).entity(e.getMessage()).build();
		}

    }


    @Override
    public Response deleteAirport(@PathParam("iata") String iata) {
    	//airportRepository.remove(iata)
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
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
    public void addDataPoint(String iataCode, String pointType, DataPoint dp) throws WeatherException {
       
    	Optional<AtmosphericInformation> ai = airportRepository.get(iataCode)
        										.map((airport) -> airport.getAtmosphericInformation());
    	
    	if (!ai.isPresent()) {
    		throw new WeatherException("couldn't update atmospheric data");
    	}
    	
        if (! Arrays.asList(DataPointType.values()).contains(pointType)) {
        	throw new WeatherException("couldn't update atmospheric data");
        }
        
        ai.get().updateAtmosphericInformation(dp, DataPointType.valueOf(pointType));
    }

}
