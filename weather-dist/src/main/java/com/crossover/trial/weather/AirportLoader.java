package com.crossover.trial.weather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {

    /** end point for read queries */
    private WebTarget query;

    /** end point to supply updates */
    private WebTarget collect;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        this.query = client.target("http://localhost:9090/query");
        this.collect = client.target("http://localhost:9090/collect");
    }

    public void upload(InputStream airportDataStream) throws IOException{
    	
    	final int IATA_FIELD = 4;
    	final int ICAO_FIELD = 5;
    	final int LATITUDE_FIELD = 6;
    	final int LONGITUDE_FIELD = 7;
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(airportDataStream));
        String l = null;
        while ((l = reader.readLine()) != null) {
        	String[] fields = l.replace("\"", "") .split(",");
        	WebTarget path = collect.path("/airport/"+ (fields[IATA_FIELD].isEmpty()?fields[ICAO_FIELD]:fields[IATA_FIELD]) +"/"+fields[LATITUDE_FIELD]+"/"+fields[LONGITUDE_FIELD]);
        	Response response = path.request().post(null);
        }        
    }

    public static void main(String args[]) throws IOException{
    	if (args.length < 1) {
    		System.err.println("No file input");
            System.exit(1);
    	}
    	
        File airportDataFile = new File(args[0]);
        if (!airportDataFile.exists() || airportDataFile.length() == 0) {
            System.err.println(airportDataFile + " is not a valid input");
            System.exit(1);
        }

        AirportLoader al = new AirportLoader();
        al.upload(new FileInputStream(airportDataFile));
        System.exit(0);
    }
}
