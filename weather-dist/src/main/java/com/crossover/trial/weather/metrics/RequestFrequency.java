package com.crossover.trial.weather.metrics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestFrequency {
	
	private static RequestFrequency instance = new RequestFrequency();
	
	private ConcurrentMap<RequestType, ConcurrentMap<String, Integer>> requestFrequency = new ConcurrentHashMap<RequestType, ConcurrentMap<String, Integer>>();	
		
	public static RequestFrequency getInstance() {
		return instance;
	}

	public void updateRequestFrequency(String key, RequestType requestType) {		
		ConcurrentMap<String, Integer> reqFreq = this.requestFrequency.getOrDefault(requestType, new ConcurrentHashMap<String, Integer>());
		reqFreq.put(key, reqFreq.getOrDefault(key, 0) + 1);
		this.requestFrequency.put(requestType, reqFreq);		
	}
	
	public double getRequestFrequency(String key, RequestType requestType) {
		ConcurrentMap<String, Integer> reqFreq = this.requestFrequency.get(requestType);
		return (double) reqFreq.getOrDefault(key, 0) / reqFreq.size();
	}
	
	public int[] getRequestHistogram(RequestType requestType) {
		
		ConcurrentMap<String, Integer> reqFreq = this.requestFrequency.get(requestType);
		
		int[] hist;
		try {
			int max = reqFreq.keySet().stream()
									.map((value) -> Double.valueOf(value))
									.max(Double::compare)
									.orElse(1000.0)
									.intValue() + 1;
			hist = new int[max];
			
		} catch (NumberFormatException e) {
			return new int[0];
		}
		
        for (String value: reqFreq.keySet()) {
            int i = Integer.valueOf(value) % 10;
            hist[i] += reqFreq.get(value);
        }
        
        return hist;
        
	}
	
	public void clear() {
		this.requestFrequency.clear();
	}
	
}
