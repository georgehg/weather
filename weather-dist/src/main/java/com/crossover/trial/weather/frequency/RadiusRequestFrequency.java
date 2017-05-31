package com.crossover.trial.weather.frequency;

public class RadiusRequestFrequency extends RequestFrequency {
	
	private Double radius;

	public RadiusRequestFrequency(Double radius) {
		this.radius = radius;
	}
	
	public Double getRadius() {
		return radius;
	}
}
