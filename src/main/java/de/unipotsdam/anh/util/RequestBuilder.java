package de.unipotsdam.anh.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.filter.LoggingFilter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RequestBuilder {

	private final Map<Object, String> queryParamMap = new HashMap<Object, String>();
	private String url;
	private MediaType mediaType;
	
	private static final LoggingFilter loggingFilter = new LoggingFilter(
			Logger.getLogger(RequestBuilder.class.getName()), true);
	
	public RequestBuilder addQueryParam(String param, Object value){
		if(value != null && !StringUtils.isEmpty(param)) {
			queryParamMap.put(value, param);
		}
		
		return this;
	}
	
	public RequestBuilder addQueryParams(String param, List<String> competences){
		for(String o : competences) {
			addQueryParam(param, o);
		}
		return this;
	}
	
	public RequestBuilder withUrl(String url){
		this.url = url;
		return this;
	}
	
	public RequestBuilder withMedienType(MediaType mediaType){
		this.mediaType = mediaType;
		return this;
	}
	
	public <T> T get(Class<T> T){
		final Client client = ClientBuilder.newClient();
		try {
			WebTarget webTarget = client.target(url).register(loggingFilter);
			for(Entry<Object, String> e : queryParamMap.entrySet()) {
				webTarget = webTarget.queryParam(e.getValue(), e.getKey());
			}
			
			final Response response = webTarget.request(mediaType).get();
			if(response.getStatus() != 200) {
				return null;
			}
			
			if(mediaType == MediaType.APPLICATION_JSON_TYPE) {
				final Gson gson = new GsonBuilder().create();
				return gson.fromJson(response.readEntity(String.class),T);
			} else if(mediaType == MediaType.APPLICATION_XML_TYPE){
				return response.readEntity(T);
			} else {
				return response.readEntity(T);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return null;
	}

	public Response post(Object body){
		final Client client = ClientBuilder.newClient();
		try {
			WebTarget webTarget = client.target(url).register(loggingFilter);
			for(Entry<Object, String> e : queryParamMap.entrySet()) {
				webTarget = webTarget.queryParam(e.getValue(), e.getKey());
			}
			
			final Response response = body == null ? webTarget.request(mediaType).post(Entity.json(body), Response.class)
											: webTarget.request(mediaType).post(null, Response.class);
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}

		return null;
	}
	
	public int postWithStatus(Object body){
		final Response response = post(body);
		return response == null ? -1 : response.getStatus();
	}
	
	public String postWithString(Object body){
		final Response response = post(body);
		return response == null ? null : response.readEntity(String.class);
	}

	public <T> T post(Object body, Class<T> T){
		final Response response = post(body);
		if(response == null || response.getStatus() != 200) {
			return null;
		}
		
		if(mediaType == MediaType.APPLICATION_JSON_TYPE) {
			final Gson gson = new GsonBuilder().create();
			return gson.fromJson(response.readEntity(String.class),T);
		} else if(mediaType == MediaType.APPLICATION_XML_TYPE){
			return response.readEntity(T);
		}
		return null;
	}
}
