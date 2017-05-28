package com.crossover.trial.weather.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.crossover.trial.weather.exceptions.WeatherException;
import com.crossover.trial.weather.model.RequestFrequency;

public class RequestFrequencyRepository implements Repository<RequestFrequency, String> {

	private static ConcurrentMap<String, RequestFrequency> requestFrequencyData = new ConcurrentHashMap<String, RequestFrequency>();
	
	@Override
	public RequestFrequency get(String iataCode) {
		return requestFrequencyData.get(iataCode);
	}

	@Override
	public Set<String> getKeySet() {
		return Collections.unmodifiableSet(new HashSet<String>(requestFrequencyData.keySet()));
	}
	
	@Override
	public void add(RequestFrequency requestFrequency) throws WeatherException {
		if (requestFrequencyData.get(requestFrequency.getIata()) == null ) {
			throw new WeatherException("RequestFrequency "+requestFrequency.getIata()+" already exists.");
		}		
		requestFrequencyData.put(requestFrequency.getIata(), requestFrequency);
	}
	
	@Override
	public RequestFrequency update(RequestFrequency requestFrequency) {
		return requestFrequencyData.put(requestFrequency.getIata(), requestFrequency);
	}

	@Override
	public Integer getCount() {
		return requestFrequencyData.size();
	}

	@Override
	public List<RequestFrequency> getAll() {
		return Collections.unmodifiableList(new ArrayList<RequestFrequency>(requestFrequencyData.values()));
	}

	@Override
	public void removeAll() {
		requestFrequencyData.clear();
	}

	public Integer getMaxRadius() {
		return this.getAll()
						.stream()
						.map((freq) -> freq.getRadius())
						.max(Double::compare)
						.orElse(1000.0)
						.intValue() + 1;
	}
	
}
