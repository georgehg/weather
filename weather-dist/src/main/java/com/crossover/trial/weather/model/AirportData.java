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
public class AirportData {

    /** the three letter IATA code */
    private String iata;

    /** latitude value in degrees */
    private double latitude;

    /** longitude value in degrees */
    private double longitude;
    
    /** Airport Atmospheric Informations */
    private AtmosphericInformation atmosphericInformation;

	public AirportData(String iata, double latitude, double longitude, AtmosphericInformation atmosphericInformation) {
		this.iata = iata;
		this.latitude = latitude;
		this.longitude = longitude;
		this.atmosphericInformation = atmosphericInformation;
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
    
}
