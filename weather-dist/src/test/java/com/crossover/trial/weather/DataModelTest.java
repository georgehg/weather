package com.crossover.trial.weather;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.crossover.trial.weather.model.DataPoint;
	
public class DataModelTest {
	
	@Test
	public void testDataPointBuilderAllFields() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withMean(22)
						    			.withFirst(10)
						    			.withSecond(20)
						    			.withThird(30)
						    			.withCount(40)
						    			.build();
		
		assertEquals(dataPoint.getMean(), 22, 0.1);
		assertEquals(dataPoint.getFirst(), 10);
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
		
		assertEquals(dataPoint.getMean(), 22, 0.1);
		assertEquals(dataPoint.getFirst(), 10);
		assertEquals(dataPoint.getSecond(), 0);
		assertEquals(dataPoint.getThird(), 0);
		assertEquals(dataPoint.getCount(), 40);
		
	}
	
	@Test
	public void testDataPointBuilderNoFields() {
		
		DataPoint dataPoint = DataPoint.builder().build();
		
		assertEquals(dataPoint.getMean(), 0.0, 0.1);
		assertEquals(dataPoint.getFirst(), 0);
		assertEquals(dataPoint.getSecond(), 0);
		assertEquals(dataPoint.getThird(), 0);
		assertEquals(dataPoint.getCount(), 0);
		
	}
	
	@Test
	public void testDataPointEquals() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withMean(22)
						    			.withFirst(10)
						    			.withSecond(20)
						    			.withThird(30)
						    			.withCount(40)
						    			.build();
		
		DataPoint otherDataPoint = DataPoint.builder()
											.withMean(22)
							    			.withFirst(10)
							    			.withSecond(20)
							    			.withThird(30)
							    			.withCount(40)
							    			.build();
		
		assertEquals(dataPoint, otherDataPoint);
		
	}
	
	@Test
	public void testDataPointNotEquals() {
		
		DataPoint dataPoint = DataPoint.builder()
										.withMean(22)
						    			.withFirst(10)
						    			.withSecond(20)
						    			.withThird(30)
						    			.withCount(40)
						    			.build();
		
		DataPoint otherDataPoint = DataPoint.builder()
											.withMean(32)
							    			.withFirst(11)
							    			.withSecond(22)
							    			.withThird(33)
							    			.withCount(44)
							    			.build();
		
		assertNotEquals(dataPoint, otherDataPoint);
		
	}

}
