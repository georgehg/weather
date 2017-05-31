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
import com.crossover.trial.weather.frequency.RequestFrequency;

public class RequestFrequecyRepository implements Repository<RequestFrequency, String> {
	
	private static RequestFrequecyRepository instance = new RequestFrequecyRepository();

	private ConcurrentMap<String, RequestFrequency> radiusData = new ConcurrentHashMap<String, RequestFrequency>();
	
	private RequestFrequecyRepository() {}
	
	public static RequestFrequecyRepository getInstance() {
		return instance;
	}
	
	@Override
	public Optional<RequestFrequency> get(String key) {
		return Optional.ofNullable(radiusData.get(key));
	}

	@Override
	public Set<String> getKeySet() {
		return Collections.unmodifiableSet(new HashSet<Double>(radiusData.keySet()));
	}
	
	@Override
	public void add(RequestFrequency radius) throws WeatherException {
		if (radiusData.get(radius.getRadius()) != null ) {
			throw new WeatherException("Radius "+radius.getRadius()+" already exists.");
		}		
		radiusData.put(radius.getRadius(), radius);
	}
	
	@Override
	public RequestFrequency update(RequestFrequency radius) {
		return radiusData.put(radius.getRadius(), radius);
	}

	@Override
	public Integer getCount() {
		return radiusData.size();
	}

	@Override
	public List<RequestFrequency> getAll() {
		return Collections.unmodifiableList(new ArrayList<RequestFrequency>(radiusData.values()));
	}

	@Override
	public void removeAll() {
		radiusData.clear();
	}
	
}
