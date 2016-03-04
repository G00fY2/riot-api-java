package com.g00fy2.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

public abstract class RiotAPI {
	
	private String urlHost;
	private String urlPath;
	private String urlQuery;
	private String region;
	private String apiVersion;
	private String apiCategory;
	private Gson gson;
	final String[] regions = new String[] {"br","eune","euw","kr","lan","las","na","oce","ru","tr"};

	public RiotAPI(final Map<String, String> apiValues, final String apiVersion, final String apiCategory){
		urlHost = apiValues.get("urlHost");
		urlPath = apiValues.get("urlPath");
		urlQuery = "api_key=" + apiValues.get("apiKey");
		setRegion(apiValues.get("region"));	
		this.apiVersion = apiVersion;
		this.apiCategory = apiCategory;
		gson = new Gson();
	}
	
	protected <T> T getObjectFromJsonUrl(String urlPath, String urlQuery, Class<T> classOf) throws URISyntaxException, IOException{
		try
		{
			HttpsURLConnection conn = establishHttpsConn(urlPath, urlQuery);
			try ( BufferedReader reader = new BufferedReader( new InputStreamReader(conn.getInputStream() ) ) )
			{
				T object = gson.fromJson(reader, classOf);
				System.out.println("DEBUG : JSON deserialization succeeded");
				return object;
			}
			catch (IOException e) //GSON (JsonIOException, JsonSyntaxException), HttpURLConnection (IOException)
			{
				throw e;
			}
		}
		catch (URISyntaxException | IOException e)
		{
			throw e;
		}
	}
	
	protected <T> T getObjectFromJsonUrl(String urlPath, String urlQuery, Type typeOf) throws URISyntaxException, IOException{
		try
		{
			HttpsURLConnection conn = establishHttpsConn(urlPath, urlQuery);
			try ( BufferedReader reader = new BufferedReader( new InputStreamReader(conn.getInputStream() ) ) )
			{
				T object = gson.fromJson(reader, typeOf);
				System.out.println("DEBUG : JSON deserialization succeeded");
				return object;
			}
			catch (IOException e) //GSON (JsonIOException, JsonSyntaxException), HttpURLConnection (IOException)
			{
				throw e;
			}
		}
		catch (URISyntaxException | IOException e)
		{
			throw e;
		}	
	}
	
	private HttpsURLConnection establishHttpsConn(String urlPath, String urlQuery) throws IOException, URISyntaxException{
		try
	    {
			URI uri = new URI("https", null, region+urlHost, -1, urlPath , urlQuery, null);
			URL obj = uri.toURL();
			HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
			conn.setRequestMethod("GET");

			System.out.println("DEBUG : Sending 'GET' request to URL : " + obj.toString());
			System.out.println("DEBUG : Response Code : " + conn.getResponseCode());
			
			return conn;
	    }
		catch (URISyntaxException | IOException e)
		{
			throw e;
		}		
	}
	
	public void setRegion(String region) throws IllegalArgumentException{
		region.trim().toLowerCase();
		if ( !(Arrays.asList(regions).contains(region)) ){
			throw new IllegalArgumentException("Unkown API region.");		
		}
		else this.region = region;
	}	
	
	protected String buildUrlPath(){
		return urlPath + region + apiVersion + apiCategory;
	}
	
	public String getRegion(){
		return region;
	}
	
	protected String getUrlQuery(){
		return urlQuery;
	}


}