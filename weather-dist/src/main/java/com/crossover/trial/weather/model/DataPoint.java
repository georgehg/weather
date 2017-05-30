package com.crossover.trial.weather.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
@ToString
@EqualsAndHashCode
public class DataPoint {
	
	private int first;

	private double mean;

	private int second;

	private int third;

	private int count;
	
    /** private constructor, use the builder to create this object */
    protected DataPoint(int first, Double mean, int second, int third, int count) {
    	this.first = first;
    	this.mean = mean;
        this.second = second;
        this.third = third;
        this.count = count;
    }

    /** the mean of the observations */
    public double getMean() {
        return this.mean;
    }

    /** 1st quartile -- useful as a lower bound */
    public int getFirst() {
        return this.first;
    }

    /** 2nd quartile -- median value */
    public int getSecond() {
        return this.second;
    }

    /** 3rd quartile value -- less noisy upper value */
    public int getThird() {
        return this.third;
    }

    /** the total number of measurements */
    public int getCount() {
        return this.count;
    }
    
    public static DataPoint.Builder builder() {
    	return new DataPoint.Builder();
    }
    
    public DataPoint.Builder toBuilder() {
    	Builder builder = new DataPoint.Builder();
    	return builder.withFirst(this.getFirst())
    					.withMean(this.getMean())
		    			.withMedian(this.getSecond())
		    			.withLast(this.getThird())
		    			.withCount(this.count);
    }

    public static final class Builder {
    	
    	private int first = 0;
    	private double mean = 0.0;
        private int median = 0;
        private int last = 0;
        private int count = 0;
        
        private Builder(){}
        
        public Builder withFirst(int first) {
        	this.first = first;
        	return this;
        }

        public Builder withMean(double mean) {
        	this.mean = mean;
        	return this;
        }

        public Builder withMedian(int median) {
        	this.median = median;
            return this;
        }

        public Builder withLast(int last) {
        	this.last = last;
        	return this;
        }
        
        public Builder withCount(int count) {
        	this.count = count;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this.first, this.mean, this.median, this.last, this.count);
        }
    }
}
