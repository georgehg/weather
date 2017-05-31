package com.crossover.trial.weather.frequency;

public class AiportRequestFrequency extends RequestFrequency {

	private String iata;
	
	public AiportRequestFrequency(String iata) {
		this.iata = iata;
	}
	
	public String getIata() {
		return iata;
	}

}
