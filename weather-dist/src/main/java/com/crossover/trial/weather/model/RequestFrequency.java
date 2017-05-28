package com.crossover.trial.weather.model;

public class RequestFrequency {
	
	private String iata;
	
	private Double radius;
	
	private Integer frequency;

	private RequestFrequency(String iata, Double radius, Integer frequency) {
		this.iata = iata;
		this.radius = radius;
		this.frequency = frequency;
	}
	
	public static RequestFrequency of(String iata, Double radius) {
		return new RequestFrequency(iata, radius, 0);
	}

	public void touchFrequency() {
		this.frequency += 1;
	}

	public String getIata() {
		return iata;
	}

	public Double getRadius() {
		return radius;
	}
	
	public Integer getFrequency() {
		return frequency;
	}
	
}
