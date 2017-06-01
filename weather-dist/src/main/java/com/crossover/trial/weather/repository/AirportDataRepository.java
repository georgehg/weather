package com.crossover.trial.weather.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.crossover.trial.weather.exceptions.WeatherException;
import com.crossover.trial.weather.model.Airport;

public class AirportDataRepository implements Repository<Airport, String> {
	
	private static AirportDataRepository instance = new AirportDataRepository();

	private ConcurrentMap<String, Airport> airportData = new ConcurrentHashMap<String, Airport>();
	
	public static AirportDataRepository getInstance() {
		return instance;
	}

	@Override
	public Optional<Airport> get(String iataCode) {
		return Optional.ofNullable(airportData.get(iataCode));
	}

	@Override
	public Set<String> getKeySet() {
		return Collections.unmodifiableSet(new HashSet<String>(airportData.keySet()));
	}

	@Override
	public void add(Airport airport) throws WeatherException {
		if (airportData.get(airport.getIata()) != null ) {
			throw new WeatherException("Airport "+airport.getIata()+" already exists.");
		}
		airportData.put(airport.getIata(), airport);
	}
	
	@Override
	public Airport update(Airport airport) {
		return airportData.put(airport.getIata(), airport);
	}

	@Override
	public Integer getCount() {
		return airportData.size();
	}

	@Override
	public List<Airport> getAll() {
		return Collections.unmodifiableList(new ArrayList<Airport>(airportData.values()));
	}
	
	@Override
	public void remove(String key) throws WeatherException {
		if (airportData.get(key) == null ) {
			throw new WeatherException("Airport with IATA["+key+"] already exists.");
		}
		airportData.remove(key);		
	}

	@Override
	public void removeAll() {
		airportData.clear();
	}
	
}
