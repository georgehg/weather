package com.crossover.trial.weather.model;

public interface UpdatableWheater {
	
	Boolean update(DataPoint oldData, DataPoint newData);

}
