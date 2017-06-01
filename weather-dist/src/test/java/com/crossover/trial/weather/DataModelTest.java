package com.crossover.trial.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.crossover.trial.weather.exceptions.WeatherException;
import com.crossover.trial.weather.model.Airport;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.repository.AirportDataRepository;
	
public class DataModelTest {
	
	private static AirportDataRepository airportRepository = AirportDataRepository.getInstance();
	
	@Test
	public void testDataPointBuilderAllFields() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withFirst(10)
										.withMean(22)
						    			.withMedian(20)
						    			.withLast(30)
						    			.withCount(40)
						    			.build();
		
		assertEquals(dataPoint.getFirst(), 10);
		assertEquals(dataPoint.getMean(), 22, 0.1);
		assertEquals(dataPoint.getSecond(), 20);
		assertEquals(dataPoint.getThird(), 30);
		assertEquals(dataPoint.getCount(), 40);
		
	}
	
	@Test
	public void testDataPointBuilderPartialFields() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withMean(22)
						    			.withFirst(10)
						    			.withCount(40)
						    			.build();
		
		assertEquals(dataPoint.getFirst(), 10);
		assertEquals(dataPoint.getMean(), 22, 0.1);
		assertEquals(dataPoint.getSecond(), 0);
		assertEquals(dataPoint.getThird(), 0);
		assertEquals(dataPoint.getCount(), 40);
		
	}
	
	@Test
	public void testDataPointBuilderNoFields() {
		
		DataPoint dataPoint = DataPoint.builder().build();
		
		assertEquals(dataPoint.getFirst(), 0);
		assertEquals(dataPoint.getMean(), 0.0, 0.1);
		assertEquals(dataPoint.getSecond(), 0);
		assertEquals(dataPoint.getThird(), 0);
		assertEquals(dataPoint.getCount(), 0);
		
	}
	
	@Test
	public void testDataPointEquals() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withFirst(10)
										.withMean(22)
						    			.withMedian(20)
						    			.withLast(30)
						    			.withCount(40)
						    			.build();
		
		DataPoint otherDataPoint = DataPoint.builder()
											.withFirst(10)
											.withMean(22)
							    			.withMedian(20)
							    			.withLast(30)
							    			.withCount(40)
							    			.build();
		
		assertEquals(dataPoint, otherDataPoint);
		
	}
	
	@Test
	public void testDataPointNotEquals() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withFirst(10)
										.withMean(22)
						    			.withMedian(20)
						    			.withLast(30)
						    			.withCount(40)
						    			.build();
		
		DataPoint otherDataPoint = DataPoint.builder()
											.withFirst(11)
											.withMean(32)
							    			.withMedian(22)
							    			.withLast(33)
							    			.withCount(44)
							    			.build();
		
		assertNotEquals(dataPoint, otherDataPoint);
		
	}
	
	
	@Test
	public void addAirport() throws WeatherException {
		
		airportRepository.removeAll();
		
		Airport airport = Airport.of("BOS", 42.364347, -71.005181);
		airportRepository.add(airport);
		airport = airportRepository.get("BOS").get();
		
		assertEquals(airport.getLatitude(), 42.364347, 0);
		assertEquals(airport.getLongitude(), -71.005181, 0.1);
		
	}

}
