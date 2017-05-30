package com.crossover.trial.weather.model;

/**
 * The various types of data points we can collect.
 *
 * @author code test administrator
 */
public enum DataPointType implements UpdatableWheater {
    WIND {
    	@Override
    	public Boolean update(DataPoint oldData, DataPoint newData) {
    		if (newData.getMean() >= 0) {
    			oldData = newData;
    			return true;
    		}
    		return false;
    	}
    },
    TEMPERATURE {
    	@Override
    	public Boolean update(DataPoint oldData, DataPoint newData) {
    		if (newData.getMean() >= -50 && newData.getMean() < 100) {
    			oldData = newData;
    			return true;
    		}
    		return false;
    	}
    },
    HUMIDTY {
    	@Override
    	public Boolean update(DataPoint oldData, DataPoint newData) {
    		if (newData.getMean() >= 0 && newData.getMean() < 100) {
    			oldData = newData;
    			return true;
    		}
    		return false;
    	}
    },
    PRESSURE {
    	@Override
    	public Boolean update(DataPoint oldData, DataPoint newData) {
    		if (newData.getMean() >= 650 && newData.getMean() < 800) {
    			oldData = newData;
    			return true;
    		}
    		return false;
    	}
    },
    CLOUDCOVER {
    	@Override
    	public Boolean update(DataPoint oldData, DataPoint newData) {
    		if (newData.getMean() >= 0 && newData.getMean() < 100) {
    			oldData = newData;
    			return true;
    		}
    		return false;
    	}
    },
    PRECIPITATION {
    	@Override
    	public Boolean update(DataPoint oldData, DataPoint newData) {
    		if (newData.getMean() >=0 && newData.getMean() < 100) {
    			oldData = newData;
    			return true;
    		}
    		return false;
    	}
    }
    
}
