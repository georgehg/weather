package com.crossover.trial.weather.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.crossover.trial.weather.exceptions.WeatherException;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericInformation {

    private ConcurrentMap<DataPointType, DataPoint> dataPoint;
    
    private long lastUpdateTime;
	
    public AtmosphericInformation() {
    	this.dataPoint = new ConcurrentHashMap<DataPointType, DataPoint>();
    	this.lastUpdateTime = System.currentTimeMillis();
    }
    
    /**
     * update atmospheric information with the given data point for the given point type
     *
     * @param dataPoint the actual data point
     * @param pointType the data point type as a string
     */
    public void updateAtmosphericInformation(DataPoint dataPoint, DataPointType pointType) throws WeatherException {
    	
    	if (this.dataPoint.get(pointType) == null) {
    		this.dataPoint.put(pointType, dataPoint);
    		this.lastUpdateTime = System.currentTimeMillis();
    	} else {
	    	if (pointType.update(this.dataPoint.get(pointType), dataPoint)) {
	    		this.lastUpdateTime = System.currentTimeMillis();
	    	}
    	}
    	
    }

    public DataPoint getAtmosphericData(DataPointType dataPointType) {
        return this.dataPoint.get(dataPointType);
    }

    public DataPoint getWind() {
    	return this.dataPoint.get(DataPointType.WIND);
    }
    
	public DataPoint getTemperature() {
		return this.dataPoint.get(DataPointType.TEMPERATURE);
	}

    public DataPoint getHumidity() {
    	return this.dataPoint.get(DataPointType.HUMIDTY);
    }
    
    public DataPoint getPressure() {
    	return this.dataPoint.get(DataPointType.PRESSURE);
    }
    
    public DataPoint getCloudCover() {
    	return this.dataPoint.get(DataPointType.CLOUDCOVER);
    }

    public DataPoint getPrecipitation() {
    	return this.dataPoint.get(DataPointType.PRECIPITATION);
    }
    
    public Integer getSize() {
    	return this.dataPoint.size();
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

}
