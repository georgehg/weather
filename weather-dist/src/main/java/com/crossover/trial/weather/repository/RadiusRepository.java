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
import com.crossover.trial.weather.model.Radius;

public class RadiusRepository implements Repository<Radius, Double> {

	private static ConcurrentMap<Double, Radius> radiusData = new ConcurrentHashMap<Double, Radius>();
	
	@Override
	public Optional<Radius> get(Double radius) {
		return Optional.ofNullable(radiusData.get(radius));
	}

	@Override
	public Set<Double> getKeySet() {
		return Collections.unmodifiableSet(new HashSet<Double>(radiusData.keySet()));
	}
	
	@Override
	public void add(Radius radius) throws WeatherException {
		if (radiusData.get(radius.getRadius()) == null ) {
			throw new WeatherException("Radius "+radius.getRadius()+" already exists.");
		}		
		radiusData.put(radius.getRadius(), radius);
	}
	
	@Override
	public Radius update(Radius radius) {
		return radiusData.put(radius.getRadius(), radius);
	}

	@Override
	public Integer getCount() {
		return radiusData.size();
	}

	@Override
	public List<Radius> getAll() {
		return Collections.unmodifiableList(new ArrayList<Radius>(radiusData.values()));
	}

	@Override
	public void removeAll() {
		radiusData.clear();
	}
	
}
