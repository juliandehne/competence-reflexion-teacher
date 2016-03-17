package de.unipotsdam.anh.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import uzuzjmd.competence.shared.dto.UserTree;
import de.unipotsdam.anh.util.AppUtil;
import de.unipotsdam.anh.util.RequestBuilder;
import de.unipotsdam.anh.util.UserTrees;

public class ActivityDao {

	
	public static synchronized List<UserTree> getActivityFromCourse(String course, String lmsSystem, String userEmail, String organization, String password, boolean isCrossdomain) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/lms/activities/usertree/xml/");
		
		if(isCrossdomain) {
			url.append("crossdomain/");
		}
		url.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		UserTrees response = requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_XML_TYPE)
							.addQueryParam("lmsSystem", lmsSystem)
							.addQueryParam("user", userEmail)
							.addQueryParam("password", password)
							.get(UserTrees.class);

		return response == null ? new ArrayList<UserTree>() : response.getActivity();
	}
	
	public static synchronized int addAktivity(String course, String lmsSystem, String organization, List<UserTree> usertree) {
		
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/lms/activities/usertree/json/add/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_XML_TYPE)
							.addQueryParam("lmsSystem", lmsSystem)
							.addQueryParam("organization", organization)
							.addQueryParam("usertree", usertree)
							.postWithStatus(null);
	}
	
	public static synchronized int addSuggestedActivityForCompetence(String competence, String aktivityUrl) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/SuggestedActivityForCompetence/create");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("competence", competence)
							.addQueryParam("activityUrl", aktivityUrl)
							.postWithStatus(null);
	}
	
	public static synchronized int deleteSuggestedActivityForCompetence(String competence, String aktivityUrl) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/SuggestedActivityForCompetence/delete/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("competence", competence)
							.addQueryParam("activityURL", aktivityUrl)
							.postWithStatus(null);
	}
}
