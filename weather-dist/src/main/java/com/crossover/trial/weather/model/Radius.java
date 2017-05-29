package com.crossover.trial.weather.model;

public class Radius {
	
	private Double radius;
	
	private Integer frequency;

	private Radius(Double radius, Integer frequency) {
		this.radius = radius;
		this.frequency = frequency;
	}
	
	public static Radius of(Double radius) {
		return new Radius(radius, 0);
	}

	public void increaseFrequency() {
		this.frequency++;
	}

	public Double getRadius() {
		return radius;
	}
	
	public Integer getFrequency() {
		return frequency;
	}
	
}
