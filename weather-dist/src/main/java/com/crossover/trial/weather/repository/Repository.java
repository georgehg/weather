package com.crossover.trial.weather.repository;

import java.util.List;
import java.util.Set;

import com.crossover.trial.weather.exceptions.WeatherException;

public interface Repository<T, K> {
	
	void add(T model) throws WeatherException;
	
	T update(T model);
	
	T get(K key);
	
	List<T> getAll();

	Set<K> getKeySet();
	
	Integer getCount();
	
	void removeAll();

}
