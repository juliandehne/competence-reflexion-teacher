package de.unipotsdam.anh.dao;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import uzuzjmd.competence.shared.DESCRIPTORSETType;
import uzuzjmd.competence.shared.StringList;
import uzuzjmd.competence.shared.dto.EPOSTypeWrapper;
import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LearningTemplateDao {

	
	public static synchronized LearningTemplateResultSet getLearningProjectTemplate(String learningTemplateName) {
		final Client client = ClientBuilder.newClient();
		
		try {
			final WebTarget webResource = client
					.target("http://localhost:8084/competences/xml/learningtemplate/get/"
										+ learningTemplateName);
			LearningTemplateResultSet result = webResource
												.request(MediaType.APPLICATION_XML)
												.get(LearningTemplateResultSet.class);
			return result;
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return null;
	}
	
	/**
	 * send new Template to the server
	 * @param learningTemplateName
	 */
	public static synchronized int createTemplate(String learningTemplateName) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target("http://localhost:8084/competences/json/learningtemplate/add/");
		try {
			Response response = webResource
				.queryParam("learningTemplateName", learningTemplateName)
				.request(MediaType.APPLICATION_JSON)
				.post(null);
			return response.getStatus();
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return -1;
	}
	
	public static synchronized int createTemplate(String learningTemplateName, LearningTemplateResultSet learningTemplateResultSet) {
		final Client client = ClientBuilder.newClient();
		
		try {
			final WebTarget webResource = client
					.target("http://localhost:8084/competences/xml/learningtemplate/add/"
									+ learningTemplateName)
					.queryParam("learningTemplateResultSet",
							learningTemplateResultSet);
			
			Response result = webResource.request(MediaType.APPLICATION_XML)
					.post(Entity.entity(learningTemplateResultSet,
							MediaType.APPLICATION_XML));
			
			return result.getStatus();
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return -1;
	}
	
	/**
	 * add a competence to the model
	 * @param competence
	 * @param operator
	 * @param learningTemplate
	 * @param catchWords
	 * @return
	 */
	public static synchronized int createOneCompetence(String competence, String operator, String learningTemplate, String... catchWords) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target("http://localhost:8084/competences/json/addOne/");
		try {
			Response response = webResource
				.queryParam("competence", competence)
				.queryParam("operator", operator)
				.queryParam("catchwords",(Object[]) catchWords)
				.queryParam("learningTemplateName", learningTemplate)
				.request()
				.post(null);
			return response.getStatus();
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return -1;
	}
	
	/**
	 * GET the operator for a given competence
	 * @param competence
	 * @return
	 */
	public static synchronized String getOperatorFromCompetence(String competence) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target("http://localhost:8084/competences/json/operator/");
		try {
			Response response = webResource
				.queryParam("competence", competence)
				.request()
				.post(null);
			return response.readEntity(String.class);
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		return null;
	}
	
	/**
	 * get all Learning template from database
	 * @return
	 */
	public static synchronized StringList findAll() {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target("http://localhost:8084/competences/xml/learningtemplates/");
		try {
			StringList response = webResource
				.request(MediaType.APPLICATION_XML)
				.get(StringList.class);
			
			return response;
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return null;
	}
	
	public static synchronized Graph getGraphFromCourse(String course) {
		final Client client = ClientBuilder.newClient();
		final WebTarget webResource = client.target("http://localhost:8084/competences/json/prerequisite/graph/")
											.path(course);
		final Gson gson = new GsonBuilder().create();
		try {
			Response response = webResource
				.request(MediaType.APPLICATION_JSON)
				.get();
			return gson.fromJson(response.readEntity(String.class), Graph.class);
		} catch ( Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return null;
	}
	
	public static synchronized void createEpos(EPOSTypeWrapper typeWrapper) {
		
		try {
			JAXBContext context = JAXBContext.newInstance(DESCRIPTORSETType.class);
	        Marshaller m = context.createMarshaller();
	        //for pretty-print XML in JAXB
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	
	        // Write to System.out for debugging
	        for(DESCRIPTORSETType desSetType: typeWrapper.getEposCompetences()) {
	        	m.marshal(desSetType, System.out);
	        	
	        	// Write to File
		        m.marshal(desSetType, new File("expos.xml"));
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		final Client client = ClientBuilder.newClient();
//		final WebTarget webResource = client.target("http://localhost:8084/competences/xml/learningtemplates/addEpos");
//		try {
//			Response response = webResource
//									.request(MediaType.APPLICATION_XML)
//									.post(Entity.entity(typeWrapper, MediaType.APPLICATION_XML));
//			
//			System.out.println(response.getStatus() + " " + response.getStatusInfo());
//			System.out.println(response.readEntity(String.class));
//		
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	
	public static synchronized DESCRIPTORSETType getEposFrom(String learningTemplate) {
		try {
			JAXBContext jc = JAXBContext.newInstance(DESCRIPTORSETType.class);
	
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        DESCRIPTORSETType descriptorsetType = (DESCRIPTORSETType) unmarshaller.unmarshal(new File("C:/Users/fides-WHK/Wissensmodellierung/competence-reflexion-2/epos-ex.xml"));
	        
	        return descriptorsetType;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
