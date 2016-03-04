package de.unipotsdam.anh.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.UserCourseListItem;
import de.unipotsdam.anh.util.AppUtil;
import de.unipotsdam.anh.util.RequestBuilder;

public class CourseDao {
	
	public static synchronized List<UserCourseListItem> getCourseFromUser(String lmsSystem, String userEmail, String organization, String password) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/lms/courses/")
			.append(lmsSystem + "/")
			.append(userEmail + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		UserCourseListItem[] course = requestBuilder.withUrl(url.toString())
										.withMedienType(MediaType.APPLICATION_JSON_TYPE)
										.addQueryParam("password", password)
										.addQueryParam("organization", organization)
										.get(UserCourseListItem[].class);
		
		return course == null ? new ArrayList<UserCourseListItem>() : Arrays.asList(course);
	}

	public static synchronized List<String> getCompetenceFromCourse(String course) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/coursecontext/selected/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		String[] list = requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.get(String[].class);
		return list == null ? new ArrayList<String>() : Arrays.asList(list);
	}
	
	public static synchronized String getRequirementFromCourse(String course) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/coursecontext/requirements/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.TEXT_PLAIN_TYPE)
							.get(String.class);
	}
	
	public static synchronized int deleteCourse(String course) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/coursecontext/delete/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.postWithStatus(null);
	}
	
	public static synchronized int createCourse(String course, String compulsory, List<String> competences, String requirements) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/coursecontext/create/")
			.append(course + "/")
			.append(compulsory + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParams("competences", competences)
							.addQueryParam("requirements", requirements)
							.postWithStatus(null);
	}
	
	public static synchronized int createPrerequisiteForCourse(String course, String linkedCompetence, List<String> selectedCompetences) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/prerequisite/create/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("linkedCompetence", linkedCompetence)
							.addQueryParams("selectedCompetences", selectedCompetences)
							.postWithStatus(null);
	}
	
	public static synchronized int deletePrerequisiteForCourse(String course, String linkedCompetence, List<String> competences) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/prerequisite/delete/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("linkedCompetence", linkedCompetence)
							.addQueryParams("competences", competences)
							.postWithStatus(null);
	}
	
	public static synchronized Graph getPrerequisiteGraph(String course, List<String> selectedCompetences) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/prerequisite/graph/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParams("selectedCompetences", selectedCompetences)
							.get(Graph.class);
	}
	
	public static synchronized List<String> getPrerequisiteForCompetence(String course, String competence) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/prerequisite/required/")
			.append(course + "/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		String[] response = requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("competence", competence)
							.get(String[].class);

		return response == null ? new ArrayList<String>() : Arrays.asList(response);
	}
	
	public static synchronized int addSuggestedCourseForCompetence(String competence, String course) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/SuggestedCourseForCompetence/create/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("competence", competence)
							.addQueryParam("course", course)
							.postWithStatus(null);
	}
	
	public static synchronized int deleteSuggestedCourseForCompetence(String competence, String course) {
		final StringBuilder url = new StringBuilder();
		url.append(AppUtil.getBaseUrl())
			.append("/competences/SuggestedCourseForCompetence/delete/");
		
		final RequestBuilder requestBuilder = new RequestBuilder();
		return requestBuilder.withUrl(url.toString())
							.withMedienType(MediaType.APPLICATION_JSON_TYPE)
							.addQueryParam("competence", competence)
							.addQueryParam("course", course)
							.postWithStatus(null);
	}
}
