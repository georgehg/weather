package com.crossover.trial.weather.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
@ToString
@EqualsAndHashCode
public class Airport {
	
	/** earth radius in KM */
	private static final double R = 6372.8;

    /** the three letter IATA code */
    private String iata;

    /** latitude value in degrees */
    private double latitude;

    /** longitude value in degrees */
    private double longitude;
    
    /** Airport Atmospheric Informations */
    private AtmosphericData atmosphericData;
    
    private Integer frequency;

	private Airport(String iata, double latitude, double longitude, AtmosphericData atmosphericData) {
		this.iata = iata;
		this.latitude = latitude;
		this.longitude = longitude;
		this.atmosphericData = atmosphericData;
		this.frequency = 0;
	}
	
	public static Airport of(String iata, double latitude, double longitude) {
		return new Airport(iata, latitude, longitude, new AtmosphericData());
	}
	
	public static Airport of(String iata, double latitude, double longitude, AtmosphericData atmosphericData) {
		return new Airport(iata, latitude, longitude, atmosphericData);
	}
	
	/**
	 * Haversine distance between two airports.
	 *
	 * @param ad1
	 *            airport 1
	 * @param other
	 *            airport 2
	 * @return the distance in KM
	 */
	public double calculateDistanceTo(Airport other) {
		double deltaLat = Math.toRadians(other.latitude - this.latitude);
		double deltaLon = Math.toRadians(other.longitude - this.longitude);
		double a = Math.pow(Math.sin(deltaLat / 2), 2)
				+ Math.pow(Math.sin(deltaLon / 2), 2) * Math.cos(this.latitude) * Math.cos(other.latitude);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}

    public String getIata() {
        return this.iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

	public AtmosphericData getAtmosphericInformation() {
		return atmosphericData;
	}

	public void setAtmosphericInformation(AtmosphericData atmosphericInformation) {
		this.atmosphericData = atmosphericInformation;
	}
	
	public Integer getFrequency() {
		return frequency;
	}
	
	public void increaseFrequency() {
		this.frequency++;
	}

}
