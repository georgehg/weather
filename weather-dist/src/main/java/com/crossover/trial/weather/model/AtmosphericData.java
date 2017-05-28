package com.crossover.trial.weather.model;

import com.crossover.trial.weather.exceptions.WeatherException;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericData {

    /** temperature in degrees celsius */
    private DataPoint temperature;

    /** wind speed in km/h */
    private DataPoint wind;

    /** humidity in percent */
    private DataPoint humidity;

    /** precipitation in cm */
    private DataPoint precipitation;

    /** pressure in mmHg */
    private DataPoint pressure;

    /** cloud cover percent from 0 - 100 (integer) */
    private DataPoint cloudCover;

    /** the last time this data was updated, in milliseconds since UTC epoch */
    private long lastUpdateTime;

    public AtmosphericData() {
    }

    protected AtmosphericData(DataPoint temperature, DataPoint wind, DataPoint humidity, DataPoint percipitation, DataPoint pressure, DataPoint cloudCover) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.precipitation = percipitation;
        this.pressure = pressure;
        this.cloudCover = cloudCover;
        this.lastUpdateTime = System.currentTimeMillis();
    }
    
    /**
     * update atmospheric information with the given data point for the given point type
     *
     * @param dataPoint the actual data point
     * @param pointType the data point type as a string
     */
    public void updateAtmosphericInformation(DataPoint dataPoint, DataPointType dataPointType) throws WeatherException {
        
    	switch (dataPointType) {
		case WIND:
			if (dataPoint.getMean() >= 0) {
                this.wind = dataPoint;
                this.lastUpdateTime = System.currentTimeMillis();
            }
			break;
			
		case TEMPERATURE:
            if (dataPoint.getMean() >= -50 && dataPoint.getMean() < 100) {
            	this.temperature = dataPoint;
            	this.lastUpdateTime = System.currentTimeMillis();
            }
			break;

		case HUMIDTY:
            if (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100) {
            	this.humidity = dataPoint;
            	this.lastUpdateTime = System.currentTimeMillis();
            }
			break;

		case PRESSURE:
            if (dataPoint.getMean() >= 650 && dataPoint.getMean() < 800) {
            	this.pressure = dataPoint ;
            	this.lastUpdateTime = System.currentTimeMillis();
            }
			break;

		case CLOUDCOVER:
            if (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100) {
            	this.cloudCover = dataPoint;
            	this.lastUpdateTime = System.currentTimeMillis();
            }
			break;

		case PRECIPITATION:
            if (dataPoint.getMean() >=0 && dataPoint.getMean() < 100) {
            	this.precipitation = dataPoint;
            	this.lastUpdateTime = System.currentTimeMillis();
            }
			break;
			
		default:
			throw new WeatherException("couldn't update atmospheric data");
		}
    	
    }

    public DataPoint getTemperature() {
        return this.temperature;
    }

    public DataPoint getWind() {
        return this.wind;
    }

    public DataPoint getHumidity() {
        return this.humidity;
    }

    public DataPoint getPrecipitation() {
        return this.precipitation;
    }

    public DataPoint getPressure() {
        return this.pressure;
    }

    public DataPoint getCloudCover() {
        return this.cloudCover;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

}
