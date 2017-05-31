package com.crossover.trial.weather.model;

public interface UpdatableDataPoint {
	
	Boolean update(DataPoint oldData, DataPoint newData);

}
