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

	private double mean;
	
	private int first;

	private int second;

	private int third;

	private int count;
	
    /** private constructor, use the builder to create this object */
    private DataPoint(double mean, int first, int second, int third, int count) {
    	this.mean = mean;
    	this.first = first;
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

    public static final class Builder {
    	
    	private double mean = 0.0;
        private int first = 0;
        private int second = 0;
        private int third = 0;
        private int count = 0;
        
        public Builder withMean(double mean) {
        	this.mean = mean;
        	return this;
        }

        public Builder withFirst(int first) {
            this.first= first;
            return this;
        }

        public Builder withSecond(int second) {
        	this.second = second;
            return this;
        }

        public Builder withThird(int third) {
        	this.third = third;
        	return this;
        }
        
        public Builder withCount(int count) {
        	this.count = count;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this.mean, this.first, this.second, this.third, this.count);
        }
    }
}
