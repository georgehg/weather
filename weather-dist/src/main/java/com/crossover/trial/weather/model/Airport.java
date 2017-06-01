package com.crossover.trial.weather.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Basic airport information.
 *
 * @author code test administrator
 */
@Data
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
    private AtmosphericInformation atmosphericInformation;

	private Airport(String iata, double latitude, double longitude, AtmosphericInformation atmosphericInformation) {
		this.iata = iata;
		this.latitude = latitude;
		this.longitude = longitude;
		this.atmosphericInformation = atmosphericInformation;
	}
	
	public static Airport of(String iata, double latitude, double longitude) {
		return new Airport(iata, latitude, longitude, new AtmosphericInformation());
	}
	
	public static Airport of(String iata, double latitude, double longitude, AtmosphericInformation atmosphericInformation) {
		return new Airport(iata, latitude, longitude, atmosphericInformation);
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
	
}