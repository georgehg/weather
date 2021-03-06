package com.crossover.trial.weather;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.crossover.trial.weather.endpoints.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.endpoints.RestWeatherQueryEndpoint;
import com.crossover.trial.weather.endpoints.WeatherCollectorEndpoint;
import com.crossover.trial.weather.endpoints.WeatherQueryEndpoint;
import com.crossover.trial.weather.model.AtmosphericInformation;
import com.crossover.trial.weather.model.DataPoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WeatherEndpointTest {

	private WeatherQueryEndpoint _query = new RestWeatherQueryEndpoint();

	private WeatherCollectorEndpoint _update = new RestWeatherCollectorEndpoint();

	private Gson _gson = new Gson();

	private DataPoint _dp;

	@Before
	public void setUp() throws Exception {
		RestWeatherQueryEndpoint.init();
		_dp = DataPoint.builder().withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
		_update.updateWeather("BOS", "wind", _gson.toJson(_dp));
		_query.weather("BOS", "0").getEntity();
	}

	@Test
	public void testPing() throws Exception {
		String ping = _query.ping();
		JsonElement pingResult = new JsonParser().parse(ping);
		assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());
		assertEquals(5, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size());
		assertEquals(1, pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().get("BOS").getAsDouble(), 0.1);
		assertEquals(1, pingResult.getAsJsonObject().get("radius_freq").getAsJsonArray().get(0).getAsInt());

	}

	@Test
	public void testGet() throws Exception {
		List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
		assertEquals(ais.get(0).getWind(), _dp);
	}

	@Test
	public void testGetNearby() throws Exception {
		// check datasize response
		_update.updateWeather("JFK", "wind", _gson.toJson(_dp));
		_update.updateWeather("EWR", "wind", _gson.toJson(_dp.toBuilder().withMean(40).build()));
		_update.updateWeather("LGA", "wind", _gson.toJson(_dp.toBuilder().withMean(30).build()));

		List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("JFK", "200").getEntity();
		assertEquals(3, ais.size());
	}

	@Test
	public void testUpdate() throws Exception {

		DataPoint windDp = DataPoint.builder().withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22)
				.build();
		_update.updateWeather("BOS", "wind", _gson.toJson(windDp));
		_query.weather("BOS", "0").getEntity();

		String ping = _query.ping();
		JsonElement pingResult = new JsonParser().parse(ping);
		assertEquals(1, pingResult.getAsJsonObject().get("datasize").getAsInt());

		DataPoint cloudCoverDp = DataPoint.builder().withCount(4).withFirst(10).withMedian(60).withLast(100)
				.withMean(50).build();
		_update.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

		List<AtmosphericInformation> ais = (List<AtmosphericInformation>) _query.weather("BOS", "0").getEntity();
		assertEquals(ais.get(0).getWind(), windDp);
		assertEquals(ais.get(0).getCloudCover(), cloudCoverDp);
	}
	
	
	@Test
	public void addAirports() {
		
		Response response = _update.addAirport("LHR","51.4775", "-0.461389");		
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());	
		
		response = _update.getAirport("LHR");
		JsonElement result = new JsonParser().parse(response.getEntity().toString());
		
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals("LHR",result.getAsJsonObject().get("iata").getAsString());

	}
	
	
	@Test
	public void deleteAirports() {
		
		Response response = _update.addAirport("STN", "40.79935", "-74.4148747");		
		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());	
		
		response = _update.deleteAirport("STN");
		assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

	}

}