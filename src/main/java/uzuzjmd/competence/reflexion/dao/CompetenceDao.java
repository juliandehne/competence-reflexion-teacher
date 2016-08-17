package uzuzjmd.competence.reflexion.dao;



import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import uzuzjmd.competence.reflexion.util.AppUtil;
import uzuzjmd.competence.shared.competence.CompetenceData;
import datastructures.trees.HierarchyChange;
import datastructures.trees.HierarchyChangeSet;

public class CompetenceDao {
	
	public static void addCompetenceStringGiven(String competence, String learningTemplate, List<String> catchwords, String operator) {		
		System.err.println("entering addCompetence 2");
		
		CompetenceData data = new CompetenceData();
		data.setCatchwords(catchwords);
		data.setForCompetence(competence);
		data.setLearningProjectName(learningTemplate);
		data.setOperator(catchwords.iterator().next());
		data.setOperator(operator);
		
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl());
		url.append("/api1/competences/");
		url.append(competence);				
		
		System.err.println(url.toString());
					
		Client client = ClientBuilder.newClient();	
		Response result = client.target(url.toString()).request(MediaType.TEXT_PLAIN).put(Entity.entity(data, MediaType.APPLICATION_XML));		
		System.err.println(result.getStatus() + " for addSubCompetence");
	}
	
	public static void addSubCompetence(String competence, String subCompetence) {		
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/api1/competences/hierarchy/update");
					
		
		HierarchyChangeSet set = new HierarchyChangeSet();	
		HierarchyChange change = new HierarchyChange("Kompetenz", competence, subCompetence);
		set.getElements().add(change);
		Client client = ClientBuilder.newClient();				
		Response result = client.target(url.toString()).request(MediaType.TEXT_PLAIN).post(Entity.entity(set, MediaType.APPLICATION_XML));
		System.err.println(result.getStatus() + " for update hierarchy 2");
	}
}
