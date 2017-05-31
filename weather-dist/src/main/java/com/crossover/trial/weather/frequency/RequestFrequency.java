package com.crossover.trial.weather.frequency;

public abstract class RequestFrequency {
		
	protected Integer instanceFrequency;	
			
	private static Integer requestFrequency;

	protected void updateFrequency () {
		this.instanceFrequency++;
		increaseFrequency();
		
	}

	private void increaseFrequency() {
		requestFrequency++;
	}
	
	protected Integer getInstanceFrequency() {
		return this.instanceFrequency;
	}
	
	protected Integer getRequestFrequency() {
		return requestFrequency;
	}
	
}
